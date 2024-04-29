package com.example.farmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragment_crop extends Fragment implements clickInterface{
    private RecyclerView recyclerView;
    private List<addingUploads> uploads;
    private AllAdapter adapter;
    private FirebaseUser user;
    private DatabaseReference cropRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crop, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        uploads = new ArrayList<>();
        adapter = new AllAdapter(getActivity(),1, uploads, this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        cropRef = FirebaseDatabase.getInstance().getReference("Crops");
        cropRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recyclerView.setAdapter(adapter);
                for( DataSnapshot postSnap : snapshot.getChildren()) {
                    addingUploads addingUploads = postSnap.getValue(com.example.farmin.addingUploads.class);
                    if(user.getEmail().equals(addingUploads.getUserEmail())) {
                        uploads.add(addingUploads);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return rootView;
    }

    @Override
    public void setItemClick(int position) {

    }
}