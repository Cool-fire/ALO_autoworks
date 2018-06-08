package com.aloautoworks.alo;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.aloautoworks.alo.models.vehicle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegistrationActivity extends AppCompatActivity {


    String[] VEHICLELIST = {"Car", "Motorcycle", "Bus", "Truck"};
    String[] MANUFACTURERLIST = {"Isuzu", "chevrolet", "suzuki", "maruthi"};
    private MaterialBetterSpinner vehicleSpinner;
    private ArrayAdapter<String> vehiclelAdapter;
    private MaterialBetterSpinner manufacturerSpinner;
    private ArrayAdapter<String> manufacturerAdapter;
    private TextInputEditText model;
    private TextInputEditText fuel;
    private Button registerBttn;
    private DatabaseReference vehicleReference;
    private DatabaseReference uservehicleReference;
    private FirebaseAuth mAuth;
    private CircularProgressButton registerCircularBttn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Vehicle Registration");
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));


        vehiclelAdapter = getArrayList(VEHICLELIST);
        vehicleSpinner = (MaterialBetterSpinner)findViewById(R.id.vehicle);
        vehicleSpinner.setAdapter(vehiclelAdapter);

        manufacturerAdapter = getArrayList(MANUFACTURERLIST);
        manufacturerSpinner = (MaterialBetterSpinner)findViewById(R.id.manufacturer);
        manufacturerSpinner.setAdapter(manufacturerAdapter);

        model = (TextInputEditText)findViewById(R.id.model);
        fuel = (TextInputEditText)findViewById(R.id.petrol);

        //registerBttn = (Button)findViewById(R.id.register);
        registerCircularBttn = (CircularProgressButton)findViewById(R.id.register);

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
        registerCircularBttn.dispose();
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
                vehicleSpinner.setError("vehicle type required");
            }
            if(manufacter.isEmpty())
            {
                manufacturerSpinner.setError("manufacturer required");
            }
            if(modelNo.isEmpty())
            {
                model.setError("model required");
            }
            if (fuelquantity.isEmpty())
            {
                fuel.setError("fuel quantity required");
            }
        }
        else
        {
            registerCircularBttn.startAnimation();

            FirebaseUser user = mAuth.getCurrentUser();
            String uid = user.getUid();
            String id= vehicleReference.push().getKey();

            vehicle registerVehicle = new vehicle(id, vehicleName, manufacter, modelNo, fuelquantity);
            vehicleReference.child(id).setValue(registerVehicle);
            uservehicleReference.child(uid).child(id).setValue(registerVehicle);

            registerCircularBttn.revertAnimation();

            gotoQuoteActivity();
            Toast.makeText(RegistrationActivity.this,"Vehicle  created", Toast.LENGTH_LONG).show();
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
