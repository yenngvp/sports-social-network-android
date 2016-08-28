package vn.datsan.datsan.models.chat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.utils.Constants;

public class Message extends AbstractMessage {

    private String message;
    private String userId;
    private Chat chat;
    private long timestamp;
    private String userName;

    public Message() {
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

    @Exclude
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userDisplayName) {
        this.userName = userDisplayName;
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
        result.put("userId", getUserId());
        result.put("userName", getUserName());
        result.put("message", getMessage());
        result.put("timestamp", getTimestamp());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        Message other = (Message) obj;
        if (other == null) {
            return false;
        }
        if (this.getId() != null && other.getId() != null && this.getId().equals(other.getId())) {
            return true;
        }
        return false;
    }
}
