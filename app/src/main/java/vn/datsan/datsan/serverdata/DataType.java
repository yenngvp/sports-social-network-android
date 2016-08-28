package vn.datsan.datsan.serverdata;

/**
 * Created by xuanpham on 8/21/16.
 */
public enum DataType {
    IMAGE("images"),
    USER("users");

    private final String name;

    private DataType(String s) {
        name = s;
    }
    public String getName() {
        return name;
    }
    }
