package com.example.finddog;

public class ModelComment {

    String comment, username;

    public ModelComment() {
    }

    public ModelComment(String comment, String username) {
        this.comment = comment;
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
