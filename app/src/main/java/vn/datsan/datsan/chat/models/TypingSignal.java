package vn.datsan.datsan.chat.models;

import com.google.firebase.database.Exclude;

/**
 * Created by yennguyen on 7/31/16.
 */
public class TypingSignal {

    private long timestamp;
    private boolean typing;
    private String userId;
    private String userName;

    public TypingSignal() {}

    public TypingSignal(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Exclude
    @Override
    public String toString() {
        return String.format("%s (%s) started typing from %d ...", userName, userId, timestamp);
    }

    @Exclude
    @Override
    public boolean equals(Object object) {
        TypingSignal that = (TypingSignal) object;
        if (that == null || that.getUserId() == null || this.getUserId() == null) {
            return false;
        }
        return this.getUserId().equals(that.getUserId());
    }
}
