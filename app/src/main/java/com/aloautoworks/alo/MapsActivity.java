package com.aloautoworks.alo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aloautoworks.alo.models.Garage.Garages;
import com.aloautoworks.alo.models.Garage.Result;
import com.aloautoworks.alo.remote.Common;
import com.aloautoworks.alo.remote.IGoogleApiService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.BubbleIconFactory;
import com.google.maps.android.ui.IconGenerator;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiclient;

    private double latitude,longitude;
    private Location mLastLocation;
    private Marker mMarker;
    private LocationRequest mLocationRequest;

    IGoogleApiService mService;
    private static final int MY_PERMISSION_CODE = 123;
    private Context context;
    private OnLocationUpdatedListener locationListner;
    public Runnable locationRunnable;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        progressBar = (ProgressBar)findViewById(R.id.progressBarDialog);

        mService = Common.getGoogleAPIService();
        context = getApplicationContext();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(SmartLocation.with(context).location().state().locationServicesEnabled() || SmartLocation.with(context).location().state().isGpsAvailable())
            {
                checkLocationPermission();
            }
            else
            {
                Toast.makeText(context,"Please Enable Location Service",Toast.LENGTH_SHORT).show();
            }

        }


        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {


            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.action_garage:

                        callForNearbygarages();
                        break;
                }

            }
        });

    }

    @Override
    public void onStop() {
        SmartLocation.with(context).location().stop();
        super.onStop();
    }

    private void callForNearbygarages() {
        Log.d("TAG", "callForNearbygarages: ");
        final Handler handler = new Handler();
        SmartLocation.with(context).location().start(locationListner);
        locationListner = new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {

                progressBar.setVisibility(View.VISIBLE);
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng latLng1 = new LatLng(lat,lng);
                // handler.postDelayed(locationRunnable,8000);
                nearByplace(lat,lng,"car repair");

            }
        };
        locationRunnable = new Runnable() {
            @Override
            public void run() {
                SmartLocation.with(context).location().start(locationListner);
            }
        };


    }


    private void nearByplace(final double lat1, final double lng1, String placeType) {
        Log.d("TAG", "nearByplace: ");
        mMap.clear();
//        double lat = 13.562195;
//        double longi = 78.494009;
        String url = getUrl(lat1,lng1,placeType);

        mService.getNearbygarages(url)
                .enqueue(new Callback<Garages>() {
                    @Override
                    public void onResponse(Call<Garages> call, Response<Garages> response) {
                        if (response.isSuccessful())
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("TAG", "onResponse: "+response.body().getStatus());
                            for(int i=0 ;i<response.body().getResults().size();i++)
                            {
                                MarkerOptions markerOptions = new MarkerOptions();
                                Result googlePlace = response.body().getResults().get(i);
                                double lat = Double.parseDouble(String.valueOf(googlePlace.getGeometry().getLocation().getLat()));
                                double lng = Double.parseDouble(String.valueOf(googlePlace.getGeometry().getLocation().getLng()));
                                String placeName = googlePlace.getName();
                                String vicinity = googlePlace.getVicinity();
                                LatLng latLng = new LatLng(lat,lng);
                                markerOptions.position(latLng);
                                markerOptions.title(placeName);

                                IconGenerator iconFactory = new IconGenerator(MapsActivity.this);
                                iconFactory.setStyle(IconGenerator.STYLE_BLUE);

                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("$500")));
                                Marker garage = mMap.addMarker(markerOptions);
                                garage.showInfoWindow();
                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {

                                        Intent intent = new Intent(MapsActivity.this,ServiceCenterActivity.class);
                                        startActivity(intent);
                                        return false;
                                    }
                                });
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                            }
                            LatLng latLng1 = new LatLng(lat1,lng1);
                            MarkerOptions markerOption = new MarkerOptions();
                            markerOption.position(latLng1);
                            markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                           Marker location =  mMap.addMarker(markerOption);
                           location.showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        }
                    }

                    @Override
                    public void onFailure(Call<Garages> call, Throwable t) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(),"Can't Load",Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private String getUrl(double latitude, double longitude, String placeType) {
        StringBuilder googglePlaceurl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googglePlaceurl.append("location="+latitude+","+longitude);
        googglePlaceurl.append("&radius="+10000);
        googglePlaceurl.append("&keyword="+placeType);
        googglePlaceurl.append("&key="+getResources().getString(R.string.mapsApiKey));

        Log.d("TAG", "getUrl: "+googglePlaceurl);

        return googglePlaceurl.toString();
    }
    private void checkLocationPermission() {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_CODE);

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_CODE);

            }

        }
        else
        {
            callForNearbygarages();
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
             //  buildGoogleApiclient();
                progressBar.setVisibility(View.INVISIBLE);
                callForNearbygarages();
                mMap.setBuildingsEnabled(true);
                mMap.setMyLocationEnabled(true);
            }
            else
            {
                checkLocationPermission();
            }
        }
        else {
           // buildGoogleApiclient();
            callForNearbygarages();
            mMap.setBuildingsEnabled(true);
            mMap.setMyLocationEnabled(true);
        }
    }
//
//    private synchronized void buildGoogleApiclient() {
//
//        mGoogleApiclient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
//        mGoogleApiclient.connect();
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//            mLocationRequest = new LocationRequest();
//         //   mLocationRequest.setInterval(1000);
//           // mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
//        {
//            if(mGoogleApiclient.isConnected())
//            {
//                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiclient,mLocationRequest,this);
//
//            }
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        mGoogleApiclient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//     mLastLocation = location;
//     if(mMarker!=null)
//     {
//         mMarker.remove();
//     }
//     latitude = location.getLatitude();
//     longitude = location.getLongitude();
//
//        LatLng latLng = new LatLng(latitude,longitude);
//        MarkerOptions markerOptions = new MarkerOptions()
//                .position(latLng)
//                .title("Your Position")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//
//        mMarker = mMap.addMarker(markerOptions);
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//        if(mGoogleApiclient!=null)
//        {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiclient,this);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_CODE:

            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {

                        mMap.setMyLocationEnabled(true);
                    }
                    callForNearbygarages();
                    callForNearbygarages();
                }
                else
                {
                    Toast.makeText(this,"Permission Required",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
