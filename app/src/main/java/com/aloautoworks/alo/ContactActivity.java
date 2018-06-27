package com.aloautoworks.alo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aloautoworks.alo.models.Contact;
import com.aloautoworks.alo.models.vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactActivity extends AppCompatActivity {

    private EditText nameArea;
    private EditText emailArea;
    private EditText phoneArea;
    private EditText messageArea;
    private Button submitBttn;
    private FirebaseAuth mAuth;
    private DatabaseReference contactReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setupviews();

        mAuth = FirebaseAuth.getInstance();
        contactReference = FirebaseDatabase.getInstance().getReference("Contact");
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadtoFirebase();
            }
        });
    }

    private void uploadtoFirebase() {
        String namearea = nameArea.getText().toString();
        String emailarea = emailArea.getText().toString();
        String phonearea = phoneArea.getText().toString();
        String messagearea = messageArea.getText().toString();
        if(namearea.isEmpty() || emailarea.isEmpty() || phonearea.isEmpty() || messagearea.isEmpty())
        {
            if(namearea.isEmpty())
            {
                nameArea.setError("Field Required");
            }
            if(emailarea.isEmpty())
            {
                emailArea.setError("Field Required");
            }
            if(phonearea.isEmpty())
            {
                phoneArea.setError("Field Required");
            }
            if(messagearea.isEmpty())
            {
                messageArea.setError("Field Required");
            }
        }
        else
        {
            FirebaseUser user = mAuth.getCurrentUser();
            String uid = user.getUid();
            String id= contactReference.push().getKey();
            Contact contact = new Contact(uid,namearea,emailarea,phonearea,messagearea,id);
            contactReference.child(id).setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    successToast();
                }
            });
        }

    }

    private void successToast() {
        Toast.makeText(getApplicationContext(),"Successfully Submitted",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupviews() {
        nameArea = (EditText)findViewById(R.id.nameArea);
        emailArea = (EditText)findViewById(R.id.emailArea);
        phoneArea = (EditText)findViewById(R.id.phoneNumberArea);
        messageArea = (EditText)findViewById(R.id.messageArea);
        submitBttn = (Button)findViewById(R.id.submitBttn);
    }
}
