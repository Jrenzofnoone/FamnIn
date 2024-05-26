package com.example.farmin;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
public class Fragment_dashoard_expensive_income extends Fragment {
    private TextView addExpenseButton, addIncome, tv_income_total, tv_expenses_total;
    private RecyclerView recyclerView_show_expenses_income;
    private List<objectIncome> incomeList;
    private List<objectIncome> expenseList;
    private MyAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_expensive_income, container, false);


        recyclerView_show_expenses_income = view.findViewById(R.id.recyclerView_show_expenses_income);
        tv_income_total = view.findViewById(R.id.tv_income_total);
        tv_expenses_total = view.findViewById(R.id.tv_expenses_total);
        addExpenseButton = view.findViewById(R.id.addExpensive);
        addIncome = view.findViewById(R.id.addIncome);
        incomeList = new ArrayList<>();
        expenseList = new ArrayList<>();
        adapter = new MyAdapter(new ArrayList<>(), getActivity());
        recyclerView_show_expenses_income.setHasFixedSize(true);
        recyclerView_show_expenses_income.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_show_expenses_income.setAdapter(adapter);
        DatabaseReference incomeRef = FirebaseDatabase.getInstance().getReference("INCOME");
        DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference("EXPENSES");
        fetchAndDisplayData(incomeRef, expenseRef);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExpenseDialog();
            }
        });
        addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIncomeDialog();
            }
        });
        return view;
    }
    private void fetchAndDisplayData(DatabaseReference incomeRef, DatabaseReference expenseRef) {
        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                incomeList.clear();
                for (DataSnapshot post : snapshot.getChildren()) {
                    objectIncome income = post.getValue(objectIncome.class);
                    incomeList.add(income);
                }
                updateTotals();
                updateRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if necessary
            }
        });
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expenseList.clear();
                for (DataSnapshot post : snapshot.getChildren()) {
                    objectIncome expense = post.getValue(objectIncome.class);
                    expenseList.add(expense);
                }
                updateTotals();
                updateRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if necessary
            }
        });
    }
    private void updateRecyclerView() {
        List<objectIncome> combinedList = new ArrayList<>();
        combinedList.addAll(incomeList);
        combinedList.addAll(expenseList);
        adapter.updateData(combinedList);
    }
    private void updateTotals() {
        double totalIncome = calculateTotal(incomeList);
        double totalExpenses = calculateTotal(expenseList);
        tv_income_total.setText(String.valueOf(totalIncome));
        tv_expenses_total.setText(String.valueOf(totalExpenses));
    }
    private double calculateTotal(List<objectIncome> list) {
        double total = 0;
        for (objectIncome item : list) {
            total += item.getAmountAsDouble(); // Assuming objectIncome has a method getAmountAsDouble()
        }
        return total;
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