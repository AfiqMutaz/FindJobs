package com.example.findjobs;

public class Jobs {

    private String docId;
    private String userId;
    private String serviceType;
    private String dateTime;
    private String dateTimeAlt;
    private String duration;
    private String numCleaner;
    private String totalPrice;
    private boolean isAccepted;
    private boolean isSupplied;

    public Jobs() {

    }

    public Jobs(String docId,
                String userId,
                String serviceType,
                String dateTime,
                String dateTimeAlt,
                String duration,
                String numCleaner,
                String totalPrice,
                boolean isAccepted,
                boolean isSupplied) {
        this.userId = userId;
        this.serviceType = serviceType;
        this.dateTime = dateTime;
        this.dateTimeAlt = dateTimeAlt;
        this.duration = duration;
        this.numCleaner = numCleaner;
        this.totalPrice = totalPrice;
        this.isAccepted = isAccepted;
        this.isSupplied = isSupplied;
    }

    //getter methods
    public String getDocId() {
        return docId;
    }

    public String getUserId() {
        return userId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDateTimeAlt() {
        return dateTimeAlt;
    }

    public String getDuration() {
        return duration;
    }

    public String getNumCleaner() {
        return numCleaner;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public boolean getIsSupplied() {
        return isSupplied;
    }

    //setter methods
    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setDateTimeAlt(String dateTimeAlt) {
        this.dateTimeAlt = dateTimeAlt;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setNumCleaner(String numCleaner) {
        this.numCleaner = numCleaner;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public void setIsSupplied(boolean isSupplied) {
        this.isSupplied = isSupplied;
    }
}
