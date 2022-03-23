package com.example.findjobs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class JobsAdapter extends ArrayAdapter<Jobs> {
    public JobsAdapter(@NonNull Context context, @NonNull List<Jobs> jobsArrayList) {
        super(context, 0, jobsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if(listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_cardview_jobs, parent, false);
        }

        Jobs jobs = getItem(position);

        TextView tvJobsTitle = listitemView.findViewById(R.id.idTvJobServiceType);
        TextView tvJobsDateTime = listitemView.findViewById(R.id.idTvJobDateTime);
        TextView tvJobsDateTimeAlt = listitemView.findViewById(R.id.idTvJobDateTimeAlt);
        TextView tvJobsDuration = listitemView.findViewById(R.id.idTvJobDuration);
        TextView tvJobsNumCleaner = listitemView.findViewById(R.id.idTvJobNumCleaner);
        TextView tvJobsIsSupplied = listitemView.findViewById(R.id.idTvJobIsSupplied);
        TextView tvJobsTotalPrice = listitemView.findViewById(R.id.idTvJobTotalPrice);
        //ImageView ivJobsImage = listitemView.findViewById(R.id.idIvImage);

        tvJobsTitle.setText(jobs.getServiceType());
        tvJobsDateTime.setText(jobs.getDateTime());
        tvJobsDateTimeAlt.setText(jobs.getDateTimeAlt());
        tvJobsDuration.setText(jobs.getDuration());
        tvJobsNumCleaner.setText(jobs.getNumCleaner());
        tvJobsIsSupplied.setText(String.valueOf(jobs.getIsSupplied()));
        tvJobsTotalPrice.setText(jobs.getTotalPrice());

        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Item clicked is: " + jobs.getServiceType(), Toast.LENGTH_SHORT).show();
            }
        });
        return listitemView;
    }
}
