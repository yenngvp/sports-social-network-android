package vn.datsan.datsan.serverdata.chat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import vn.datsan.datsan.models.Group;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.models.UserRole;
import vn.datsan.datsan.models.chat.Chat;
import vn.datsan.datsan.models.chat.Member;
import vn.datsan.datsan.models.chat.Message;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.GroupManager;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 8/2/16.
 */
public class ChatService {

    private static final String TAG = ChatService.class.getName();

    private static ChatService instance = new ChatService();

    private DatabaseReference chatDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHATS);
    private ValueEventListener valueEventListener;

    private MemberService memberService = MemberService.getInstance();

    List<Chat> chatHistory = new ArrayList<>();

    private ChatService() {}

    public static ChatService getInstance() {
        return instance;
    }

    public void createChat(int groupType, String groupName, List<String> members) {
        DatabaseReference newGroupRef = chatDatabaseRef.push();
        String generatedKey = newGroupRef.getKey();
        newGroupRef.setValue(new Group().toMap());
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
        for (Member member : initialMembers) {
            // Create chat for all users are members
            childUpdates.put(Constants.FIREBASE_USERS + "/" + member.getUserId() + "/chats/" + chatKey, chat.toMap());
        }
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

        return chat;
    }

    /**
     * Start chat with someone
     * @return
     */
    public Chat createOneToOneChat(User me, User buddy) {
        // Create chat has two members me and other
        List<Member> members = new ArrayList<>();
        members.add(new Member(me.getId(), UserRole.ADMIN));
        members.add(new Member(buddy.getId(), UserRole.MEMBER));

        String title = me.getName() + Constants.GROUP_NAME_SEPARATOR + buddy.getName();
        return createChat(title, Chat.TYPE_ONE_TO_ONE_CHAT, members, null);
    }

    public void createChatGroup(Group group) {
        chatDatabaseRef.push().setValue(group.toMap());
    }

    public Chat createChatGroup(List<User> users) {

        User me = UserManager.getInstance().getCurrentUser();
        if (!users.contains(me)) {
            users.add(me);
        }

        // Create chat has two members me and other
        List<Member> members = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (User user : users) {
            if (user.equals(me)) {
                members.add(new Member(user.getId(), UserRole.ADMIN));
            } else {
                members.add(new Member(user.getId(), UserRole.MEMBER));
            }
            names.add(user.getName());
        }

        String title = buildChatTitle(names);
        Chat chat = createChat(title, Chat.TYPE_GROUP_CHAT, members, null);

        AppLog.d(TAG, "Create new chat: " + chat.getTitle());
        return chat;
    }

    /**
     * Search for existed chat group from the list of users
     *
     * @param users
     * @return
     */
    public void searchChatFromUsers(final List<User> users, final CallBack.OnResultReceivedListener callback) {

        if (users.size() < 2) {
            // not sure what you mean
            if (callback != null) {
                callback.onResultReceived(null);
            }
            return;
        }

        final int numberOfUsers = users.size();
        final BlockingQueue<String> chatIdsQueue = new LinkedBlockingQueue();
        final BlockingQueue<String> processedUser = new LinkedBlockingQueue(numberOfUsers);

        for (final User user : users) {

            UserManager.getInstance().getUserChatDatabaseRef(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> currentChatIds = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String chatId = data.getKey();
                        currentChatIds.add(chatId);
                    }

                    if (processedUser.size() == 0) {
                        chatIdsQueue.addAll(currentChatIds);
                    } else {
                        if (currentChatIds.size() > 0 && chatIdsQueue.size() > 0) {
                            for (String chatId : chatIdsQueue) {
                                if (!currentChatIds.contains(chatId)) {
                                    chatIdsQueue.remove(chatId);
                                }
                            }
                        } else {
                            chatIdsQueue.removeAll(chatIdsQueue);
                        }
                    }

                    processedUser.add(user.getId());

                    // we are done
                    if (processedUser.size() == numberOfUsers) {
                        if (chatIdsQueue.size() == 0) {
                            callback.onResultReceived(null);
                        } else {
                            try {
                                if (chatIdsQueue.size() > 1) {
                                    AppLog.e(TAG, "The group of users has more than one chat group");
                                }

                                final String existedChatId = chatIdsQueue.take();
                                // Get the actual members list of the chatId
                                MemberService.getInstance().getMembers(existedChatId, new CallBack.OnResultReceivedListener() {
                                    @Override
                                    public void onResultReceived(Object result) {
                                        List<Member> members = (List<Member>) result;

                                        if (members == null || members.size() != numberOfUsers) {
                                            callback.onResultReceived(null);
                                        } else {
                                            // Verify all processed users are real members
                                            boolean matched = true;
                                            for (Member member : members) {
                                                if (!processedUser.contains(member.getUserId())) {
                                                    matched = false;
                                                    break;
                                                }
                                            }
                                            if (!matched) {
                                                callback.onResultReceived(null);
                                            } else {
                                                // Found the correct chat
                                                ChatService.getInstance().getChatDatabaseRef(existedChatId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Chat chat = dataSnapshot.getValue(Chat.class);
                                                        chat.setId(dataSnapshot.getKey());
                                                        callback.onResultReceived(chat);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        callback.onResultReceived(null);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            } catch (InterruptedException e) {
                                AppLog.t(e);
                                callback.onResultReceived(null);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    public void updateChatTitle(String chatId, String title) {

    }

    public void updateLastMessage(String chatId, Message lastMessage) {

    }

    private String buildChatTitle(List<String> names) {
        return StringUtils.join(names, Constants.GROUP_NAME_SEPARATOR);
    }

    /**
     * Load chat history for current user
     * @param callBack
     */
    public void loadChatHistory(final CallBack.OnResultReceivedListener callBack) {

        User currentUser = UserManager.getInstance().getCurrentUser();
        if (currentUser == null) { // This should not never happened but need a debug checkpoint
            AppLog.e(TAG, "loadChatHistory currentUser is NULL");
            return;
        }

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // TODO optimize loading history (do some kind of pagination)
                if (chatHistory.size() > 0) {
                    chatHistory.removeAll(chatHistory);
                }

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Chat chat = data.getValue(Chat.class);
                    if (chat != null) {
                        chat.setId(data.getKey());
                        chatHistory.add(chat);
                    }
                }
                if (callBack != null) {
                    Collections.reverse(chatHistory); // Want to have the latest chats on top
                    callBack.onResultReceived(chatHistory);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        UserManager.getInstance().getCurrentUserChatDatabaseRef()
                .orderByChild("lastModifiedTimestamp")
                .limitToLast(Constants.CHAT_HISTORY_PAGINATION_SIZE_DEFAULT)
                .addValueEventListener(valueEventListener);
    }

    public void removeDatabaseRefListeners() {
        if (valueEventListener != null) {
            chatDatabaseRef.removeEventListener(valueEventListener);
        }
    }

    /**
     * Delete chat for of current user
     * @param chatId
     */
    public void deleteChat(String chatId) {

        User currentUser = UserManager.getInstance().getCurrentUser();
        String userId = currentUser.getId();

        // NOT actually remove the chat, just:
        // Remove the current user from the Members list, and
        // Remove the chat entry off the user chats list

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(Constants.FIREBASE_MEMBERS + "/" + chatId + "/" + userId,  null);
        childUpdates.put(Constants.FIREBASE_USERS + "/" + userId + "/chats/" + chatId, null);

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
    }

    /**
     * Delete chat for of current user
     * @param chat
     */
    public void addMemberChat(Chat chat, User user) {

        final String userId = user.getId();
        final Member member = new Member(userId, UserRole.MEMBER);

        // Add the user to the Members list, and
        // Add the chat entry on the user chats list

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(Constants.FIREBASE_MEMBERS + "/" + chat.getId() + "/" + userId,  member.toUserRoleMap());
        childUpdates.put(Constants.FIREBASE_USERS + "/" + userId + "/chats/" + chat.getId(), chat.toMap());

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
    }
}
