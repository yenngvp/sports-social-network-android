package vn.datsan.datsan.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by yennguyen on 9/18/16.
 */
public class ServerTime implements Serializable {

    private long timestamp;
    private long todayAtMidnight;

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

    public Map<String, String> getTodayAtMidnight() {
        return ServerValue.TIMESTAMP;
    }

    @Exclude
    public long getTodayAtMidnightMillis() {
        return todayAtMidnight;
    }

    public void setTodayAtMidnight(long todayAtMidnight) {
        this.todayAtMidnight = todayAtMidnight;
    }
}
