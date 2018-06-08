package com.aloautoworks.alo.models;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class vehicle  {
       public String uid;
        public String vehicleType;
        public String manufacturerName;
        public String modelName;
        public String fuel;

        public vehicle()
        {

        }

        public vehicle(String uid, String vehicleType, String manufacturerName, String modelName, String fuel) {
            this.uid = uid;
            this.vehicleType = vehicleType;
            this.manufacturerName = manufacturerName;
            this.modelName = modelName;
            this.fuel = fuel;
        }

    public String getUid() {
        return uid;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getModelName() {
        return modelName;
    }

    public String getFuel() {
        return fuel;
    }
}
