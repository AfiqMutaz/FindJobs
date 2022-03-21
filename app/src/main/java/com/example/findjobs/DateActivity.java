package com.example.findjobs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class DateActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button btnDate, btnDateAlternate, btnNext;

    private static final String TAG = DateActivity.class.getSimpleName();

    private int hour;
    private int min;
    private String serviceType;

    private String mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        Intent intent = getIntent();
        int viewId = intent.getIntExtra("viewId", 0);

        switch (viewId) {
            case 2131231198:
                serviceType = "Electrician";
                //setContentView(R.layout.layout_cleaning);
                break;
            case 2131231197:
                serviceType = "Cleaning";
                //setContentView(R.layout.layout_plumbing);
                break;
            default:
                serviceType = null;
                //setContentView(R.layout.activity_date);
                break;

        }

        initDatePicker();
        btnDate = findViewById(R.id.idBtnDatePicker);
        btnDateAlternate = findViewById(R.id.idBtnDatePickerAlternate);
        Log.d(TAG, getDateToday());
        btnDate.setText(getDateToday());
        btnDateAlternate.setText(getDateToday());

        btnNext = findViewById(R.id.idBtnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                intent.putExtra("ServiceType", serviceType);
                intent.putExtra("DateTime", btnDate.getText().toString());
                intent.putExtra("DateTimeAlternate", btnDateAlternate.getText().toString());

                startActivity(intent);
            }
        });
    }

    private String getDateToday() {
        Calendar cal = Calendar.getInstance();

        String DateTime;
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        month = month + 1;

        if (min<10) {
            DateTime = makeDateString(day, month, year) + " " + hour + ":0" + min;
        } else {
            DateTime = makeDateString(day, month, year) + " " + hour + ":" + min;
        }

        return DateTime;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                mDate = date;
                openTimePicker();
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //int style = R.style.DialogTheme;

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);

        //datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
//        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
        //datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.quantum_googgreen400, null));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return dayOfMonth + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "ABC";
        }
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void openTimePicker() {
        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                hour = hourOfDay;
                min = minute;

                btnDate.setText(mDate + " " + hour + ":" + min);
            }
        }, hour, min, false);
        timePickerDialog.show();
    }
}