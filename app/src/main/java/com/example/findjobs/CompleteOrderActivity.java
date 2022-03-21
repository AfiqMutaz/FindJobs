package com.example.findjobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CompleteOrderActivity extends AppCompatActivity {

    private String serviceType, dateTime, dateTimeAlt, spinSupplies, spinNumCleaner, spinDuration;
    private TextView tvServiceType, tvDateTime, tvDateTimeAlt, tvSupplies, tvNumCleaner, tvDuration, tvBasePrice, tvSuppliesPrice, tvAddCleanersPrice, tvDurationPrice, tvTotalPrice;
    private float numCleaner, numDuration;
    private Button btnConfirmPay;

    private final static float basePrice = 30;
    private final static float addCleaner = 15;
    private final static float addSupplies = 5;
    private final static float addDuration = 5;

    private static final DecimalFormat df = new DecimalFormat("0.00");

//    Intent intent = new Intent(getApplicationContext(), CompleteOrderActivity.class);
//                intent.putExtra("ServiceType", serviceType);
//                intent.putExtra("DateTime", dateTime);
//                intent.putExtra("DateTimeAlternate", dateTimeAlternate);
//                intent.putExtra("SpinSupplies", spinSupplies.getSelectedItem().toString());
//                intent.putExtra("SpinNumCleaner", spinNumCleaner.getSelectedItem().toString());
//                intent.putExtra("SpinDuration", spinDuration.getSelectedItem().toString());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);

        Intent intent = getIntent();

        serviceType = intent.getStringExtra("ServiceType");
        dateTime = intent.getStringExtra("DateTime");
        dateTimeAlt = intent.getStringExtra("DateTimeAlternate");
        spinSupplies = intent.getStringExtra("SpinSupplies");

        spinNumCleaner = intent.getStringExtra("SpinNumCleaner");
        if(spinNumCleaner.equals("No"))
            numCleaner = 0;
        else
            numCleaner = Float.parseFloat(spinNumCleaner);

        spinDuration = intent.getStringExtra("SpinDuration");
        if(spinDuration.equals("No"))
            numDuration = 0;
        else
            numDuration = Float.parseFloat(spinNumCleaner);

        tvServiceType = findViewById(R.id.idTvService);
        tvDateTime = findViewById(R.id.idTvBookingDate);
        tvDateTimeAlt = findViewById(R.id.idTvBookingDateAlt);
        tvSupplies = findViewById(R.id.idTvSupplies);
        tvNumCleaner = findViewById(R.id.idTvAddCleaners);
        tvDuration = findViewById(R.id.idTvDuration);

        tvServiceType.setText(serviceType);
        tvDateTime.setText(dateTime);
        tvDateTimeAlt.setText(dateTimeAlt);
        tvSupplies.setText(spinSupplies);
        tvNumCleaner.setText(spinNumCleaner);
        tvDuration.setText(spinDuration);

        tvBasePrice = findViewById(R.id.idTvBasePrice);
        tvSuppliesPrice = findViewById(R.id.idTvSuppliesPrice);
        tvAddCleanersPrice = findViewById(R.id.idTvAddCleanersPrice);
        tvDurationPrice = findViewById(R.id.idTvDurationPrice);
        tvTotalPrice = findViewById(R.id.idTvTotalPrice);

        tvBasePrice.setText("$" + df.format(basePrice));

        float suppliesPrice;
        if(spinSupplies.equals("Yes"))
            suppliesPrice = addSupplies;
        else
            suppliesPrice = 0;

        float cleanerPrice = numCleaner * addCleaner;

        float durationPrice = numDuration * addDuration;

        tvSuppliesPrice.setText("$" + df.format(suppliesPrice));
        tvAddCleanersPrice.setText("$" + df.format(cleanerPrice));
        tvDurationPrice.setText("$" + df.format(durationPrice));

        float totalPrice = basePrice + suppliesPrice + cleanerPrice + durationPrice;

        tvTotalPrice.setText("$" + df.format(totalPrice));

        btnConfirmPay = findViewById(R.id.idBtnConfirmPay);
        btnConfirmPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}