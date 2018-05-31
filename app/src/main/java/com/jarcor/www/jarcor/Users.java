package com.jarcor.www.jarcor;

/**
 * Created by Trip on 8/20/2017.
 */
public class Users {

    public String name;
    public String image;
    public String status;
    public String ppa_location;
    public String thumb_image;

    public Users(){

    }

    public Users(String name, String image, String status, String ppa_location, String thumb_image) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.ppa_location = ppa_location;
        this.thumb_image = thumb_image;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPpa_location() {
        return ppa_location;
    }

    public void setPpa_location(String ppa_location) {
        this.ppa_location = ppa_location;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

}

