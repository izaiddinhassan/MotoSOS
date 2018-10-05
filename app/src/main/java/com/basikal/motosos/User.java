package com.basikal.motosos;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    String uid;
    String name;
    String email;
    String phoneNo;
    String address;

    public User() {

    }

    public User(String uid, String name, String email, String phoneNo, String address) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAddress() {
        return address;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("phoneNo", phoneNo);
        result.put("address", address);
        return result;
    }

}
