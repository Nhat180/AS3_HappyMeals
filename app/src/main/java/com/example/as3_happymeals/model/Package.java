package com.example.as3_happymeals.model;


public class Package {

    private String packageName;
    private String quantity;
    private String description;

    public Package(String packageName, String quantity, String description) {
        this.packageName = packageName;
        this.quantity = quantity;
        this.description = description;
    }

    public Package(){

    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}