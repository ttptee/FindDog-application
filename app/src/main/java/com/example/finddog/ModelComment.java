package com.example.finddog;

public class ModelComment {

    String comment, username,image;

    public ModelComment() {
    }

    public ModelComment(String comment, String username, String image) {
        this.comment = comment;
        this.username = username;
        this.image = image;
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

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }
}
