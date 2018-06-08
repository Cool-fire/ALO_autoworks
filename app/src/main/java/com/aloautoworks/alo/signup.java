package com.aloautoworks.alo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.aloautoworks.alo.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity{

    private TextInputEditText Email;
    private TextInputEditText Password;
    private Button signupButton;
    private Button signinButton;
    private FirebaseAuth mAuth;
    private static final String TAG = "signupActivity";
    private static final int RC_SIGN_IN = 123;
    private ProgressDialog dialog;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("signup");
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));
        
        setupviews();

        mAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIntheUser();
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Password.getWindowToken(), 0);
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);

            }
        });




    }

    private void signIntheUser() {
        String userEmail = Email.getText().toString();
        String userPassword = Password.getText().toString();
        if(!userEmail.contains("@") || userEmail.isEmpty() || userPassword.isEmpty() )
        {
            if(!userEmail.contains("@") || userEmail.isEmpty())
            {
                Email.setError("Invalid Email");
            }
            if(userPassword.isEmpty())
            {
                Password.setError("Invalid Password");
            }

        }

        else
        {
           // progressBar.setVisibility(View.VISIBLE);
            startDialog();
            signupButton.setEnabled(false);
            signinButton.setEnabled(false);
            mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        stopDialog();
                        AddusertoFirebase();
                        Intent intent = new Intent(signup.this,UpdateEmailUser.class);
                        startActivity(intent);
                        finish();

                        //Snackbar.make(findViewById(R.id.signupActivity),"successfully signed up",Snackbar.LENGTH_SHORT).show();
                    }
                    if(!task.isSuccessful())
                    {
                      
                        stopDialog();
                        signupButton.setEnabled(true);
                        signinButton.setEnabled(true);
                        Snackbar.make(findViewById(R.id.signupActivity),"Error Occurred try Again",Snackbar.LENGTH_SHORT).show();
                    }



                }
            });
        }
    }

    private void startDialog() {
        dialog.setTitle("Signup");
        dialog.setMessage("signing up...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    private void stopDialog() {
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
    }

    private void AddusertoFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user  = mAuth.getCurrentUser();
        String uid = user.getUid();
        String email = user.getEmail();
        UserModel userModel = new UserModel(uid,"none",email,"none");
        mDatabase.child("users").child(uid).setValue(userModel);
    }

    private void setupviews() {
        
        Email = (TextInputEditText)findViewById(R.id.Email);
        Password = (TextInputEditText)findViewById(R.id.Password);
        signupButton = (Button)findViewById(R.id.signupBttn);
        signinButton = (Button)findViewById(R.id.loginBttn);
        dialog = new ProgressDialog(signup.this);

    }
}
