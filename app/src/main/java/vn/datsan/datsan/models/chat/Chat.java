package vn.datsan.datsan.models.chat;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.models.FirebaseObject;

/**
 * Created by yennguyen on 8/2/16.
 */
public class Chat extends FirebaseObject {

    public static final String TYPE_CLUB_CHAT = "CUB_CHAT";
    public static final String TYPE_MATCH_CHAT = "MATCH_CHAT";
    public static final String TYPE_ONE_TO_ONE_CHAT = "ONE_TO_ONE_CHAT";
    public static final String TYPE_GROUP_CHAT = "GROUP_CHAT";

    private String title;
    private String type;
    private Message lastMessage;
    private String linkedGroup;

    public Chat() {

    }

    public Chat(String title, String type, String linkedGroup) {
        this.title = title;
        this.type = type;
        this.linkedGroup = linkedGroup;
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", getTitle());
        result.put("type", getType());
        result.put("linkedGroup", getLinkedGroup());
        result.put("lastMessage", getLastMessage());

        return result;
    }
}
