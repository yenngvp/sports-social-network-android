package vn.datsan.datsan.models.chat;

import vn.datsan.datsan.models.FirebaseObject;
import vn.datsan.datsan.models.Group;

/**
 * Created by yennguyen on 8/2/16.
 */
public class Chat extends FirebaseObject {

    private String name;
    private Message lastMessage;

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
}
