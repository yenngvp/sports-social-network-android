package vn.datsan.datsan.models;

/**
 * Created by xuanpham on 7/30/16.
 */
public class ImageModel {
    private String name, url;

    public ImageModel(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
