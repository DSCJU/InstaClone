package com.sushilmaurya.instaclone;

public class Post {
    private String title, description, image;

    public Post() {
    }

    public Post(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return title+" "+description+" "+ image;
    }
}
