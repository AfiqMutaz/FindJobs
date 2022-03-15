package com.example.findjobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = EditProfileActivity.class.getSimpleName();

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText edtName, edtEmail, edtPhone, edtAddress1, edtAddress2;
    private Button btnSave, btnBack;

    private String first, last, email, phone, address1, address2, fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edtName = findViewById(R.id.idEdtName);
        edtEmail = findViewById(R.id.idEdtEmail);
        edtPhone = findViewById(R.id.idEdtPhone);
        edtAddress1 = findViewById(R.id.idEdtAddress1);
        edtAddress2 = findViewById(R.id.idEdtAddress2);

        btnSave = findViewById(R.id.idBtnSave);
        btnBack = findViewById(R.id.idBtnBack);

        Intent intent = getIntent();
        first = intent.getStringExtra("first");
        last = intent.getStringExtra("last");

        fullname = first + " " + last;

        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        address1 = intent.getStringExtra("address1");
        address2 = intent.getStringExtra("address2");

        edtName.setText(fullname);
        edtEmail.setText(email);
        edtPhone.setText(phone);
        edtAddress1.setText(address1);
        edtAddress2.setText(address2);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(
                        edtName.getText().toString(),
                        edtEmail.getText().toString(),
                        edtPhone.getText().toString());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void userLogout(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    public void saveChanges(String name, String em, String number) {
        String userId = fAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(userId);

        //Update First Name
        if(!(fullname.equals(edtName.getText().toString()))) {
            String[] split = name.split("\\s+");

            docRef.update("first", split[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "First Name updated");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Error updating document");
                }
            });

            //Update Last Name
            docRef.update("last", split[1]).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "Last name updated");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Error updating document");
                }
            });
        }

        //Update Email (Require Verification)
        if(!(email.equals(edtEmail.getText().toString()))) {
            docRef.update("email", em).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "Email updated");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Error updating document");
                }
            });
        }

        //Update Phone Number (Require Verification)
        if(!(phone.equals(edtPhone.getText().toString()))) {
            docRef.update("phone", number).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "Phone updated");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Error updating document");
                }
            });
        }

        //Update Address 1
//        docRef.update("first", fn).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.d(TAG, "First name update");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Error updating document");
//            }
//        });
//
//        //Update Address 2
//        docRef.update("first", fn).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.d(TAG, "First name update");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Error updating document");
//            }
//        });

        Toast.makeText(this, "Profile updated", Toast.LENGTH_LONG).show();
        finish();
    }
}