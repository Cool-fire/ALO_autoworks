package com.aloautoworks.alo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aloautoworks.alo.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdatePhoneUser extends AppCompatActivity {

    private Toolbar myToolbar;
    private ProgressDialog dialog;
    private EditText email;
    private Button updateBttn;
    private EditText name;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone_user);
        setupViews();

        mAuth = FirebaseAuth.getInstance();
//
//        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//        getSupportActionBar().setTitle("Update Info");
//        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));
        updateBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfotoFirebase();
            }
        });
    }
    private void updateInfotoFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postref = mDatabase.child("users");
        FirebaseUser user = mAuth.getCurrentUser();
        final  String Phone = user.getPhoneNumber();
        final String uid = user.getUid();
        final String Name = name.getText().toString();
        final String Email = email.getText().toString();
        if(Name.isEmpty() || Email.isEmpty())
        {
            if(Name.isEmpty())
            {
                name.setError("Name required");
            }
            if(Email.isEmpty())
            {
                email.setError("Email required");
            }
        }
        else
        {
            startDialog();
            postref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(uid))
                    {
                        mDatabase.child("users").child(uid).child("Name").setValue(Name);
                        mDatabase.child("users").child(uid).child("Email").setValue(Email);
                        stopDialog();
                        checkForUservehicle(new UpdateEmailUser.getVehicleresult() {
                            @Override
                            public void checkVehicle(String value) {
                                if(value.toString()=="true")
                                {
                                    quoteIntent();
                                }
                                else
                                {
                                    registrationIntent();
                                }
                            }
                        });

                    }
                    else if(!dataSnapshot.hasChild(uid))
                    {
                        UserModel userModel = new UserModel(uid,Name,Email,Phone);
                        mDatabase.child("users").child(uid).setValue(userModel);
                        stopDialog();
                        registrationIntent();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    stopDialog();
                }
            });

        }


    }

    private void setupViews() {

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        updateBttn = (Button)findViewById(R.id.updateBttn);
        dialog = new ProgressDialog(UpdatePhoneUser.this);
    }

    private void checkForUservehicle(final UpdateEmailUser.getVehicleresult getVehicleresult) {
        DatabaseReference uservehicleref = mDatabase.child("user vehicles");
        FirebaseUser user = mAuth.getCurrentUser();
        final String uid = mAuth.getUid();

        final int[] flag = new int[1];

        uservehicleref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid))
                {

                    Log.d("TAG", "onDataChange: "+dataSnapshot.child(uid).hasChildren());
                    if(dataSnapshot.child(uid).hasChildren())
                    {
                        getVehicleresult.checkVehicle("true");
                        Log.d("TAG", "onDataChange: e"+ flag[0]);
                    }
                }
                else
                {
                    getVehicleresult.checkVehicle("false");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getVehicleresult.checkVehicle("false");
            }
        });

    }

    private void quoteIntent() {
        Intent intent = new Intent(UpdatePhoneUser.this,QuoteMainActivity.class);
        startActivity(intent);
        finish();
    }
    private void registrationIntent() {


        Intent intent = new Intent(UpdatePhoneUser.this,RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    private void startDialog() {
        dialog.setTitle("update");
        dialog.setMessage("updating...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    private void stopDialog() {
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
    public interface getVehicleresult {
        void checkVehicle(String value);
    }
}
