package com.example.findjobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int viewId = intent.getIntExtra("viewId", 0);

        switch (viewId) {
            case 2131231198:
                setContentView(R.layout.activity_order);
                break;
            case 2131231197:
                //setContentView(R.layout.test_layout);
                break;
        }
    }
}