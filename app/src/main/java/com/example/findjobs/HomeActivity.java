package com.example.findjobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(authStateListener);
    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if(firebaseUser == null) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fAuth.removeAuthStateListener(authStateListener);
    }

    FirstFragment firstFragment = new FirstFragment();
    HomeFragment homeFragment = new HomeFragment();
    AccountFragment accountFragment = new AccountFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.person:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, firstFragment).commit();
                return true;

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                return true;

            case R.id.setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, accountFragment).commit();
                return true;
        }
        return false;
    }

    public void openActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openEditProfileActivity(View view) {
        String userId = fAuth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        String first = document.getString("first");
                        String last = document.getString("last");
                        String email = document.getString("email");
                        String phone = document.getString("phone");
                        String address1 = document.getString("address1");
                        String address2 = document.getString("address2");

                        Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                        intent.putExtra("first", first);
                        intent.putExtra("last", last);
                        intent.putExtra("email", email);
                        intent.putExtra("phone", phone);
                        intent.putExtra("address1", address1);
                        intent.putExtra("address2", address2);

                        startActivity(intent);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Get failed with", task.getException());
                }
            }
        });
//        Intent intent = new Intent(this, EditProfileActivity.class);
//        startActivity(intent);
    }

//    public ArrayList<String> getUserDetails() {
//        ArrayList<String> userDetails = new ArrayList<>();
//
//        String userId = fAuth.getCurrentUser().getUid();
//
//        DocumentReference docRef = db.collection("users").document(userId);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if(document.exists()) {
//                        String first = document.getString("first");
//                        String last = document.getString("last");
//                        String email = document.getString("email");
//                        String address1 = document.getString("address1");
//                        String address2 = document.getString("address2");
//
//                        userDetails.add(first);
//                        userDetails.add(last);
//                        userDetails.add(email);
//                        userDetails.add(address1);
//                        userDetails.add(address2);
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "Get failed with", task.getException());
//                }
//            }
//        });
//        return userDetails;
//    }
}