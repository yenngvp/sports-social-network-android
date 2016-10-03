package vn.datsan.datsan.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import vn.datsan.datsan.chat.models.Member;
import vn.datsan.datsan.search.interfaces.Searchable;
import vn.datsan.datsan.ui.adapters.FlexListAdapter;
import vn.datsan.datsan.utils.localization.VietnameseUnsignedTranslator;

/**
 * Created by xuanpham on 6/20/16.
 */
public class Group implements Searchable, Parcelable, FlexListAdapter.ViewItem {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_CLUB = 1;
    public static final int TYPE_CHAT = 2;

    private String id;
    private int type;
    private List<Member> members;
    private String name;
    private String city;
    private List<String> phones;
    private List<String> favouriteFields;
    private String logoUrl;

    public Group() {
        super();
    }

    public Group(String name, int type, List<Member>members) {
        this.name = name;
        this.type = TYPE_DEFAULT;
        this.members = members;
    }

    protected Group(Parcel in) {
        id = in.readString();
        type = in.readInt();
        name = in.readString();
        city = in.readString();
        logoUrl = in.readString();
        phones = in.createStringArrayList();
        favouriteFields = in.createStringArrayList();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

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

    @Override
    @Exclude
    public String getItemId() {
        return getId();
    }

    @Override
    @Exclude
    public String getImageUrl() {
        return getLogoUrl();
    }

    @Override
    @Exclude
    public String getRowTitle() {
        return getName();
    }

    @Override
    @Exclude
    public String getRowContent() {
        return getCity();
    }

    @Override
    @Exclude
    public String getRowNote() {
        return null;
    }

    @Override
    @Exclude
    public String getRowBadge() {
        return null;
    }

    @Override
    @Exclude
    public long getSortingValue() {
        return 0;
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

    public HashMap<String, String> getMembers() {

        HashMap<String, String> membersMap = new HashMap<>();
        for (Member member : members) {
            membersMap.putAll(member.toUserRoleMap());
        }
        return membersMap;
    }

    public void setMembers(HashMap<String, String> members) {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        for (Map.Entry<String, String> entry : members.entrySet()) {
            String userId = entry.getKey();
            String userRole = entry.getValue();
            this.members.add(new Member(userId, userRole));
        }
    }

    public List<Member> getMembersList() {
        return members;
    }

    public void setMembersList(List<Member> members) {
        this.members = members;
    }

    public void addMember(String id, UserRole role) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(new Member(id, role));
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeInt(type);
        parcel.writeString(name);
        parcel.writeString(city);
        parcel.writeString(logoUrl);
        parcel.writeStringList(phones);
        parcel.writeStringList(favouriteFields);
    }
}
