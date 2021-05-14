package com.example.greentea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.greentea.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<String> historyOrderName = new ArrayList<>();
    ArrayList<String> historyOrderTotalAmount = new ArrayList<>();
    ArrayList<String> historyOrderListProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        recyclerView = findViewById(R.id.history_order_recyclerview);
        DatabaseReference historyOrderRef = FirebaseDatabase.getInstance().getReference().child("All Orders Shipped").child(Prevalent.currentOnlineUser.getPhone());
        historyOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String val1 = ds.child("productName").getValue(String.class);
                    String val2 = ds.child("totalAmount").getValue(String.class);
                    String val3 = ds.child("listProductBuy").getValue(String.class);
                    historyOrderName.add(val1);
                    historyOrderTotalAmount.add(val2);
                    historyOrderListProducts.add(val3);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                historyOrderAdapter adapter = new historyOrderAdapter(HistoryOrderActivity.this, historyOrderName, historyOrderTotalAmount, historyOrderListProducts);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}