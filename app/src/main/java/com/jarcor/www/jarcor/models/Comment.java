package com.jarcor.www.jarcor.models;

/**
 * Created by Trip on 8/26/2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties

public class Comment {

    public String user_id;
    public String name;
    public String text;



    public Comment() {

    }

    public Comment(String user_id, String name, String text) {
        this.user_id = user_id;
        this.name = name;
        this.text = text;



    }

}
