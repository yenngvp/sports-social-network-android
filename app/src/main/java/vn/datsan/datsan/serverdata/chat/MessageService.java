package vn.datsan.datsan.serverdata.chat;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.datsan.datsan.models.chat.Chat;
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

        message.getChat().setLastMessage(message);
        final String chatId = message.getChat().getId();

        final DatabaseReference chatMessageRef = getMessageDatabaseRef(chatId);
        String messageId = chatMessageRef.push().getKey();
        message.setId(messageId);

        // Store the new message to messages object, and
        // Update the chat with the new last message
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(Constants.FIREBASE_CHATS + "/" + chatId, message.getChat().toMap());
        childUpdates.put(Constants.FIREBASE_MESSAGES + "/" + chatId + "/" + message.getId(), message.toMap());

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

        // Increase messageCount on the chat in a managed transaction
        DatabaseReference chatRef = ChatService.getInstance().getChatDatabaseRef(chatId);
        chatRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Chat chat = mutableData.getValue(Chat.class);
                if (chat == null) {
                    return Transaction.success(mutableData);
                }

                chat.setMessageCount(chat.getMessageCount() + 1);

                // Set value and report transaction success
                mutableData.setValue(chat);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                AppLog.e(TAG, "chat.runTransaction:onComplete:" + databaseError);
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
