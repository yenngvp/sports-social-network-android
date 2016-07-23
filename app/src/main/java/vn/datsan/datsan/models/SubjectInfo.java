package vn.datsan.datsan.models;

/**
 * Created by xuanpham on 7/22/16.
 */
public class SubjectInfo {
    private String subject;
    private String info;
    private DataType type;

    public SubjectInfo(String subject, String info, DataType type) {
        this.subject = subject;
        this.info = info;
        this.type = type;
    }

    public SubjectInfo(String subject, String info) {
        this.subject = subject;
        this.info = info;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public enum DataType {
        TEXT,
        DATE_TIME
    }
}
