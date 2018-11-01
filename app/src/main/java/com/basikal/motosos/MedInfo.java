package com.basikal.motosos;

public class MedInfo {
    String medId;
    String medType;
    String medName;
    String medExtra;
    String userId;

    public MedInfo() {

    }

    public MedInfo(String medId, String medType, String medName, String medExtra, String userId) {
        this.medId = medId;
        this.medType = medType;
        this.medName = medName;
        this.medExtra = medExtra;
        this.userId = userId;
    }

    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedExtra() {
        return medExtra;
    }

    public void setMedExtra(String medExtra) {
        this.medExtra = medExtra;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
