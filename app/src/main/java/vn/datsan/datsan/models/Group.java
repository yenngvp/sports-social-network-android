package vn.datsan.datsan.models;

import java.util.Date;
import java.util.List;

import vn.datsan.datsan.utils.Utils;

/**
 * Created by xuanpham on 6/20/16.
 */
public class Group {
    private String id;
    private int type;
    private String createDate;
    private List<String> members;
    private String name;
    private String city;
    private List<String> admins;
    private List<String> favouriteFields;

    public Group(List<String> admins, String city, String name, List<String> members) {
        this.admins = admins;
        this.city = city;
        this.name = name;
        this.members = members;
        this.createDate = Utils.SIMPLE_DATE_FORMAT.format(new Date());
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getFavouriteFields() {
        return favouriteFields;
    }

    public void setFavouriteFields(List<String> favouriteFields) {
        this.favouriteFields = favouriteFields;
    }
}
