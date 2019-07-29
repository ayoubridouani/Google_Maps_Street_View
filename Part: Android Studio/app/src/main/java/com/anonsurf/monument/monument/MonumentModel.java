package com.anonsurf.monument.monument;

import android.graphics.Bitmap;

public class MonumentModel {
    private int id;
    private String title;
    private String image;
    private Bitmap bmp;
    private String about;
    private String description;
    private String map;

    public MonumentModel() {

    }

    public MonumentModel(int id, String title, String image, String about, String description, String map) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.about = about;
        this.description = description;
        this.map = map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
