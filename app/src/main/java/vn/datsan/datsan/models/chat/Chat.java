package vn.datsan.datsan.models.chat;

import org.joda.time.DateTime;

import vn.datsan.datsan.models.FirebaseObject;

/**
 * Created by yennguyen on 8/2/16.
 */
public class Chat extends FirebaseObject {

    public static final String TYPE_CLUB_CHAT = "CUB_CHAT";
    public static final String TYPE_MATCH_CHAT = "MATCH_CHAT";
    public static final String TYPE_ONE_TO_ONE_CHAT = "ONE_TO_ONE_CHAT";
    public static final String TYPE_GROUP_CHAT = "GROUP_CHAT";

    private String name;
    private Message lastMessage;
    private DateTime timestamp;

    public Chat() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }
}
