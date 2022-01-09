package com.example.as3_happymeals.model;

public class Comment {
    private int id;
    private String name;
    private String textCom;

    public Comment(){

    }

    public Comment(int id, String name, String textCom) {
        this.id = id;
        this.name = name;
        this.textCom = textCom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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