package com.aloautoworks.alo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aloautoworks.alo.models.Garage.Garages;
import com.aloautoworks.alo.models.Garage.Result;
import com.aloautoworks.alo.remote.GooglemapsApiclient;
import com.aloautoworks.alo.remote.IGoogleApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyGaragesActivity extends AppCompatActivity {
    
    private static final String TAG = "NearbyGarageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_garages);
        
        getNearbygarages();

        Log.d(TAG, "onCreate: ");
    }

    private void getNearbygarages() {
        String baseurl = "https://maps.googleapis.com/";

        double lat = 13.562195;
        double longi = 78.494009;
        String url = getUrl(lat, longi, "car repair");
        IGoogleApiService apiService = GooglemapsApiclient.getClient(baseurl).create(IGoogleApiService.class);
        Call<Garages> nearbygarages = apiService.getNearbygarages(url);
        nearbygarages.enqueue(new Callback<Garages>() {

            private List<Result> results;

            @Override
            public void onResponse(Call<Garages> call, Response<Garages> response) {
                String status = response.body().getStatus();
                Log.d("TAG", "onResponse: "+status);
                

                results = response.body().getResults();
                Log.d(TAG, "onResponse: ");
                


            }

            @Override
            public void onFailure(Call<Garages> call, Throwable t) {

            }
        });

    }

    private String getUrl(double latitude, double longitude, String placeType) {
        StringBuilder googglePlaceurl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googglePlaceurl.append("location="+latitude+","+longitude);
        googglePlaceurl.append("&radius="+10000);
        googglePlaceurl.append("&keyword="+placeType);
        googglePlaceurl.append("&key="+getResources().getString(R.string.mapsApiKey));

        return googglePlaceurl.toString();
    }
}
