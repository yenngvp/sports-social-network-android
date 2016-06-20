package vn.datsan.datsan.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xuanpham on 6/20/16.
 */
public class FakeStadium {
    @SerializedName("FIELD1")
    private String district;

    @SerializedName("FIELD2")
    private String name;

    @SerializedName("FIELD3")
    private String address;

    @SerializedName("FIELD4")
    private String location;

    @SerializedName("FIELD5")
    private String phone;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
