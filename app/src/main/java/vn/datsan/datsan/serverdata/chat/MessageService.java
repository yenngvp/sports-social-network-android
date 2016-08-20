package vn.datsan.datsan.serverdata.chat;

import com.google.firebase.auth.FirebaseAuth;
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
    public Message save(Message message) {

        message.getChat().setLastMessage(message.getMessage());
        String chatId = message.getChat().getId();

        DatabaseReference chatMessgeRef = getMessageDatabaseRef(chatId);
        String messageId = chatMessgeRef.push().getKey();
        message.setId(messageId);

        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(Constants.FIREBASE_CHATS + "/" + chatId, message.getChat().toMap());
        childUpdates.put(Constants.FIREBASE_USERS + "/" + currentUserUid + "/chats/" + chatId, message.getChat().toMap());
        childUpdates.put(Constants.FIREBASE_MESSAGES + "/" + chatId + "/" + messageId, message.toMap());

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

        return message;
    }

    public DatabaseReference getMessageDatabaseRef(String chatId) {
        return messageDatabaseRef.child(chatId);
    }
}
