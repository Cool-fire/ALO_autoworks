package com.aloautoworks.alo.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserModel {

    public String Email = "none";
    public String Phone = "none";
    public String Name = "none";
    public String Userid = "none";


    public UserModel() {
    }

    public UserModel(String Userid,String Name,String Email,String Phone)
    {
        this.Userid = Userid;
        this.Name = Name;
        this.Email = Email;
        this.Phone = Phone;
    }


}
