package com.basikal.motosos;

public class Logs {
    String logId;
    String logDate;
    String logTime;
    String logLat;
    String logLong;
    String logGyroValue;
    String logAcceleroValue;
    String logAccidentStatus;
    String userId;

    public Logs() {

    }

    public Logs(String logId, String logDate, String logTime, String logLat, String logLong, String logGyroValue, String logAcceleroValue, String logAccidentStatus, String userId) {
        this.logId = logId;
        this.logDate = logDate;
        this.logTime = logTime;
        this.logLat = logLat;
        this.logLong = logLong;
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

    public String getLogLat() {
        return logLat;
    }

    public void setLogLat(String logLat) {
        this.logLat = logLat;
    }

    public String getLogLong() {
        return logLong;
    }

    public void setLogLong(String logLong) {
        this.logLong = logLong;
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
