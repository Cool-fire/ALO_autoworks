package com.aloautoworks.alo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    private TextView name;
    private TextView country;
    private TextView phone;
    private TextView email;
    private EditText phoneedit;
    private TextView emailedit;
    private Button updatebtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference postref;
    private FirebaseUser user;
    private DatabaseReference child;
    private ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setupviews();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        postref = mDatabase.child("users");
        user = mAuth.getCurrentUser();
        if(user!=null)
        {
            child = postref.child(user.getUid());
            child.addListenerForSingleValueEvent(new ValueEventListener() {

                private String phonedb;
                private String emaildb;
                private String namedb;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    namedb = dataSnapshot.child("Name").getValue(String.class);
                    emaildb = dataSnapshot.child("Email").getValue(String.class);
                    phonedb = dataSnapshot.child("Phone").getValue(String.class);

                    name.setText(namedb);
                    phone.setText(phonedb);
                    email.setText(emaildb);

                    try {

                        Picasso.get().load(user.getPhotoUrl()).into(pic);

                    }catch (Exception e)
                    {
                Toast.makeText(getApplicationContext(),"Error loading pic",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
        }

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateuser();

            }
        });
    }

    private void updateuser() {
        String emailText = emailedit.getText().toString();
        String phoneText = phoneedit.getText().toString();

        if(emailText.isEmpty() || phoneText.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Check the Fields and try again", Toast.LENGTH_SHORT).show();
        }
        else
        {
            updateUserinFirebase(emailText,phoneText);
        }
    }

    private void updateUserinFirebase(String emailText, String phoneText) {
        mDatabase.child("users").child(user.getUid()).child("Email").setValue(emailText);
        mDatabase.child("users").child(user.getUid()).child("Phone").setValue(phoneText);
        Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
        finish();

    }

    private void setupviews() {
        name=(TextView)findViewById(R.id.nametext);
        country=(TextView)findViewById(R.id.countrytext);
        phone=(TextView)findViewById(R.id.phonetext);
        email=(TextView)findViewById(R.id.emailtext);
        phoneedit=(EditText)findViewById(R.id.phoneedit);
        emailedit=(TextView)findViewById(R.id.emailedit);
        updatebtn=(Button)findViewById(R.id.updatebutton);
        pic = (ImageView)findViewById(R.id.profilepic);
    }
}
