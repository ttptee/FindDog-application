package com.example.finddog;

public class DetailMissing {
    public String name;
    public String breed;
    public String special;
    public String datetime;
    public String prize;
    public String uid;

    public DetailMissing(String uid, String name, String breed, String special, String datetime, String prize){
        this.uid = uid;
        this.name = name;
        this.breed = breed;
        this.special = special;
        this.datetime = datetime;
        this.prize = prize;

    }
}
