package vn.datsan.datsan.models.chat;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.datsan.datsan.models.User;
import vn.datsan.datsan.serverdata.UserService;
import vn.datsan.datsan.ui.adapters.FlexListAdapter;
import vn.datsan.datsan.utils.AppConstants;
import vn.datsan.datsan.utils.AppUtils;

/**
 * Created by yennguyen on 8/2/16.
 */
public class Chat implements Parcelable, FlexListAdapter.ViewItem {

    public static final String TYPE_CLUB_CHAT = "CUB_CHAT";
    public static final String TYPE_MATCH_CHAT = "MATCH_CHAT";
    public static final String TYPE_ONE_TO_ONE_CHAT = "ONE_TO_ONE_CHAT";
    public static final String TYPE_GROUP_CHAT = "GROUP_CHAT";

    private String id;
    private long createdTimestamp;
    private long lastModifiedTimestamp;
    private String createdBy;
    private String lastModifiedBy;
    private String title;
    private String type;
    private Message lastMessage;
    private String linkedGroup;
    private List<Member> members;
    private int messageCount;
    private int unreadMessageCount;
    private boolean removed;

    public Chat() {

    }

    protected Chat(Parcel in) {
        id = in.readString();
        title = in.readString();
        type = in.readString();
        linkedGroup = in.readString();
        messageCount = in.readInt();
//        lastMessage = in.readTypedObject();

    }

    public Chat(String title, String type, String linkedGroup) {
        this.title = title;
        this.type = type;
        this.linkedGroup = linkedGroup;
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    @Exclude
    @Override
    public String getItemId() {
        return getId();
    }

    /**
     * Get chat group avatar url
     *
     * @return
     */
    @Exclude
    @Override
    public String getImageUrl() {
        return null;
    }

    /**
     * Get chat title
     *
     * @return
     */
    @Override
    @Exclude
    public String getRowTitle() {
        return getDynamicChatTitle();
    }

    /**
     * Get last chat message short description
     *
     * @return
     */
    @Override
    @Exclude
    public String getRowContent() {
        final boolean oneToOneChat;
        if (getTitle() != null && getTitle().indexOf(AppConstants.GROUP_NAME_SEPARATOR) > 0) {
            oneToOneChat = false; // The chat has more than 2 members
        } else {
            oneToOneChat = true;
        }
        String content = null;
        if (getLastMessage() != null) {
            content = getLastMessage().getMessage();

            // Not show the last sender name if there is an one-to-one chat
            if (!oneToOneChat && getLastMessage().getUserName() != null) {
                String senderName = getLastMessage().getUserName();
                if (senderName.length() > 18) {
                    senderName = senderName.substring(0, 15) + "...";
                }
                content = senderName + ": " + content;
            }
        }

        return content;
    }

    /**
     * Get last message sent date
     *
     * @return
     */
    @Override
    @Exclude
    public String getRowNote() {

        return AppUtils.getDateTimeForMessageSent(getLastModifiedTimestampMillis());
    }

    /**
     * Get number of unread message of the chat, highlighted as badge icon
     *
     * @return
     */
    @Override
    @Exclude
    public String getRowBadge() {
        return getUnreadMessageCount() > 0 ? String.valueOf(getUnreadMessageCount()) : null;
    }

    /**
     * Get sorting value of the chat as sent time milliseconds
     *
     * @return
     */
    @Override
    @Exclude
    public long getSortingValue() {
        return getLastModifiedTimestampMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getCreatedTimestamp() {
        return ServerValue.TIMESTAMP;
    }

    @Exclude
    public long getCreatedTimestampMillis() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long timestamp) {
        this.createdTimestamp = timestamp;
    }

    public Map<String, String> getLastModifiedTimestamp() {
        return ServerValue.TIMESTAMP;
    }

    @Exclude
    public long getLastModifiedTimestampMillis() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(long timestamp) {
        this.lastModifiedTimestamp = timestamp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLinkedGroup() {
        return linkedGroup;
    }

    public void setLinkedGroup(String linkedGroup) {
        this.linkedGroup = linkedGroup;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    @Exclude
    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Exclude
    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    @Exclude
    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", getTitle());
        result.put("type", getType());
        if (getCreatedTimestampMillis() == 0) {
            result.put("createdTimestamp", getCreatedTimestamp());
        } else {
            result.put("createdTimestamp", getCreatedTimestampMillis());
        }
        result.put("lastModifiedTimestamp", getLastModifiedTimestamp());
        result.put("linkedGroup", getLinkedGroup());
        if (getLastMessage() != null) {
            result.put("lastMessage", getLastMessage().toMap());
        }
        result.put("messageCount", getMessageCount());

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getId());
        parcel.writeString(getTitle());
        parcel.writeString(getType());
        parcel.writeString(getLinkedGroup());
        parcel.writeInt(getMessageCount());
//        parcel.writeString(getLastMessage());
    }

    @Exclude
    public String getDynamicChatTitle() {

        if (getType() == null) {
            return removeMyName();
        }

        String dynamicTitle = "";
        switch (getType()) {
            case TYPE_ONE_TO_ONE_CHAT:
                // The other member's name
                dynamicTitle = removeMyName();
                break;
            case TYPE_CLUB_CHAT:
                // Chat title is defaulted to group name
                break;
            case TYPE_GROUP_CHAT:
                // The other members' name
                dynamicTitle = removeMyName();
                break;
            case TYPE_MATCH_CHAT:
                // The other members' name
                dynamicTitle = removeMyName();
                break;
            default:
                break;
        }

        return dynamicTitle;
    }

    /**
     * Concatenate member names who are not the current user
     */
    private String concatMemberNames() {
        if (members == null || members.size() == 0) return "";

        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StringBuilder builder = new StringBuilder();
        for (Member member : members) {
            if (!member.getUserId().equals(currentUid)) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(member.getUserId());
            }
        }

        return builder.toString();
    }

    /**
     * Trim out the current user name for the chat title
     */
    private String removeMyName() {

        User currentUser = UserService.getInstance().getCurrentUser();

        if (TextUtils.isEmpty(getTitle()) || TextUtils.isEmpty(currentUser.getName())) {
            return getTitle();
        }

        String dynamicTitle = getTitle().replaceFirst(currentUser.getName(), "");
        if (TextUtils.isEmpty(dynamicTitle)) {
            return "";
        }

        // The removed string leaves behind the dirty separator characters,
        // do remove them now to get clean string
        String dirtySeparator = AppConstants.GROUP_NAME_SEPARATOR;

        if (dynamicTitle.indexOf(dirtySeparator) == 0) {
            dynamicTitle = dynamicTitle.substring(1);
        } else if (dynamicTitle.indexOf(dirtySeparator) == dynamicTitle.length() - dirtySeparator.length()) {
            dynamicTitle = dynamicTitle.substring(0, dynamicTitle.length() - dirtySeparator.length());
        } else {
            String doubleDirtySeparator = dirtySeparator + dirtySeparator;
            dynamicTitle = dynamicTitle.replaceAll(doubleDirtySeparator, dirtySeparator);
        }

        return dynamicTitle.trim();
    }

    @Override
    public boolean equals(Object obj) {
        Chat that = (Chat) obj;
        if (that == null || that.getId() == null || this.getId() == null) {
            return false;
        }
        return that.getId().equals(this.getId());
    }
}
