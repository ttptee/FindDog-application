package com.example.finddog;

import java.util.StringTokenizer;

public class MissingBlog {
    private String name;
    private String breed;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MissingBlog(){

    }

    public MissingBlog(String name, String breed, String image) {
        this.name = name;
        this.breed = breed;
        this.image = image;
    }


}
