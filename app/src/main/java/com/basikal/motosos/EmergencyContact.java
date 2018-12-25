package com.basikal.motosos;

public class EmergencyContact {
    String emcId;
    String emcName;
    String emcPhoneNo;
    String emcAddress;
    String userId;

    public EmergencyContact() {

    }

    public EmergencyContact(String emcId, String emcName, String emcPhoneNo, String emcAddress, String userId) {
        this.emcId = emcId;
        this.emcName = emcName;
        this.emcPhoneNo = emcPhoneNo;
        this.emcAddress = emcAddress;
        this.userId = userId;
    }

    public String getEmcId() {

        return emcId;
    }

    public void setEmcId(String emcId) {
        this.emcId = emcId;
    }

    public String getEmcName() {
        return emcName;
    }

    public void setEmcName(String emcName) {
        this.emcName = emcName;
    }

    public String getEmcPhoneNo() {
        return emcPhoneNo;
    }

    public void setEmcPhoneNo(String emcPhoneNo) {
        this.emcPhoneNo = emcPhoneNo;
    }

    public String getEmcAddress() {
        return emcAddress;
    }

    public void setEmcAddress(String emcAddress) {
        this.emcAddress = emcAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
