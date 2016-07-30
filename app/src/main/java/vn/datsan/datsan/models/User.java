package vn.datsan.datsan.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.search.interfaces.Searchable;
import vn.datsan.datsan.utils.localization.VietnameseUnsignedTranslator;

/**
 * Created by xuanpham on 6/13/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Searchable {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String groups;
    private String location;
    private String avatar;

    public User() {}

    public User(String name, String email, String phone, String address, String groups, String location) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.groups = groups;
        this.location = location;
    }

    /**
     * Get JSON string as put mapping for Searchable object
     * @return JSON String
     */
    public static String getPutMapping() {
        return "{ \"" + User.class.getSimpleName() + "\" : { \"properties\" : { \"location\" : {\"type\" : \"geo_point\", \"store\" : \"yes\"} } } }";
    }

    @Override
    public String getDocumentId() {
        return getId();
    }

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

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
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
}
