package vn.datsan.datsan.models.chat;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.models.FirebaseObject;
import vn.datsan.datsan.models.UserRole;

/**
 * Created by yennguyen on 8/2/16.
 */
public class Member extends FirebaseObject {

    private String chatId;
    private String user;
    private UserRole role;

    public Member() {};

    public Member(String user, UserRole role) {
        this.user = user;
        this.role = role;
    }

    @Exclude
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Exclude
    public UserRole getRoleVal() {
        return role;
    }

    public String getRole() {
        // Convert enum to string
        if (role == null) {
            return null;
        } else {
            return role.name();
        }
    }

    public void setRole(String roleString) {
        // Get enum from string
        if (roleString == null) {
            this.role = null;
        } else {
            this.role = UserRole.valueOf(roleString);
        }
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user", getUser());
        result.put("role", getRole());

        return result;
    }

    @Exclude
    public Map<String, Object> toUserRoleMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(getUser(), getRole());

        return result;
    }
}
