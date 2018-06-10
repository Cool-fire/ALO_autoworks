package com.aloautoworks.alo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.aloautoworks.alo.models.vehicle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuoteActivity extends AppCompatActivity {

    private MaterialBetterSpinner vehicleName;
    private MaterialBetterSpinner serviceType;
    private TextInputEditText numberplate;
    private TextInputEditText pincode;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private List<vehicle> list = new ArrayList<>();
    private List<String> vehiclelist = new ArrayList<>();
    private List<String> servicelist = new ArrayList<>();
    private FirebaseUser user;
    private String uid;
    private Button quoteBttn;
    private TextInputEditText modelNo;

    String[] SERVICELIST = {"Servicing and MOT", "Clutch and Gearbox Repairs", "Brakes aand Exhausts", "Mobile Mechanics and Services","Engine and Cooling","Air-con,Heating and Cooling","BodyWorks,Dents and Smart Repairs","Breaak down and Recovery",
                            "Diagnostics","Electicl and Batteries","Hybrid and Electric Vehicles","Safety Components","Steering and Suspension","Tyres,Wheels and Tracking","Windows,Windscreens,Mirrors"};
    private TextInputEditText mileage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);


        vehiclelist.add("");

        setupviews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quote");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        uid = user.getUid();
        ArrayAdapter<String> arrayList = getArrayList(Arrays.asList(SERVICELIST));
        serviceType.setAdapter(arrayList);
        checkForUservehicle(new getVehicleresults() {
            @Override
            public void getvehicle(vehicle value) {
                if(vehiclelist.contains(""))
                {
                    vehiclelist.remove("");
                }
                list.add(value);
                vehiclelist.add(value.manufacturerName);
                Log.d("TAG", "getvehicle: "+value);
                callAdapter();
            }
        });

        vehicleName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vehicle vehicleName = list.get(i);
                modelNo.setText(vehicleName.modelName);
                mileage.setText(vehicleName.fuel);

            }
        });




       callAdapter();

       quoteBttn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                    Intent intent = new Intent(QuoteActivity.this,MapsActivity.class);
                    startActivity(intent);
           }
       });




    }

    private void callAdapter() {
        ArrayAdapter<String> arrayList = getArrayList(vehiclelist);
        vehicleName.setAdapter(arrayList);
    }

    private void setupviews() {


        vehicleName = (MaterialBetterSpinner)findViewById(R.id.vehicle);
        serviceType = (MaterialBetterSpinner)findViewById(R.id.serviceType);
        numberplate = (TextInputEditText)findViewById(R.id.numberplate);
        modelNo = (TextInputEditText)findViewById(R.id.modelNumber);
        pincode = (TextInputEditText)findViewById(R.id.pincode);
        quoteBttn = (Button)findViewById(R.id.quoteBttn);
        mileage = (TextInputEditText)findViewById(R.id.mileage);

    }

    private ArrayAdapter<String> getArrayList(List<String> list) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
        return arrayAdapter;

    }

    private void checkForUservehicle(final getVehicleresults getVehicleresult) {
        DatabaseReference uservehicleref = mDatabase.child("user vehicles").child(uid);

        uservehicleref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot data : dataSnapshot.getChildren())
               {
                   vehicle vehicleData = data.getValue(vehicle.class);
                   getVehicleresult.getvehicle(vehicleData);
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public interface getVehicleresults {
        void getvehicle(vehicle value);

    }
}
