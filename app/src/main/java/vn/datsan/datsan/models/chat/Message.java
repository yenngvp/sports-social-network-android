package vn.datsan.datsan.models.chat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

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
    private DateTime timestamp;
    private String userName;

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

    @Exclude
    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
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
        if (getTimestamp() != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(Constants.DATETIME_FORMAT);
            result.put("timestamp", formatter.print(getTimestamp()));
        }
        return result;
    }

}
