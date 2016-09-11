package vn.datsan.datsan.models.chat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.utils.AppUtils;

public class Message {

    private String id;
    private  MessageType type;
    private String message;
    private String userId;
    private Chat chat;
    private long timestamp;
    private String userName;

    public Message() {
    }

    public Message(MessageType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public MessageType getTypeVal() {
        return type;
    }

    public void setTypeVal(MessageType type) {
        this.type = type;
    }

    public String getType() {
        if (this.type == null) {
            return null;
        } else {
            return this.type.name();
        }
    }

    public void setType(String typeString) {
        // Get enum from string
        if (typeString == null) {
            this.type = null;
        } else {
            this.type = MessageType.valueOf(typeString);
        }
    }

    @Exclude
    public boolean isMe() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser.getUid().equals(userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Exclude
    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, String> getTimestamp() {
        return ServerValue.TIMESTAMP;
    }

    @Exclude
    public long getTimestampMillis() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude
    public String getTimestampAsDatetime() {
        return AppUtils.getDateTimeAsString(this.timestamp, AppUtils.DATETIME_FORMATTER);
    }

    /**
     * Get description of the message to show in the chat history or notification,
     * which is telling [userName]: [just sending a message]
     *
     * @return
     */
    @Exclude
    public String toString() {
        return getUserName() + ": " + getMessage();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("type", getType());
        result.put("userId", getUserId());
        result.put("userName", getUserName());
        result.put("message", getMessage());
        result.put("timestamp", getTimestamp());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        Message that = (Message) obj;
        if (that == null || that.getId() == null || this.getId() == null) {
            return false;
        }
        return that.getId().equals(this.getId());
    }
}
