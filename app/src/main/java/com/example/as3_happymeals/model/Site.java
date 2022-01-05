package com.example.as3_happymeals.model;

public class Site {
    private String name;
    private double latitude;
    private double longitude;
    private String leaderUid;
    private String leaderName;
    private int totalPeople;
    private int totalComment;

    public Site() {
    }

    public Site(String name, double latitude, double longitude, String leaderUid, String leaderName, int totalPeople, int totalComment) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.leaderUid = leaderUid;
        this.leaderName = leaderName;
        this.totalPeople = totalPeople;
        this.totalComment = totalComment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLeaderUid() {
        return leaderUid;
    }

    public void setLeaderUid(String leaderUid) {
        this.leaderUid = leaderUid;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public int getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(int totalPeople) {
        this.totalPeople = totalPeople;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    @Override
    public String toString() {
        return "Site{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", leaderUid='" + leaderUid + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", totalPeople=" + totalPeople +
                ", totalComment=" + totalComment +
                '}';
    }
}
