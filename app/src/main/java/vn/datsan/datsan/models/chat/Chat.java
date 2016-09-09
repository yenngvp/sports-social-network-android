package vn.datsan.datsan.models.chat;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.datsan.datsan.models.User;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 8/2/16.
 */
public class Chat implements Parcelable {

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
    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
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

        User currentUser = UserManager.getInstance().getCurrentUser();

        if (TextUtils.isEmpty(getTitle()) || TextUtils.isEmpty(currentUser.getName())) {
            return getTitle();
        }

        String dynamicTitle = getTitle().replaceFirst(currentUser.getName(), "");
        if (TextUtils.isEmpty(dynamicTitle)) {
            return "";
        }

        // The removed string leaves behind the dirty separator characters,
        // do remove them now to get clean string
        String dirtySeparator = Constants.GROUP_NAME_SEPARATOR;

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
}
