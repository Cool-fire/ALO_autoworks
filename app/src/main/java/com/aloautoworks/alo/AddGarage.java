package com.aloautoworks.alo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aloautoworks.alo.models.ServiceCenter;
import com.aloautoworks.alo.models.vehicle;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddGarage extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1000;
    private TextInputEditText ServiceCenterName;
    private TextInputEditText phoneNumber;
    private TextInputEditText emailAddress;
    private TextInputEditText GarageAddress;
    private TextInputEditText pincode;
    private ImageView addlocation;
    private LatLng garagelatLng;
    private TextInputEditText latText,longText;
    private LinearLayout addLayout;
    private Button addButton;
    private DatabaseReference garageReference;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_garage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));
        Log.d("TAG", "onCreate: AddGarageActivity");

        garageReference = FirebaseDatabase.getInstance().getReference("ServiceCenters");
        setupViews();

        addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(AddGarage.this,AddGarageLocation.class);
                    startActivityForResult(intent,REQUEST_LOCATION);
            }
        });

        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddGarage.this,AddGarageLocation.class);
                startActivityForResult(intent,REQUEST_LOCATION);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGaragetoDatabase();
            }
        });

    }

    private void addGaragetoDatabase() {
        String servicecentername = ServiceCenterName.getText().toString();
        String phonenumber = phoneNumber.getText().toString();
        String emailaddress = emailAddress.getText().toString();
        String garageaddress = GarageAddress.getText().toString();
        String lat = latText.getText().toString();
        String lon = longText.getText().toString();
        String pincod = pincode.getText().toString();

        String error = "This field is required";

        if(servicecentername.isEmpty() || phonenumber.isEmpty() || emailaddress.isEmpty() || garageaddress.isEmpty() || pincod.isEmpty() || lat.isEmpty()|| lon.isEmpty())
        {
            if(servicecentername.isEmpty())
            {
                ServiceCenterName.setError(error);
            }
            if(phonenumber.isEmpty())
            {
                phoneNumber.setError(error);
            }
            if(emailaddress.isEmpty())
            {
                emailAddress.setError(error);
            }
            if(garageaddress.isEmpty())
            {
                GarageAddress.setError(error);
            }
            if(pincod.isEmpty())
            {
                pincode.setError(error);
            }
            if(lat.isEmpty())
            {
                latText.setError(error);
            }
            if(lon.isEmpty())
            {
                longText.setError(error);
            }
        }
        else
        {
            startDialog();
            String id= garageReference.push().getKey();
            ServiceCenter serviceCenter = new ServiceCenter(id,servicecentername,phonenumber,emailaddress,garageaddress,pincod,lat,lon);
            garageReference.child(id).setValue(serviceCenter);
            stopDialog();
            Toast.makeText(getApplicationContext(),"added garage",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void setupViews() {
        ServiceCenterName = (TextInputEditText)findViewById(R.id.ServiceCentreName);
        phoneNumber = (TextInputEditText)findViewById(R.id.phoneNumber);
        emailAddress = (TextInputEditText)findViewById(R.id.emailAddress);
        GarageAddress = (TextInputEditText)findViewById(R.id.GarageAddress);
        pincode = (TextInputEditText)findViewById(R.id.pincode);
        addlocation = (ImageView)findViewById(R.id.addLocation);
        latText = (TextInputEditText)findViewById(R.id.latitude);
        longText = (TextInputEditText)findViewById(R.id.longitude);
        addLayout = (LinearLayout)findViewById(R.id.addLayout);
        addButton = (Button)findViewById(R.id.addcentrebutton);
        dialog = new ProgressDialog(AddGarage.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_LOCATION)
        {
            if(resultCode==RESULT_OK)
            {
                garagelatLng = data.getParcelableExtra("location");
                double lat = garagelatLng.latitude;
                double lon = garagelatLng.longitude;
                latText.setText(Double.toString(lat));
                longText.setText(Double.toString(lon));
                Log.d("TAG", "onActivityResult: "+lat+" "+lon);
            }
        }
    }

    private void startDialog() {
        dialog.setTitle("updating");
        dialog.setMessage("loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    private void stopDialog() {
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
}
