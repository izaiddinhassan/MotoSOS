package com.basikal.motosos;

public class Logs {
    String logId;
    String logDate;
    String logTime;
    String logLatLong;
    String logGyroValue;
    String logAcceleroValue;
    String logAccidentStatus;
    String userId;

    public Logs() {

    }

    public Logs(String logId, String logDate, String logTime, String logLatLong, String logGyroValue, String logAcceleroValue, String logAccidentStatus, String userId) {
        this.logId = logId;
        this.logDate = logDate;
        this.logTime = logTime;
        this.logLatLong = logLatLong;
        this.logGyroValue = logGyroValue;
        this.logAcceleroValue = logAcceleroValue;
        this.logAccidentStatus = logAccidentStatus;
        this.userId = userId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getLogLatLong() {
        return logLatLong;
    }

    public void setLogLatLong(String logLatLong) {
        this.logLatLong = logLatLong;
    }

    public String getLogGyroValue() {
        return logGyroValue;
    }

    public void setLogGyroValue(String logGyroValue) {
        this.logGyroValue = logGyroValue;
    }

    public String getLogAcceleroValue() {
        return logAcceleroValue;
    }

    public void setLogAcceleroValue(String logAcceleroValue) {
        this.logAcceleroValue = logAcceleroValue;
    }

    public String getLogAccidentStatus() {
        return logAccidentStatus;
    }

    public void setLogAccidentStatus(String logAccidentStatus) {
        this.logAccidentStatus = logAccidentStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
