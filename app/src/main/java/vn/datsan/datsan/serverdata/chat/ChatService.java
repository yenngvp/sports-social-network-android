package vn.datsan.datsan.serverdata.chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.datsan.datsan.models.Group;
import vn.datsan.datsan.models.UserRole;
import vn.datsan.datsan.models.chat.Chat;
import vn.datsan.datsan.models.chat.Member;
import vn.datsan.datsan.models.chat.Message;
import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 8/2/16.
 */
public class ChatService {

    private static final String TAG = ChatService.class.getName();

    private DatabaseReference chatDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHATS);
    private static ChatService instance = new ChatService();

    private MemberService memberService = MemberService.getInstance();

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

    public Chat createChat(String title, String chatType, List<Member> initialMembers, String linkedGroup) {
        Chat chat = new Chat(title, chatType, linkedGroup);
        String chatKey = chatDatabaseRef.push().getKey();
        chat.setId(chatKey);

        // Create a map of initial members
        Map<String, Object> userRolesMap = new HashMap<>();
        for (Member member : initialMembers) {
            userRolesMap.putAll(member.toUserRoleMap());
        }

        // Because a chat always has a list of members when creating
        // so we would create chat and member objects at the same time to make sure the consistency
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(Constants.FIREBASE_CHATS + "/" + chatKey, chat.toMap());
        childUpdates.put(Constants.FIREBASE_MEMBERS + "/" + chatKey, userRolesMap);

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

        return chat;
    }

    public void updateChatTitle(String chatId, String title) {

    }

    public void updateLastMessage(String chatId, Message lastMessage) {

    }

    public void addMember(String chatId, Member member) {
        memberService.add(chatId, member);
    }
}
