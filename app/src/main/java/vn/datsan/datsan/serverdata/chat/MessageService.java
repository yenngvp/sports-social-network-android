package vn.datsan.datsan.serverdata.chat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.datsan.datsan.models.chat.Member;
import vn.datsan.datsan.models.chat.Message;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 8/13/16.
 */
public class MessageService {

    private static final String TAG = MessageService.class.getName();

    private DatabaseReference messageDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_MESSAGES);
    private static MessageService instance = new MessageService();

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
}
