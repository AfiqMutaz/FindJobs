package com.example.findjobs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirstFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public static final String TAG = FirstFragment.class.getSimpleName();

    private boolean userType;

    private ArrayList<Jobs> jobsArrayList;

    private Jobs jobs;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(userType)
            return inflater.inflate(R.layout.fragment_first, container, false);
        else
            return inflater.inflate(R.layout.fragment_first_jobs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        jobsArrayList = new ArrayList<>();
//        JobsAdapter adapter = new JobsAdapter(getContext(), jobsArrayList);

//        loadDataInListView();

        db.collection("jobs")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
//                                    Map<String, Object> list = dc.getDocument().getData();
//                                    Jobs jobs = new Jobs(dc.getDocument().getId(),
//                                            String.valueOf(list.get("userId")),
//                                            String.valueOf(list.get("serviceType")),
//                                            String.valueOf(list.get("dateTime")),
//                                            String.valueOf(list.get("dateTimeAlt")),
//                                            String.valueOf(list.get("duration")),
//                                            String.valueOf(list.get("numCleaner")),
//                                            String.valueOf(list.get("totalPrice")),
//                                            Boolean.parseBoolean(String.valueOf(list.get("isAccepted"))),
//                                            Boolean.parseBoolean(String.valueOf(list.get("isSupplied"))));
//                                    jobsArrayList.add(jobs);
//                                    Log.d(TAG, String.valueOf(list.get("serviceType")));
                                    jobs = dc.getDocument().toObject(Jobs.class);
                                    jobsArrayList.add(jobs);
                                    Log.d(TAG, "New Job: [ID=" + dc.getDocument().getId() + "] " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified Job: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    jobs = dc.getDocument().toObject(Jobs.class);
                                    jobsArrayList.add(jobs);
                                    Log.d(TAG, "Removed Job: " + dc.getDocument().getData());
                                    break;
                            }

                            ListView lvJobs = getView().findViewById(R.id.idLVJobs);
                            ArrayAdapter adapter = new JobsAdapter(getContext(), jobsArrayList);
                            lvJobs.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        //loadDataInListView();
    }

    public void loadDataInListView() {
        db.collection("jobs")
                .whereEqualTo("isAccepted", false)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Jobs jobs = d.toObject(Jobs.class);
                                jobsArrayList.add(jobs);
                            }
                            JobsAdapter adapter = new JobsAdapter(getContext(), jobsArrayList);
                            //lvJobs.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Fail to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean getUserType() {
        String userId = fAuth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        userType = document.getBoolean("isProvider");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Get failed with", task.getException());
                }
            }
        });
        return userType;
    }

    public void restartFragment() {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.flFragment);

        if(currentFragment instanceof FirstFragment) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
        }
    }
}