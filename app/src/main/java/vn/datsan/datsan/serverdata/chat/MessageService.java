package vn.datsan.datsan.serverdata.chat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.datsan.datsan.models.chat.Message;
import vn.datsan.datsan.models.chat.TypingSignal;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 8/13/16.
 */
public class MessageService {

    private static final String TAG = MessageService.class.getName();

    private DatabaseReference messageDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_MESSAGES);
    private static MessageService instance = new MessageService();

    private ChildEventListener childEventListener;

    private MessageService() {}

    public static MessageService getInstance() {
        return instance;
    }

    /**
     * Load messages history for a chat id
     * @param chatId
     * @return array list of messages
     */
    public List<Message> loadChatHistory(String chatId, ValueEventListener listener) {

        return null;
    }

    /**
     * Save a new message to the firebase and update as last message to related chat
     * @param message
     * @return new message key
     */
    public Message send(Message message) {

        message.getChat().setLastMessage(message.getMessage());
        final String chatId = message.getChat().getId();

        DatabaseReference chatMessgeRef = getMessageDatabaseRef(chatId);
        String messageId = chatMessgeRef.push().getKey();
        message.setId(messageId);

        final Message messageAllParticipants = message;

        // Get list of all participants of the chat
        MemberService.getInstance().getMemberDatabaseRef(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    // Chat no member, should never happened
                    AppLog.e(TAG, "Chat " + chatId + " has no member");
                    return;
                }

                Map<String, Object> childUpdates = new HashMap<>();

                // Add all participants to the message broadcast list
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String memberId = data.getKey();
                    childUpdates.put(Constants.FIREBASE_USERS + "/" + memberId + "/chats/" + chatId, messageAllParticipants.getChat().toMap());
                }

                childUpdates.put(Constants.FIREBASE_CHATS + "/" + chatId, messageAllParticipants.getChat().toMap());
                childUpdates.put(Constants.FIREBASE_MESSAGES + "/" + chatId + "/" + messageAllParticipants.getId(), messageAllParticipants.toMap());

                FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return message;
    }

    public DatabaseReference getMessageDatabaseRef(String chatId) {
        return messageDatabaseRef.child(chatId);
    }

    public DatabaseReference getTypingSignalDatabaseRef(String chatId) {
        String typingMessageKey = getTypingSignalKeyForChat(chatId);
        return messageDatabaseRef.child(typingMessageKey);
    }

    public void startTypingSignal(String chatId, TypingSignal signal) {
        getTypingSignalDatabaseRef(chatId).child(signal.getUserId()).setValue(signal);
    }

    public void stopTypingSignal(String chatId, TypingSignal signal) {
        getTypingSignalDatabaseRef(chatId).child(signal.getUserId()).setValue(null);
    }

    /**
     * Create listener for typing signal
     */
    public void listenOnTypingSignal(String chatId, final CallBack.OnResultReceivedListener callback) {
        if (childEventListener == null) {

            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    TypingSignal signal = dataSnapshot.getValue(TypingSignal.class);
                    if (callback != null) {
                        callback.onResultReceived(signal);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    TypingSignal signal = dataSnapshot.getValue(TypingSignal.class);
                    if (callback != null) {
                        callback.onResultReceived(signal);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    TypingSignal signal = dataSnapshot.getValue(TypingSignal.class);
                    signal.setStopped(true);
                    if (callback != null) {
                        callback.onResultReceived(signal);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    AppLog.d("TYPING_SINAL", "onChildMoved");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    AppLog.d("TYPING_SINAL", "onCancelled");
                }
            };

            getTypingSignalDatabaseRef(chatId).addChildEventListener(childEventListener);
        }
    }

    public void removeTypingSignalListeners(String chatId) {
        if (childEventListener != null) {
            getTypingSignalDatabaseRef(chatId).removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    private String getTypingSignalKeyForChat(String chatId) {
        return chatId + "_" + Constants.FIREBASE_TYPING_SIGNAL;
    }
}
