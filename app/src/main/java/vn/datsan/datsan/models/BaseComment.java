package vn.datsan.datsan.models;

/**
 * Created by xuanpham on 8/12/16.
 */
public class BaseComment {
    private String id;
    private String topicId;
    private String userId;
    private String message;

    public BaseComment() {
    }

    public BaseComment(String id, String topicId, String userId, String message) {
        this.id = id;
        this.topicId = topicId;
        this.userId = userId;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
