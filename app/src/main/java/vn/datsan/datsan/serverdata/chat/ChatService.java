package vn.datsan.datsan.serverdata.chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import vn.datsan.datsan.models.Group;
import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 8/2/16.
 */
public class ChatService {

    private static final String TAG = ChatService.class.getName();

    private DatabaseReference chatDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHATS);
    private static ChatService instance = new ChatService();

    private ChatService() {}

    public static ChatService getInstance() {
        return instance;
    }

    public void createChat(int groupType, String groupName, List<String> members) {
        DatabaseReference newGroupRef = chatDatabaseRef.push();
        String generatedKey = newGroupRef.getKey();
        newGroupRef.setValue(new Group().toMap());
    }

    public void createChatGroup(Group group) {
        chatDatabaseRef.push().setValue(group.toMap());
    }

    public DatabaseReference getChatDatabaseRef(String chatId) {
        return chatDatabaseRef.child(chatId);
    }

    public void createChat(String name) {

    }
}
