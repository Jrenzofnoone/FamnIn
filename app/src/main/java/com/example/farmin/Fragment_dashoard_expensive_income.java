package com.example.farmin;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Fragment_dashoard_expensive_income extends Fragment {
    private TextView addExpenseButton, addIncome;
    private RecyclerView recyclerView_show_expenses_income;
    private List<objectIncome> uploads;
    private MyAdapter adapter ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_expensive_income, container, false);

        recyclerView_show_expenses_income = view.findViewById(R.id.recyclerView_show_expenses_income);
        uploads = new ArrayList<>();

        // Find TextViews
        addExpenseButton = view.findViewById(R.id.addExpensive);
        addIncome = view.findViewById(R.id.addIncome);
        adapter = new MyAdapter(uploads, getActivity());
        recyclerView_show_expenses_income.setHasFixedSize(true);
        recyclerView_show_expenses_income.setLayoutManager(new LinearLayoutManager(getActivity()));
        DatabaseReference incomeRef = FirebaseDatabase.getInstance().getReference("income");
        DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference("expenses");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploads.clear();
                for(DataSnapshot post: snapshot.getChildren()){
                    objectIncome objectIncome = post.getValue(com.example.farmin.objectIncome.class);
                    uploads.add(objectIncome);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploads.clear();
                for(DataSnapshot post: snapshot.getChildren()){
                    objectIncome objectIncome = post.getValue(com.example.farmin.objectIncome.class);
                    uploads.add(objectIncome);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView_show_expenses_income.setAdapter(adapter);

        // Set click listener for Add Expense button
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open expense dialog fragment
                openExpenseDialog();
            }
        });

        // Set click listener for Add Income button
        addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open income dialog fragment
                openIncomeDialog();
            }
        });

        return view;
    }

    private void openExpenseDialog() {
        ExpenseDialogFragment dialog = new ExpenseDialogFragment();
        dialog.show(getChildFragmentManager(), "ExpenseDialogFragment");
    }

    private void openIncomeDialog() {
        IncomeDialogFragment dialog = new IncomeDialogFragment();
        dialog.show(getChildFragmentManager(), "IncomeDialogFragment");
    }
}
