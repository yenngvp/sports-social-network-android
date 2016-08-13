package vn.datsan.datsan.models;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by xuanpham on 8/12/16.
 */
public class FriendlyMatch {
    private String id;
    private String creatorId;
    private String creatorName;
    private String homeGroupId;
    private String homeGroupName;
    private String opponentGroup;
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

    public String getOpponentGroup() {
        return opponentGroup;
    }

    public void setOpponentGroup(String opponentGroup) {
        this.opponentGroup = opponentGroup;
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
}
