package vn.datsan.datsan.models;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import vn.datsan.datsan.search.interfaces.Searchable;
import vn.datsan.datsan.utils.localization.VietnameseUnsignedTranslator;

/**
 * Created by xuanpham on 6/20/16.
 */
public class Group extends FirebaseObject implements Searchable {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_CLUB = 1;
    public static final int TYPE_CHAT = 2;

    private int type;
    private HashMap<String, UserRole> members;
    private String name;
    private String city;
    private List<String> phones;
    private List<String> favouriteFields;

    public Group() {
        super();
    }

    public Group(String name, int type, HashMap<String, UserRole> members) {
        this.name = name;
        this.type = TYPE_DEFAULT;
        this.members = members;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public HashMap<String, UserRole> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, UserRole> members) {
        this.members = members;
    }

    public void addMember(String id, UserRole role) {
        if (members == null)
            members = new HashMap<>();
        members.put(id, role);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", getName());
        result.put("type", getType());
        result.put("members", getMembers());

        return result;
    }

    @Override
    public String toString() {
        return name + " : "+ city;
    }
}
