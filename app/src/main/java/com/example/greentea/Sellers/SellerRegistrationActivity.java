package com.example.greentea.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greentea.Buyers.JoinForm;
import com.example.greentea.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
    private Button sellerLoginBegin;
    private EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private TextView back_to_home_seller_registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        sellerLoginBegin = findViewById(R.id.seller_already_have_account_btn);
        registerButton = findViewById(R.id.seller_register_btn);
        nameInput = findViewById(R.id.seller_name);
        phoneInput = findViewById(R.id.seller_phone);
        emailInput = findViewById(R.id.seller_email);
        passwordInput = findViewById(R.id.seller_password);
        addressInput = findViewById(R.id.seller_address);
        sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class));
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
        back_to_home_seller_registration = findViewById(R.id.back_to_home_seller_registration);
        back_to_home_seller_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerRegistrationActivity.this, JoinForm.class));
            }
        });
    }

    private void registerSeller(){
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String address = addressInput.getText().toString();
        if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals("")){
            loadingBar.setTitle("Create Seller Account");
            loadingBar.setMessage("Please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        String sid = mAuth.getCurrentUser().getUid();
                        HashMap<String, Object> sellerMap = new HashMap<>();
                        sellerMap.put("sid", sid);
                        sellerMap.put("phone", phone);
                        sellerMap.put("email", email);
                        sellerMap.put("address", address);
                        sellerMap.put("name", name);
                        rootRef.child("Sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                loadingBar.dismiss();
                                                Toast.makeText(SellerRegistrationActivity.this, "Register successfully. Please check your email for verification", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(SellerRegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(SellerRegistrationActivity.this, "This email had already exist", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Please complete the registration form", Toast.LENGTH_SHORT).show();
        }
    }
}