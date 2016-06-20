package vn.datsan.datsan.models;

import android.location.Location;

import java.util.Date;

/**
 * Created by xuanpham on 6/13/16.
 */
public class User {
    private String name;
    private String email;
    private String phone;

    public User() {

    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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
}
