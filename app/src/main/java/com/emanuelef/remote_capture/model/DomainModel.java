package com.emanuelef.remote_capture.model;

import java.io.Serializable;

public class DomainModel implements Serializable {
    private String name;
    private String title;
    private String description;
    private String link;
    private String img;

    public DomainModel() {
    }

    public DomainModel(String name, String title, String description, String link, String img) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.link = link;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
