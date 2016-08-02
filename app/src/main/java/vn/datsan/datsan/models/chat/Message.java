package vn.datsan.datsan.models.chat;

import java.util.List;

/**
 * Created by yennguyen on 7/31/16.
 */
public class Message extends AbstractMessage {

    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
