package vn.datsan.datsan.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.datsan.datsan.search.interfaces.Searchable;
import vn.datsan.datsan.ui.adapters.FlexListAdapter;
import vn.datsan.datsan.utils.AppUtils;
import vn.datsan.datsan.utils.localization.VietnameseUnsignedTranslator;

/**
 * Created by xuanpham on 8/12/16.
 */
public class FriendlyMatch implements Searchable, Parcelable, FlexListAdapter.ViewItem {
    private String id;
    private String creatorId;
    private String creatorName;
    private String homeGroupId;
    private String homeGroupName;
    private String opponentGroupId;
    private String opponentGroupName;
    private String fields;
    private long startTime;
    private long endTime;
    private String title;
    private String description;
    private String score;
    private List<BaseComment> comments;

    public FriendlyMatch() {

    }

    protected FriendlyMatch(Parcel in) {
        creatorId = in.readString();
        creatorName = in.readString();
        homeGroupId = in.readString();
        homeGroupName = in.readString();
        opponentGroupId = in.readString();
        opponentGroupName = in.readString();
        fields = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        title = in.readString();
        description = in.readString();
        score = in.readString();
    }

    public static final Creator<FriendlyMatch> CREATOR = new Creator<FriendlyMatch>() {
        @Override
        public FriendlyMatch createFromParcel(Parcel in) {
            return new FriendlyMatch(in);
        }

        @Override
        public FriendlyMatch[] newArray(int size) {
            return new FriendlyMatch[size];
        }
    };

    @Override
    @Exclude
    public String getItemId() {
        return getId();
    }

    @Override
    @Exclude
    public String getImageUrl() {
        return null;
    }

    @Override
    @Exclude
    public String getRowTitle() {
        return getTitle();
    }

    @Override
    @Exclude
    public String getRowContent() {

        DateTime startTime = new DateTime(getStartTime());
        DateTime endTime = new DateTime(getEndTime());
        String dayWeek = AppUtils.getWeekDayAsText(startTime);
        String dayMonth = AppUtils.getMonthDayAsText(startTime);
        // TODO: Shouldn't do it manually. Use Joda Duration/Period to calculate the time period
        String timeRange = "Thời gian " + startTime.getHourOfDay() + "h:" + startTime.getMinuteOfHour() + " - " +
                endTime.getHourOfDay() + "h:" + endTime.getMinuteOfHour();
        String field = "\nSân : ";
        if (getFields() == null || getFields().isEmpty()) {
            field += "Thoả thuận sau";
        } else {
            field += getFields();
        }
        String content = dayWeek + "," + dayMonth + "\n" + timeRange + field;
        return content;
    }

    @Override
    @Exclude
    public String getRowNote() {
        return null;
    }

    @Override
    @Exclude
    public String getRowBadge() {
        return null;
    }

    @Override
    @Exclude
    public long getSortingValue() {
        return 0;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getHomeGroupId() {
        return homeGroupId;
    }

    public void setHomeGroupId(String homeGroupId) {
        this.homeGroupId = homeGroupId;
    }

    public String getHomeGroupName() {
        return homeGroupName;
    }

    public void setHomeGroupName(String homeGroupName) {
        this.homeGroupName = homeGroupName;
    }

    public String getOpponentGroupId() {
        return opponentGroupId;
    }

    public void setOpponentGroupId(String opponentGroupId) {
        this.opponentGroupId = opponentGroupId;
    }

    public String getOpponentGroupName() {
        return opponentGroupName;
    }

    public void setOpponentGroupName(String opponentGroupName) {
        this.opponentGroupName = opponentGroupName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<BaseComment> getComments() {
        return comments;
    }

    public void setComments(List<BaseComment> comments) {
        this.comments = comments;
    }

    @Override
    public String getDocumentId() {
        return getId();
    }

    @Override
    public Map<String, String> getSearchableSource() {
        Map<String, String> source = new HashMap<>();
        source.put("title_unsigned", getTitle());
        source.put("creatorName", getCreatorName());
        source.put("homeGroupName", getHomeGroupName());
        source.put("fields", getFields());

        source.put("title_unsigned", VietnameseUnsignedTranslator.getInstance().getTranslation(getTitle()));
        source.put("creatorName_unsigned", VietnameseUnsignedTranslator.getInstance().getTranslation(getCreatorName()));
        source.put("homeGroupName_unsigned", VietnameseUnsignedTranslator.getInstance().getTranslation(getHomeGroupName()));
        source.put("fields_unsigned", VietnameseUnsignedTranslator.getInstance().getTranslation(getFields()));
        return source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(creatorId);
        parcel.writeString(creatorName);
        parcel.writeString(homeGroupId);
        parcel.writeString(homeGroupName);
        parcel.writeString(opponentGroupId);
        parcel.writeString(opponentGroupName);
        parcel.writeString(fields);
        parcel.writeLong(startTime);
        parcel.writeLong(endTime);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(score);
    }
}
