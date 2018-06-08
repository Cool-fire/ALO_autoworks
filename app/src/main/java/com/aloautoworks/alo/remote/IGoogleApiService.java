package com.aloautoworks.alo.remote;

import com.aloautoworks.alo.models.Garage.Garages;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleApiService {

    @GET
    Call<Garages> getNearbygarages(@Url String url);
}
