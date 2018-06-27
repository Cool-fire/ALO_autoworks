package com.aloautoworks.alo.models;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ServiceCenter {
    public String id;
    public String ServiceCentername;
    public String phoneNumber;
    public String emailAddress;
    public String GarageAddress;
    public String pincode;
    public String latitude;
    public String longitude;

    public ServiceCenter()
    {

    }


    public ServiceCenter(String id, String ServiceCentername, String phoneNumber, String emailAddress, String GarageAddress, String pincode, String latitude, String longitude) {
        this.ServiceCentername = ServiceCentername;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.GarageAddress = GarageAddress;
        this.pincode = pincode;
        this.latitude = latitude;

        this.id = id;
        this.longitude = longitude;
    }


}
