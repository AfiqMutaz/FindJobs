package com.example.findjobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String serviceType, dateTime, dateTimeAlternate;
    private TextView tvServiceType, tvDateTime, tvDateTimeAlternate;
    private Spinner spinSupplies, spinNumCleaner, spinDuration;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        serviceType = intent.getStringExtra("ServiceType");
        dateTime = intent.getStringExtra("DateTime");
        dateTimeAlternate = intent.getStringExtra("DateTimeAlternate");

        switch (serviceType) {
            case "Cleaning":
                setContentView(R.layout.layout_cleaning);
                break;
            case "Electrician":
                setContentView(R.layout.layout_electrician);
                break;
            default:
                setContentView(R.layout.activity_order);
                break;

        }

        //tvServiceType = findViewById(R.id.idTvServiceType);
        //tvDateTime = findViewById(R.id.idTvServiceDateTime);
        //tvDateTimeAlternate = findViewById(R.id.idTvServiceDateTimeAlt);

        //tvServiceType.setText(serviceType);
        //tvDateTime.setText(dateTime);
        //tvDateTimeAlternate.setText(dateTimeAlternate);

        spinSupplies = findViewById(R.id.idSpinCleanSupplies);
        spinNumCleaner = findViewById(R.id.idSpinNumberCleaners);
        spinDuration = findViewById(R.id.idSpinDuration);

        ArrayAdapter<CharSequence> adapterYesNo = ArrayAdapter.createFromResource(this, R.array.yes_no, android.R.layout.simple_spinner_item);
        adapterYesNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterOneThree = ArrayAdapter.createFromResource(this, R.array.one_three, android.R.layout.simple_spinner_item);
        adapterOneThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinSupplies.setAdapter(adapterYesNo);
        spinNumCleaner.setAdapter(adapterOneThree);
        spinDuration.setAdapter(adapterOneThree);

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.yes_no, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinSupplies.setAdapter(adapter);

        btnNext = findViewById(R.id.idBtnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompleteOrderActivity.class);
                intent.putExtra("ServiceType", serviceType);
                intent.putExtra("DateTime", dateTime);
                intent.putExtra("DateTimeAlternate", dateTimeAlternate);
                intent.putExtra("SpinSupplies", spinSupplies.getSelectedItem().toString());
                intent.putExtra("SpinNumCleaner", spinNumCleaner.getSelectedItem().toString());
                intent.putExtra("SpinDuration", spinDuration.getSelectedItem().toString());

                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //An item was selected. You can retrieve the selected item using
        //parent.getItemAtPosition(pos)
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Another interface callback
    }
}