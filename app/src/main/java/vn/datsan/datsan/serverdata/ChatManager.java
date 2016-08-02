package vn.datsan.datsan.serverdata;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 8/2/16.
 */
public class ChatManager {

    private static final String TAG = ChatManager.class.getName();

    private DatabaseReference userDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHATS);
    private static ChatManager instance = new ChatManager();

    private ChatManager() {}

    public static ChatManager getInstance() {
        return instance;
    }

    public void createChatGroup(int groupType, String groupName, List<String> members) {

    }
}
