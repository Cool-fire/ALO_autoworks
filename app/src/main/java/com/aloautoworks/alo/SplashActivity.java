package com.aloautoworks.alo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG ="SplashActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            Intent intent = new Intent(this,signup.class);
            startActivity(intent);
            finish();
        }
        else
        {
           checkForDataInFirebase(currentUser);

        }

    }


    private void checkForDataInFirebase(FirebaseUser user) {

        final String uid = user.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference postref = mDatabase.child("users");
        postref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid))
                {
                    postref.child(uid).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {

                        private String value;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            value = dataSnapshot.getValue(String.class);
                            Log.d("TAG", "onDataChange: event "+value.toString());
                            if(value.toString().equals("none"))
                            {
                                Log.d("TAG", "onDataChange: even1t "+value.toString());


                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                                finish();


                            }
                            else
                            {
                                Intent intent = new Intent(SplashActivity.this,QuoteMainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
                else if(!dataSnapshot.hasChild(uid))
                {
                    Log.d("TAG", "onDataChange: else");
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
