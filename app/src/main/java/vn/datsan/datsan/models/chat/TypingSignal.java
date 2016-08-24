package vn.datsan.datsan.models.chat;

import com.google.firebase.database.Exclude;

/**
 * Created by yennguyen on 7/31/16.
 */
public class TypingSignal {

    private long timestamp;
    private boolean stopped;
    private String userId;
    private String userName;

    public TypingSignal() {}

    public TypingSignal(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude
    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
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
}
