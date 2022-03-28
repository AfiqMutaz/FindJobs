package com.example.findjobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    String emulatorIp = "10.0.2.2";

    //variable for FirebaseAuth class
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //variable for our text input
    //field for phone and OTP
    private EditText edtPhone, edtOTP;

    //buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    //string for storing our verification ID
    private String verificationId;

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.useEmulator("10.0.2.2", 9099);
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocationPermission();

        //initializing variables for button and Edittext
        edtPhone = findViewById(R.id.idEdtPhoneNumber);
        edtOTP = findViewById(R.id.idEdtOtp);
        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);

        //setting onclick listener for generate OTP button
        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //below line is for checking number whether the user has entered mobile number or not
                if(TextUtils.isEmpty(edtPhone.getText().toString())) {
                    //display toast if text field is empty
                    Toast.makeText(MainActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                } else {
                    String phone = "+673" + edtPhone.getText().toString();
                    sendVerificationCode(phone);
                }
            }
        });

        //onclick listener for verify otp btn
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validating if the otp text field is empty or not
                if(TextUtils.isEmpty(edtOTP.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    //if OTP field is not empty call verify method
                    verifyCode(edtOTP.getText().toString());
                }
            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        //inside this method checking if the code entered is correct or not
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //if code is correct and successful send user to new activity
                    Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    //toast code is not correct
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationCode(String number) {
        //this method is used for getting OTP on user phone number
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this).setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    //callback method is called on phone auth provider
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            //initializing our callback for on verification callback method
        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //when we receive the OTP it contains a unique id which we are storing in our string which we have already created
            verificationId = s;
        }

        //this method is called when user receive OTP from firebase
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //below line is used for getting OTP code which is sent in phone auth credentials
            final String code = phoneAuthCredential.getSmsCode();

            //check if the code is null or not
            if(code != null) {
                //if the code is not null then we are setting that code to our OTP edittext field
                edtOTP.setText(code);

                //after setting this code to OTP edittext field we are calling our verify code method
                verifyCode(code);
            }
        }

        //this method is called when firebase doesn't send our OTP code due to any error or issue
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            //display error message with firebase exception
            Toast.makeText(MainActivity.this, "e.getMessage()", Toast.LENGTH_SHORT).show();
        }
    };

    //below method is use to verify code from Firebase
    private void verifyCode(String code) {
        //below line is used for getting credentials from our verification id and code
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        //after getting credentials we are calling sign in method
        signInWithCredential(credential);
    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if(firebaseUser != null) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    //Give user opportunity to allow or deny location access
    private void getLocationPermission() {
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if(requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            //updateLocationUI();
        }
    }
}