package vn.datsan.datsan.models.chat;

import vn.datsan.datsan.models.FirebaseObject;

/**
 * Created by yennguyen on 7/31/16.
 */
public class AbstractMessage extends FirebaseObject {

    protected MessageType messageType;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
