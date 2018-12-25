package com.basikal.motosos;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    String uid;
    String name;
    String email;
    String icNo;
    String phoneNo;
    String address;
    String dob;
    String gender;
    String bloodType;
    String insurancePolicy;
    String insurancePhone;

    public User() {

    }

    public User(String uid, String name, String email, String icNo, String phoneNo, String address, String dob, String gender, String bloodType, String insurancePolicy, String insurancePhone) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.icNo = icNo;
        this.phoneNo = phoneNo;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
        this.bloodType = bloodType;
        this.insurancePolicy = insurancePolicy;
        this.insurancePhone = insurancePhone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(String insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    public String getIcNo() {
        return icNo;
    }

    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    public String getInsurancePhone() {
        return insurancePhone;
    }

    public void setInsurancePhone(String insurancePhone) {
        this.insurancePhone = insurancePhone;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("phoneNo", phoneNo);
        result.put("dob", dob);
        result.put("gender", gender);
        result.put("bloodType", bloodType);
        result.put("insurancePolicy", insurancePolicy);
        result.put("insurancePhone", insurancePhone);
        result.put("address", address);
        return result;
    }

}
