package com.example.greentea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.greentea.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewOrderBuyerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<String> viewOrderDate = new ArrayList<>();
    ArrayList<String> viewOrderTime = new ArrayList<>();
    ArrayList<String> viewOrderName = new ArrayList<>();
    ArrayList<String> viewOrderPhone = new ArrayList<>();
    ArrayList<String> viewOrderTotalAmount = new ArrayList<>();
    ArrayList<String> viewOrderListProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_buyer);
        recyclerView = findViewById(R.id.view_multi_order_recyclerview);
        DatabaseReference viewOrderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        viewOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("current_phone_db").getValue(String.class).equals(Prevalent.currentOnlineUser.getPhone())){
                        String val1 = ds.child("date").getValue(String.class);
                        String val2 = ds.child("time").getValue(String.class);
                        String val3 = ds.child("name").getValue(String.class);
                        String val4 = ds.child("phone").getValue(String.class);
                        String val5 = ds.child("totalAmount").getValue(String.class);
                        String val6 = ds.child("listProducts").getValue(String.class);
                        viewOrderDate.add(val1);
                        viewOrderTime.add(val2);
                        viewOrderName.add(val3);
                        viewOrderPhone.add(val4);
                        viewOrderTotalAmount.add(val5);
                        viewOrderListProducts.add(val6);
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                viewOrderBuyerAdapter adapter = new viewOrderBuyerAdapter(ViewOrderBuyerActivity.this, viewOrderDate, viewOrderTime,
                        viewOrderName, viewOrderPhone, viewOrderTotalAmount, viewOrderListProducts);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}