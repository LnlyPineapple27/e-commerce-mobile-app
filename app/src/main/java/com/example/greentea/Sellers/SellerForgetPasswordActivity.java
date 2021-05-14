package com.example.greentea.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greentea.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SellerForgetPasswordActivity extends AppCompatActivity {
    private Button btn_seller_reset_password;
    private EditText input_email_seller_reset_password;
    private TextView back_seller_reset_password;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_forget_password);
        input_email_seller_reset_password = findViewById(R.id.input_email_seller_reset_password);
        btn_seller_reset_password = findViewById(R.id.btn_reset_password_seller);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_seller_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_email_seller_reset_password.getText().toString().equals("")){
                    Toast.makeText(SellerForgetPasswordActivity.this, "Email can't be blank", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(input_email_seller_reset_password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(SellerForgetPasswordActivity.this, SellerLoginActivity.class));
                                        finish();
                                        Toast.makeText(SellerForgetPasswordActivity.this, "Please check your email for reset password", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SellerForgetPasswordActivity.this, "This email hasn't existed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        back_seller_reset_password = findViewById(R.id.back_seller_reset_password);
        back_seller_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerForgetPasswordActivity.this, SellerLoginActivity.class));
            }
        });
    }
}