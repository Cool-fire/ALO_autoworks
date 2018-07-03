package com.aloautoworks.alo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private EditText pincode;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private List<vehicle> list = new ArrayList<>();
    private List<String> vehiclelist = new ArrayList<>();
    private List<String> servicelist = new ArrayList<>();
    private FirebaseUser user;
    private String uid;
    private Button quoteBttn;
    private EditText modelNo;

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

    private EditText mileage;
    private ImageView carbutton;
    private MaterialBetterSpinner subservice;
    private TextView headingText;
    private Drawable errordrawable;
    private String string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);


        vehiclelist.add("");
        Log.d("TAG", "onCreate: QuoteActivity");

        setupviews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quote");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));





        subservice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                subservice.setError(null,null);
            }
        });


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
                try
                {
                    vehicle vehiclename = list.get(i);
                    modelNo.setText(vehiclename.modelName);
                    mileage.setText(vehiclename.fuel);
                    vehicleName.setError(null,null);
                }
                catch (Exception e)
                {

                }


            }
        });
        serviceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                ArrayAdapter<String> NewAdapter;
                NewAdapter = getArrayList(Arrays.asList(arraylist[i]));
                subservice.setAdapter(NewAdapter);
                subservice.setText("");
                serviceType.setError(null,null);
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

               checkforDetails();

           }
       });




    }

    private void checkforDetails() {

        String vehiclename = vehicleName.getText().toString();
        String servicetype= serviceType.getText().toString();
        String modelno = modelNo.getText().toString();
        String pincodeno  = pincode.getText().toString();
        String mileageno = mileage.getText().toString();
        String subserviceno = subservice.getText().toString();

        if(vehiclename.isEmpty() || servicetype.isEmpty() || modelno.isEmpty() || pincodeno.isEmpty() || mileageno.isEmpty() || subserviceno.isEmpty())
        {
            if(vehiclename.isEmpty())
            {
                vehicleName.setError("Vehicle Required",errordrawable);
            }
            if(servicetype.isEmpty())
            {
                serviceType.setError("Service type Requied",errordrawable);
            }
            if(modelno.isEmpty())
            {
                modelNo.setError("Model Required",errordrawable);
            }
            if(pincodeno.isEmpty())
            {
                pincode.setError("pincode Required",errordrawable);
            }
            if(mileageno.isEmpty())
            {
                mileage.setError("Mileage required",errordrawable);
            }
            if(subserviceno.isEmpty())
            {
                subservice.setError("service Type Required",errordrawable);
            }
        }
        else
        {
            Intent intent = new Intent(QuoteActivity.this,MapsActivity.class);
            intent.putExtra("vehiclename",vehiclename);
            intent.putExtra("servicetype",servicetype);
            intent.putExtra("modelno",modelno);
            intent.putExtra("pincodeno",pincodeno);
            intent.putExtra("mileageno",mileageno);
            intent.putExtra("subservice",subserviceno);
            startActivity(intent);
        }
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

        errordrawable = getResources().getDrawable(R.drawable.error);
        errordrawable.setBounds(0, 0, errordrawable.getIntrinsicWidth(), errordrawable.getIntrinsicHeight());


        vehicleName = (MaterialBetterSpinner)findViewById(R.id.vehicle);
        vehicleName.setHintTextColor(Color.WHITE);
        vehicleName.setTextColor(Color.WHITE);
        vehicleName.setBackground(getDrawable(R.drawable.et_bg));


        serviceType = (MaterialBetterSpinner)findViewById(R.id.serviceType);
        serviceType.setHintTextColor(Color.WHITE);
        serviceType.setTextColor(Color.WHITE);
        serviceType.setBackground(getDrawable(R.drawable.et_bg));

        
        modelNo = (EditText)findViewById(R.id.modelNumber);
        pincode = (EditText)findViewById(R.id.pincode);
        quoteBttn = (Button)findViewById(R.id.quoteBttn);
        mileage = (EditText)findViewById(R.id.mileage);
        carbutton = (ImageView)findViewById(R.id.carbutton);
        
        subservice = (MaterialBetterSpinner)findViewById(R.id.subservice);
        subservice.setHintTextColor(Color.WHITE);
        subservice.setTextColor(Color.WHITE);
        subservice.setBackground(getDrawable(R.drawable.et_bg));


        headingText = (TextView)findViewById(R.id.Heading);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Adlery-Pro-trial.ttf");
        headingText.setTypeface(typeface);

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
