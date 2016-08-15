package vn.datsan.datsan.models;

/**
 * Created by xuanpham on 8/14/16.
 */
public class BaseDto {
    private String id;
    private String name;

    public BaseDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
