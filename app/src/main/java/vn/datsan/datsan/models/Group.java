package vn.datsan.datsan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import vn.datsan.datsan.search.interfaces.Searchable;
import vn.datsan.datsan.utils.Utils;
import vn.datsan.datsan.utils.localization.VietnameseUnsignedTranslator;

/**
 * Created by xuanpham on 6/20/16.
 */
public class Group extends FirebaseObject implements Searchable {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_CLUB = 1;
    public static final int TYPE_CHAT = 2;

    private int type;
    private List<String> members;
    private String name;
    private String city;
    private List<String> admins;
    private List<String> favouriteFields;

    public Group() {
        super();
    }

    public Group(List<String> admins, String city, String name, List<String> members) {
        this.admins = admins;
        this.name = name;
        this.city = city;
        this.members = members;
        this.type = TYPE_DEFAULT;
    }

    /**
     * Get JSON string as put mapping for Searchable object
     * @return JSON String
     */
    @Exclude
    public static String getPutMapping() {
        return null;
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
        // Localization attributes
        source.put("name_unsigned", VietnameseUnsignedTranslator.getInstance().getTranslation(getName()));

        return source;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
