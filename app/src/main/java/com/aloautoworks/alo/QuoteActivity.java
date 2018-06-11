package com.aloautoworks.alo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

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

    private static final int REQUESTCAR = 100;
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

    String[] SERVICELIST = {"Servicing and MOT", "Clutch and Gearbox Repairs", "Brakes and Exhausts", "Mobile Mechanics and Services","Engine and Cooling","Air-con,Heating and Cooling","BodyWorks,Dents and Smart Repairs","Break down and Recovery",
                            "Diagnostics","Electical and Batteries","Hybrid and Electric Vehicles","Safety Components","Steering and Suspension","Tyres,Wheels and Tracking","Windows,Windscreens,Mirrors"};

    String[] ServicingList = {"Servicing and MOT"};
    String[] ClutchList = {"Clutch and Gearbox Repairs"};
    String[] BrakesList = {"Brakes and Exhausts"};
    String[] MechanicsList = {"Mobile Mechanics and Services"};
    String[] EngineList = {"Engine and Cooling"};
    String[] AirconList = {"Aircon Regas","Airconditioning Servies","car heater","other-Aircon heating,cooling","overheating problem","Heater Matrix"};
    String[] BodyWorksList = {"Respraying","BodyShop","Accident Repairs","Dent Removal","Paint Work Repair","Smart Repair,Scratches and scuff","welding","Panel Beating","Wrapping and vinyl wrap"};
    String[] BreakDownList = {"Break down and Recovery-other","other"};
    String[] DiagnosticsList = {"Diagnostics-other","Diesel particulate Filter Replacement","Engine Mapping and Rebalancing","Engine Tuning","Diesel Tuning","Diagnostic Test","Electrical fault Diagnostics","Diesel particulate filter clean"};
    String[] ElectricalList = {"Electrical and batteries"};
    String[] HybridList = {"Hybrid and Electric Vehicles"};
    String[] SafetyList = {"Airbags Repair","Seat belt Repair","Key Components","Central Locking system","key Programming and codes","auto locksmith","Immobilisers fault","car alarm keeps going off"};
    String[] SteeringList = {"Steering Repairs","shock absorber repair","Power steering","Steering Rack","Axle Reepairs","Car Pulling Left or Right","Steering wheel shaking"};
    String[] TyresList = {"Puncture Repair","Tyre Fitting","wheel Bearing","Front Wheel Alignment","Alloy wheel Refurbishment","wheel Tracking","Front and rear wheel alignment","specalist tyres"};
    String[] WindowsList = {"Windscreen Replacement","wing mirror","window Tinting","Electric window faults","window Replacement","windscreen wipers","Rear windscreen wipers","window repair","windscreen Repair"};

    String[][] arraylist = new String[][]{ServicingList,ClutchList,BrakesList,MechanicsList,EngineList,AirconList,BodyWorksList,BreakDownList,DiagnosticsList,ElectricalList,HybridList,SafetyList,SteeringList,TyresList,WindowsList};
    String[] dummyList = {""};

    private TextInputEditText mileage;
    private ImageView carbutton;
    private MaterialBetterSpinner subservice;


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
        ArrayAdapter<String> arrayAdapter = getArrayList(Arrays.asList(SERVICELIST));
        serviceType.setAdapter(arrayAdapter);

        ArrayAdapter<String> dummyAdapter = getArrayList(Arrays.asList(dummyList));
        subservice.setAdapter(dummyAdapter);


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
        serviceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                ArrayAdapter<String> NewAdapter;
                NewAdapter = getArrayList(Arrays.asList(arraylist[i]));
                subservice.setAdapter(NewAdapter);
                subservice.setText("");
            }
        });

        carbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    callRegistrationActivity();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUESTCAR)
        {
            if(!vehiclelist.isEmpty() || !list.isEmpty())
            {
                list.clear();
                vehiclelist.clear();
                vehiclelist.add("");
            }
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
        }
    }

    private void callRegistrationActivity() {
        Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
        startActivityForResult(intent,REQUESTCAR);
    }

    private void callAdapter() {
        ArrayAdapter<String> arrayList = getArrayList(vehiclelist);
        vehicleName.setAdapter(arrayList);
    }

    private void setupviews() {


        vehicleName = (MaterialBetterSpinner)findViewById(R.id.vehicle);
        serviceType = (MaterialBetterSpinner)findViewById(R.id.serviceType);
        modelNo = (TextInputEditText)findViewById(R.id.modelNumber);
        pincode = (TextInputEditText)findViewById(R.id.pincode);
        quoteBttn = (Button)findViewById(R.id.quoteBttn);
        mileage = (TextInputEditText)findViewById(R.id.mileage);
        carbutton = (ImageView)findViewById(R.id.carbutton);
        subservice = (MaterialBetterSpinner)findViewById(R.id.subservice);
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
