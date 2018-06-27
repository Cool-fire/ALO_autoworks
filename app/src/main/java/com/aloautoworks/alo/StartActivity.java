package com.aloautoworks.alo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private TextView headingText;
    private Button signupBttn;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        setupviews();

        signupBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,signup.class);
                startActivity(intent);
                finish();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupviews() {
        
        headingText = (TextView)findViewById(R.id.headingtext);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Adlery-Pro-trial.ttf");
        headingText.setTypeface(typeface);

        signupBttn = (Button)findViewById(R.id.signupBttn);
        loginText = (TextView)findViewById(R.id.loginText);
    }
}
