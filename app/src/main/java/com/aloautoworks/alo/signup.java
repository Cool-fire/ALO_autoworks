package com.aloautoworks.alo;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity{

    private TextInputEditText Email;
    private TextInputEditText Password;
    private Button signupButton;
    private Button signinButton;
    private FirebaseAuth mAuth;

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
            }
        });
        

    }

    private void signIntheUser() {
        String userEmail = Email.getText().toString();
        String userPassword = Password.getText().toString();
        if(!userEmail.contains("@") || userEmail.isEmpty())
        {
            Email.setError("Invalid Email");
        }
        if(userPassword.isEmpty())
        {
            Password.setError("Invalid Password");
        }
    }

    private void setupviews() {
        
        Email = (TextInputEditText)findViewById(R.id.Email);
        Password = (TextInputEditText)findViewById(R.id.Password);
        signupButton = (Button)findViewById(R.id.signupBttn);
        signinButton = (Button)findViewById(R.id.loginBttn);
        
    }
}
