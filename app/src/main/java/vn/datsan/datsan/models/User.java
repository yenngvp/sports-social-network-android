package vn.datsan.datsan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.search.interfaces.Searchable;
import vn.datsan.datsan.utils.localization.VietnameseUnsignedTranslator;

/**
 * Created by xuanpham on 6/13/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Searchable {

    public static final int ROLE_MEMBER = 0;
    public static final int ROLE_GROUP_ADMIN = 1;
    public static final int ROLE_FIELD_OWNER = 2;

    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Group groups;
    private String location;
    private String avatar;
    private int role;

    public User() {}

    public User(String name, String email, String phone, String address, Group groups, String location) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.groups = groups;
        this.location = location;
        this.role = ROLE_MEMBER;
    }

    /**
     * Get JSON string as put mapping for Searchable object
     * @return JSON String
     */
    @Exclude
    public static String getPutMapping() {
        return "{ \"" + User.class.getSimpleName() + "\" : { \"properties\" : { \"location\" : {\"type\" : \"geo_point\"} } } }";
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
        source.put("email", getEmail());
        source.put("phone", getPhone());
        source.put("address", getAddress());
        source.put("location", getLocation());
        // Localization attributes
        source.put("name_unsigned", VietnameseUnsignedTranslator.getInstance().getTranslation(getName()));
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Group getGroups() {
        return groups;
    }

    public void setGroups(Group groups) {
        this.groups = groups;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
