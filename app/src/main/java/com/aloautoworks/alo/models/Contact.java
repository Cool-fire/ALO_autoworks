package com.aloautoworks.alo.models;

import android.media.MediaExtractor;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Contact {

    public String Email = "none";
    public String Phone = "none";
    public String Name = "none";
    public String Userid = "none";
    public String Message = "none";
    public String Id = "none";


    public Contact() {
    }

    public Contact(String Userid,String Name,String Email,String Phone,String Message,String Id)
    {
        this.Userid = Userid;
        this.Name = Name;
        this.Email = Email;
        this.Phone = Phone;
        this.Message = Message;
        this.Id = Id;
    }


}
