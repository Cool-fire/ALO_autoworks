package com.aloautoworks.alo;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class AddGarage extends AppCompatActivity {

    private TextInputEditText ServiceCenterName;
    private TextInputEditText phoneNumber;
    private TextInputEditText emailAddress;
    private TextInputEditText GarageAddress;
    private TextInputEditText pincode;
    private ImageView addlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_garage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));
        Log.d("TAG", "onCreate: AddGarageActivity");
        setupViews();

        addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(AddGarage.this,AddGarageLocation.class);
                    startActivity(intent);
            }
        });

    }

    private void setupViews() {
        ServiceCenterName = (TextInputEditText)findViewById(R.id.ServiceCentreName);
        phoneNumber = (TextInputEditText)findViewById(R.id.phoneNumber);
        emailAddress = (TextInputEditText)findViewById(R.id.emailAddress);
        GarageAddress = (TextInputEditText)findViewById(R.id.GarageAddress);
        pincode = (TextInputEditText)findViewById(R.id.pincode);
        addlocation = (ImageView)findViewById(R.id.addLocation);
        
    }
}
