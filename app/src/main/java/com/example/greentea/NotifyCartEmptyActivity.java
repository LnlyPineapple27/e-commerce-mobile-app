package com.example.greentea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.greentea.Buyers.HomeActivity;

public class NotifyCartEmptyActivity extends AppCompatActivity {
    private Button continueShoppingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_cart_empty);
        continueShoppingBtn = findViewById(R.id.btn_continue_shopping);
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotifyCartEmptyActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}