package vn.datsan.datsan.models.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.datsan.datsan.models.FirebaseObject;

/**
 * Created by yennguyen on 8/2/16.
 */
public class Member extends FirebaseObject {

    private Map<String, Boolean> users;

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }
}
