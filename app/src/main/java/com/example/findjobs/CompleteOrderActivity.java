package com.example.findjobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CompleteOrderActivity extends AppCompatActivity {

    private String serviceType, dateTime, dateTimeAlt, spinSupplies, spinNumCleaner, spinDuration;
    private TextView tvServiceType, tvDateTime, tvDateTimeAlt, tvSupplies, tvNumCleaner, tvDuration, tvBasePrice, tvSuppliesPrice, tvAddCleanersPrice, tvDurationPrice, tvTotalPrice;
    private float numCleaner, numDuration;
    private Button btnConfirmPay;

    private static final String TAG = CompleteOrderActivity.class.getSimpleName();

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            numDuration = Float.parseFloat(spinDuration);

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

                String formatDateTime = dateTime.replaceAll("[\\s\\:]", "");
                String formatDateTimeAlt = dateTimeAlt.replaceAll("[\\s\\:]", "");

                boolean supplies;
                if (spinSupplies.equals("Yes"))
                    supplies = true;
                else
                    supplies = false;

                createJob(serviceType, formatDateTime, formatDateTimeAlt, supplies, df.format(numCleaner + 1), df.format(numDuration + 1), df.format(totalPrice));
            }
        });
    }

    public void createJob(String st, String dt, String dta, boolean supplied, String nc, String d, String tp) {
        String userId = fAuth.getCurrentUser().getUid();

        Map<String, Object> data = new HashMap<>();
        data.put("serviceType", st);
        data.put("dateTime", dt);
        data.put("dateTimeAlt", dta);
        data.put("isSupplied", supplied);
        data.put("numCleaner", nc);
        data.put("duration", d);
        data.put("userId", userId);
        data.put("isAccepted", false);
        data.put("totalPrice", tp);

        db.collection("jobs")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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

//    public String numberOfMonth(String month) {
//        switch(month) {
//            case "JAN":
//                return "1";
//            case "FEB":
//                return "2";
//            case "MAR":
//                return "3";
//            case "APR":
//                return "4";
//            case "MAY":
//                return "5";
//            case "JUN":
//                return "6";
//            case "JUL":
//                return "7";
//            case "AUG":
//                return "8";
//            case "SEP":
//                return "9";
//            case "OCT":
//                return "10";
//            case "NOV":
//                return "11";
//            case "DEC":
//                return "12";
//            default:
//                return null;
//        }
//    }
}