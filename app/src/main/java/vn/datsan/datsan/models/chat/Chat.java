package vn.datsan.datsan.models.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.models.FirebaseObject;
import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 8/2/16.
 */
public class Chat extends FirebaseObject implements Parcelable {

    public static final String TYPE_CLUB_CHAT = "CUB_CHAT";
    public static final String TYPE_MATCH_CHAT = "MATCH_CHAT";
    public static final String TYPE_ONE_TO_ONE_CHAT = "ONE_TO_ONE_CHAT";
    public static final String TYPE_GROUP_CHAT = "GROUP_CHAT";

    private String title;
    private String type;
    private String lastMessage;
    private DateTime timestamp;
    private String linkedGroup;

    public Chat() {

    }

    protected Chat(Parcel in) {
        id = in.readString();
        title = in.readString();
        type = in.readString();
        linkedGroup = in.readString();
        lastMessage = in.readString();

    }

    public Chat(String title, String type, String linkedGroup) {
        this.title = title;
        this.type = type;
        this.linkedGroup = linkedGroup;
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Exclude
    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
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
        if (getTimestamp() != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(Constants.DATATIME_FORMAT);
            result.put("timestamp", formatter.print(getTimestamp()));
        }

        return result;
    }

    @Exclude
    public Map<String, Object> toSimpleMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", getTitle());
        result.put("lastMessage", getLastMessage());

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getId());
        parcel.writeString(getTitle());
        parcel.writeString(getType());
        parcel.writeString(getLinkedGroup());
        parcel.writeString(getLastMessage());
    }
}
