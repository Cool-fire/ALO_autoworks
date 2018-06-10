package com.aloautoworks.alo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aloautoworks.alo.models.UserModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FloatingActionButton googlesignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;
    private static final int RC_SIGNIN = 1234;
    private FirebaseAuth mAuth;
    private FloatingActionButton phonesignIn;
    private static final int RC_SIGN = 1;
    private TextInputEditText email;
    private TextInputEditText password;
    private Button loginButton;
    private TextView forgotpassword;
    private ProgressDialog dialog;
    private DatabaseReference mDatabase;
    private ProgressDialog dialog1;
    private DataSnapshot name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Login");
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));
        
        googlesignIn = (FloatingActionButton)findViewById(R.id.googleBttn);
        phonesignIn = (FloatingActionButton)findViewById(R.id.phoneBttn);
        email = (TextInputEditText)findViewById(R.id.loginEmail);
        password = (TextInputEditText)findViewById(R.id.loginPassword);
        loginButton = (Button)findViewById(R.id.login);
        forgotpassword = (TextView)findViewById(R.id.textView);
        dialog = new ProgressDialog(LoginActivity.this);
        dialog1 = new ProgressDialog(LoginActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();

        googlesignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        phonesignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInusingPhone();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                signinwithEmail();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                if(!userEmail.isEmpty())
                {
                    dialog1.setTitle("Reset Password");
                    dialog1.setMessage("Sending Reset Email...");
                    dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog1.show();
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                if(dialog1.isShowing())
                                {
                                    dialog1.dismiss();
                                }
                                Snackbar.make(findViewById(R.id.loginactivity),"Reset Email Sent Check your Inbox",Snackbar.LENGTH_SHORT).show();
                            }
                            if(!task.isSuccessful())
                            {
                                if(dialog1.isShowing())
                                {
                                    dialog1.dismiss();
                                }
                                Snackbar.make(findViewById(R.id.loginactivity),"Error Occurred please try again",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if(userEmail.isEmpty())
                {
                    email.setError("Email Required");
                }
                else if(!userEmail.contains("@") ||!userEmail.contains(".") )
                {
                    email.setError("Invalid Email");
                }
            }
        });
    }

    private void signinwithEmail() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        if(!userEmail.contains("@") || userEmail.isEmpty() || !userEmail.contains(".") || userPassword.isEmpty())
        {
            if(!userEmail.contains("@") || userEmail.isEmpty() || !userEmail.contains("."))
            {
                email.setError("Invalid Email");
            }
            if(userPassword.isEmpty())
            {
                password.setError("Invalid Password");
            }

        }
        else
        {
            dialog.setTitle("Login");
            dialog.setMessage("logging in...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        if(dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        FirebaseUser user = mAuth.getCurrentUser();
                        checkForDataInFirebase(user);
                       // Toast.makeText(getApplicationContext(),"successfully signed in",Toast.LENGTH_SHORT).show();
                    }

                    if(!task.isSuccessful())
                    {
                        if(dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Snackbar.make(findViewById(R.id.loginactivity),"Error Occurred please login Again",Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void signInusingPhone() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme)
                        .setLogo(R.drawable.telephoneicon)
                        .build(),
                RC_SIGN);


    }

    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGNIN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//
//                Log.w("TAG", "Google sign in failed", e);
//
//            }
//        }

        if(requestCode == RC_SIGN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("TAG", "onActivityResult: "+user.getPhoneNumber());
                Toast.makeText(getApplicationContext(),"Succesfully signed In",Toast.LENGTH_SHORT).show();
            } else {

                if(resultCode != 0)
                {
                    Snackbar.make(findViewById(R.id.loginactivity),"Error Occurred",Snackbar.LENGTH_SHORT).show();
                }

            }

        }
        if(requestCode == RC_SIGNIN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("TAG", "onActivityResult: "+user.getDisplayName());
                checkForDataInFirebase(user);

            } else {
                if(resultCode != 0)
                {
                    Snackbar.make(findViewById(R.id.loginactivity),"Error Occurred",Snackbar.LENGTH_SHORT).show();
                }
            }

        }

    }

    private void checkForDataInFirebase(FirebaseUser user) {

        startDialog();
        final String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                                stopDialog();


                                    Intent intent = new Intent(getApplicationContext(),UpdateEmailUser.class);
                                    startActivity(intent);
                                    finish();


                            }
                            else
                            {

                                stopDialog();
                                checkForUservehicle(new getVehicleresult() {
                                    @Override
                                    public void checkVehicle(String value) {
                                        if(value.toString()=="true")
                                        {
                                            Intent intent = new Intent(getApplicationContext(),QuoteMainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                stopDialog();
                        }
                    });

                }
                else if(!dataSnapshot.hasChild(uid))
                {
                    Log.d("TAG", "onDataChange: else");
                    stopDialog();
                    Intent intent = new Intent(getApplicationContext(),UpdateEmailUser.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    stopDialog();
            }
        });

    }

    private void checkForUservehicle(final getVehicleresult getVehicleresult) {
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
                    else
                    {
                        getVehicleresult.checkVehicle("false");
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

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),user.getDisplayName(),Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.loginactivity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }


                    }
                });
    }
    private void startDialog() {
        dialog.setTitle("Logging in");
        dialog.setMessage("verifying..");
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
