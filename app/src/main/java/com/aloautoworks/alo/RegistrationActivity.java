package com.aloautoworks.alo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.aloautoworks.alo.models.vehicle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegistrationActivity extends AppCompatActivity {


    String[] VEHICLELIST = {"Car", "Motorcycle", "Bus", "Truck"};
    String[] MANUFACTURERLIST = {"Hyundai", "maruthi suzuki", "Ford", "mahindra","Tata motors","Toyota","Volkswagen","Skoda","Rolls royce","Renault"};
    String[] HyndaiList = {"EON", "SANTRO","XING", "I10", "GRAND I10","I20", "VERNA", "ELANTRA", "SONATA", "SANTA FE", "BALENO", "CRETA"};
    String[] SuzukiList = {"ALTO 800", "ALTO K10", "CELERIO", "CIAZ", "DZIRE","EECO", "ERTIGA", "VITARA", "GYPSY", "OMNI", "RITZ", "STINGRAY", "SWIFT", "WAGONR"};
    String[] FordList = {"FORD CLASSIC", "FORD ECOSPORT SUV","ENDEAVOUR SUV", "FORD FIESTA" , "FORD FIGO."};
    String[] MahindraList = {"MAHINDRA BOLERO", "QUANTO" , "REVO E20" , "SCORPIO", "THAR", "VERITO", "VERITO VIBE", "XUV500", "XYLO"};
    String[] TataList = {"NANO", "BOLT", "ZEST", "INDICA", "MANZA", "INDIGO ECS", "INDIGO XL", "SAFARI STORME", "SAFARI DICOR", "SUMO GOLD", "MOVUS", "ARIA"};
    String[] ToyotaList ={"TOYOTA ETIOS LIVA", "ETIOS SEDAN", "ETIOS CROSS", "INNOVA", "COROLLA ALTIS", "FORTUNER", "CAMRY", "PRIUS", "PRADO" , "LAND CRUIZER"};
    String[] VolkswagenList = {"VOLKSWAGEN NEW POLO", "VW POLO GT", "CROSS POLO", "JETTA", "NEW VENTO", "VENTO KONEKT", "VENTO TSI", "VENTO"};
    String[] SkodaList = {"SKODA FABIA", "SKODA OCTAVIA", "SKODA RAPID","SKODA SUPERB", "SKODA YETI."};
    String[] RollsList = {"ROLLS ROYCE GHOST","ROLLS ROYCE PHANTOM", "ROLLS ROYCE WRAITH"};
    String[] RenaultList = {"RENAULT DUSTER", "RENAULT FLUENCE","RENAULT KOLEOS", "RENAULT PULSE", "RENAULT SCALA"};
    String[][] arrayList = new String[][]{HyndaiList, SuzukiList,FordList,MahindraList,TataList,ToyotaList,VolkswagenList,SkodaList,RollsList,RenaultList};
    String[] dummyList = {""};

    private MaterialBetterSpinner vehicleSpinner;
    private ArrayAdapter<String> vehiclelAdapter;
    private MaterialBetterSpinner manufacturerSpinner;
    private ArrayAdapter<String> manufacturerAdapter;
    private MaterialBetterSpinner model;
    private TextInputEditText fuel;
    private Button registerBttn;
    private DatabaseReference vehicleReference;
    private DatabaseReference uservehicleReference;
    private FirebaseAuth mAuth;
    private Button registerCircularBttn;
    private Drawable drawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


//
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//        getSupportActionBar().setTitle("Vehicle Registration");
//        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));


        vehiclelAdapter = getArrayList(VEHICLELIST);
        vehicleSpinner = (MaterialBetterSpinner)findViewById(R.id.vehicle);
        vehicleSpinner.setHintTextColor(Color.WHITE);
        vehicleSpinner.setTextColor(Color.WHITE);
        vehicleSpinner.setBackground(getDrawable(R.drawable.et_bg));
        vehicleSpinner.setAdapter(vehiclelAdapter);
        vehicleSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              vehicleSpinner.setError(null,null);
            }
        });

        drawable = getResources().getDrawable(R.drawable.error);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());


        manufacturerAdapter = getArrayList(MANUFACTURERLIST);
        manufacturerSpinner = (MaterialBetterSpinner)findViewById(R.id.manufacturer);
        manufacturerSpinner.setHintTextColor(Color.WHITE);
        manufacturerSpinner.setTextColor(Color.WHITE);
        manufacturerSpinner.setBackground(getDrawable(R.drawable.et_bg));
        manufacturerSpinner.setAdapter(manufacturerAdapter);



        ArrayAdapter<String> dummyAdapter = getArrayList(dummyList);
        model = (MaterialBetterSpinner)findViewById(R.id.model);
        model.setHintTextColor(Color.WHITE);
        model.setTextColor(Color.WHITE);
        model.setBackground(getDrawable(R.drawable.et_bg));
        model.setAdapter(dummyAdapter);
        fuel = (TextInputEditText)findViewById(R.id.petrol);
        model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                model.setError(null,null);
            }
        });

        manufacturerSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                manufacturerSpinner.setError(null,null);
                ArrayAdapter<String> NewAdapter;
                NewAdapter = getArrayList(arrayList[i]);
                model.setAdapter(NewAdapter);
                model.setText("");


            }
        });

        //registerBttn = (Button)findViewById(R.id.register);
        registerCircularBttn = (Button)findViewById(R.id.register);

        vehicleReference = FirebaseDatabase.getInstance().getReference("vehicle");

        uservehicleReference = FirebaseDatabase.getInstance().getReference("user vehicles");

        mAuth = FirebaseAuth.getInstance();


        registerCircularBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Register();
            }
        });

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    private void Register() {
        String vehicleName = vehicleSpinner.getText().toString();
        String manufacter = manufacturerSpinner.getText().toString();
        String modelNo = model.getText().toString();
        String fuelquantity = fuel.getText().toString();
        if(vehicleName.isEmpty() || manufacter.isEmpty() || modelNo.isEmpty() || fuelquantity.isEmpty())
        {
            if(vehicleName.isEmpty())
            {
                vehicleSpinner.setError("vehicle type required",drawable);
            }
            if(manufacter.isEmpty())
            {
                manufacturerSpinner.setError("manufacturer required",drawable);
            }
            if(modelNo.isEmpty())
            {
                model.setError("model required",drawable);

            }
            if (fuelquantity.isEmpty())
            {
                fuel.setError("mileage required");
            }
        }
        else
        {


            FirebaseUser user = mAuth.getCurrentUser();
            String uid = user.getUid();
            String id= vehicleReference.push().getKey();
            vehicle registerVehicle = new vehicle(id, vehicleName, manufacter, modelNo, fuelquantity);
            vehicleReference.child(id).setValue(registerVehicle);
            uservehicleReference.child(uid).child(id).setValue(registerVehicle);


            if(getCallingActivity()!=null)
            {
               if(getCallingActivity().getClassName().toString().equals("com.aloautoworks.alo.QuoteActivity"))
               {
                   Toast.makeText(RegistrationActivity.this,"Vehicle  added", Toast.LENGTH_LONG).show();
                   finish();
               }
               else
               {
                   gotoQuoteActivity();
                   Toast.makeText(RegistrationActivity.this,"Vehicle  added", Toast.LENGTH_LONG).show();
               }
            }
            else
            {
                gotoQuoteActivity();
                Toast.makeText(RegistrationActivity.this,"Vehicle  created", Toast.LENGTH_LONG).show();
            }


        }
    }

    private void gotoQuoteActivity() {
        Intent intent = new Intent(getApplicationContext(),QuoteMainActivity.class);
        startActivity(intent);
        finish();
    }

    private ArrayAdapter<String> getArrayList(String[] list) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
        return arrayAdapter;

    }
}
