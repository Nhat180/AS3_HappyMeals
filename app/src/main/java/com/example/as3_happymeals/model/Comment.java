package com.example.as3_happymeals.model;

public class Comment {
    private String id;
    private String name;
    private String textCom;

    public Comment(){

    }

    public Comment(String id, String name, String textCom) {
        this.id = id;
        this.name = name;
        this.textCom = textCom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextCom() {
        return textCom;
    }

    public void setTextCom(String textCom) {
        this.textCom = textCom;
    }
}