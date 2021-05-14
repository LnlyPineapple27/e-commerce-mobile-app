package com.example.greentea.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greentea.Prevalent.Prevalent;
import com.example.greentea.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderBtn;
    private String totalAmount = "";
    //
    String tmpResNameProduct = "";
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        getSupportActionBar().hide();
        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = $" + totalAmount, Toast.LENGTH_SHORT).show();
        confirmOrderBtn = findViewById(R.id.confirm_final_order_btn);
        nameEditText = findViewById(R.id.shipment_name);
        phoneEditText = findViewById(R.id.shipment_phone_number);
        addressEditText = findViewById(R.id.shipment_address);
        cityEditText = findViewById(R.id.shipment_city);
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
        //
        ArrayList<String> tmpStoreProduct = (ArrayList<String>) getIntent().getSerializableExtra("totalListProduct");
        for(int i = 0; i < tmpStoreProduct.size(); i++){
            tmpResNameProduct += tmpStoreProduct.get(i) + ", ";
        }
        //
    }

    private void Check(){
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "Please provide your full name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(this, "Please provide your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "Please provide your address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(this, "Please provide your city name", Toast.LENGTH_SHORT).show();
        }
        else{
            ConfirmOrder();
        }
    }

    //
    private String randomKeyOrder(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
    //

    private void ConfirmOrder(){
        final String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        //final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        //
        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(randomKeyOrder(10));
        //
        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");
        //
        ordersMap.put("current_phone_db", Prevalent.currentOnlineUser.getPhone());
        ordersMap.put("listProducts", tmpResNameProduct.substring(0, tmpResNameProduct.length() - 2));
        //
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ConfirmFinalOrderActivity.this, "Your order has been placed successfully!!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}