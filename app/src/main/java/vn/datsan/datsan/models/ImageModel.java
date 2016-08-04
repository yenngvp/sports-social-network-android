package vn.datsan.datsan.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xuanpham on 7/30/16.
 */
public class ImageModel implements Parcelable {
    private String name, url;

    public ImageModel(String url, String name) {
        this.url = url;
        this.name = name;
    }

    protected ImageModel(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(url);
    }
}
