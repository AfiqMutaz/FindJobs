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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseUser user;
    private String userId;

    private Button btnSubmit;
    private EditText edtFirstName, edtLastName, edtEmail, edtPhone;


    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnSubmit = findViewById(R.id.idBtnSubmit);
        edtFirstName = findViewById(R.id.idEdtFirstName);
        edtLastName = findViewById(R.id.idEdtLastName);
        edtEmail = findViewById(R.id.idEdtEmail);
        edtPhone = findViewById(R.id.idEdtPhone);

        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        edtPhone.setText(user.getPhoneNumber());

        //Check if User has already registered, if yes send to Home
        DocumentReference docIdRef = db.collection("users").document(userId);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        //It Exist
                        Log.d(TAG, "ID Exists, Skipping this Activity");
                        Toast.makeText(RegisterActivity.this, "ID Exists", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //Does not exist
                        Log.d(TAG, userId);
                        Log.d(TAG, "ID Doesn't exists, continuing registration");
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(edtFirstName != null || edtLastName != null || edtEmail != null || edtPhone != null)
                                    createUser(edtFirstName.getText().toString(), edtLastName.getText().toString(), edtEmail.getText().toString(), edtPhone.getText().toString());
                                else
                                    Toast.makeText(RegisterActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
//        CollectionReference uidRef = db.collection("users");
//        Query query = uidRef.whereEqualTo("UID", userId);
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()) {
//                    for(DocumentSnapshot documentSnapshot : task.getResult()) {
//                        String uid = documentSnapshot.getString("UID");
//
//                        if(uid.equals(userId)) {
//                            Log.d(TAG, "ID Exists, Skipping this Activity");
//                            Toast.makeText(RegisterActivity.this, "ID Exists", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                }
//
//                if(task.getResult().size() == 0) {
//                    Log.d(TAG, userId);
//                    Log.d(TAG, "ID Doesn't exists, continuing registration");
//                    btnSubmit.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(edtFirstName != null || edtLastName != null || edtEmail != null || edtPhone != null)
//                                createUser(edtFirstName.getText().toString(), edtLastName.getText().toString(), edtEmail.getText().toString(), edtPhone.getText().toString());
//                            else
//                                Toast.makeText(RegisterActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
    }

    public void createUser(String firstName, String lastName, String email, String phone) {
        Map<String, Object> user = new HashMap<>();
        user.put("first", firstName);
        user.put("last", lastName);
        user.put("email", email);
        user.put("phone", phone);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + userId);
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}