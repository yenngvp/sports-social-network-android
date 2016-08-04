package vn.datsan.datsan.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.search.interfaces.Searchable;
import vn.datsan.datsan.utils.localization.VietnameseUnsignedTranslator;

/**
 * Created by xuanpham on 6/20/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Field implements Searchable, Parcelable {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String contactName;
    private String location;
    private String district;
    private String details;
    private String type;

    public Field() {

    }

    public Field(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        contactName = in.readString();
        location = in.readString();
        district = in.readString();
        details = in.readString();
        type = in.readString();
    }

    public static final Creator<Field> CREATOR = new Creator<Field>() {
        @Override
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        @Override
        public Field[] newArray(int size) {
            return new Field[size];
        }
    };

    /**
     * Get JSON string as put mapping for Searchable object
     * @return JSON String
     */
    @Exclude
    public static String getPutMapping() {
        return "{ \"" + Field.class.getSimpleName() + "\" : { \"properties\" : { \"location\" : {\"type\" : \"geo_point\"} } } }";
    }

    @Exclude
    @Override
    public String getDocumentId() {
        return getId();
    }

    @Exclude
    @Override
    public Map<String, String> getSearchableSource() {
        Map<String, String> source = new HashMap<>();
        source.put("name", getName());
        source.put("contactName", getContactName());
        source.put("phone", getPhone());
        source.put("address", getAddress());
        source.put("location", getLocation());
        // Localization attributes
        source.put("name_unsigned", VietnameseUnsignedTranslator.getInstance().getTranslation(getName()));
        source.put("contactName_unsigned", VietnameseUnsignedTranslator.getInstance().getTranslation(getContactName()));
        source.put("address_unsigned", VietnameseUnsignedTranslator.getInstance().getTranslation(getAddress()));

        return source;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Field (%s, %s, %s)", getName(), getAddress(), getPhone());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(contactName);
        parcel.writeString(location);
        parcel.writeString(district);
        parcel.writeString(details);
        parcel.writeString(type);
    }
}
