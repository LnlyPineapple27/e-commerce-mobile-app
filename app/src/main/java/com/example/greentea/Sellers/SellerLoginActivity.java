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

public class SellerLoginActivity extends AppCompatActivity {
    private Button loginSellerBtn, seller_not_have_account_btn;
    private EditText emailInput, passwordInput;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private TextView forgetPasswordSeller, back_to_home_seller_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.seller_login_email);
        passwordInput = findViewById(R.id.seller_login_password);
        loginSellerBtn = findViewById(R.id.seller_login_btn);
        loadingBar = new ProgressDialog(this);
        loginSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });
        forgetPasswordSeller = findViewById(R.id.seller_forget_password);
        
        back_to_home_seller_login = findViewById(R.id.back_to_home_seller_login);
        back_to_home_seller_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerLoginActivity.this, JoinForm.class));
            }
        });
        seller_not_have_account_btn = findViewById(R.id.seller_not_have_account_btn);

    }

    private void loginSeller(){
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();
        if(!email.equals("") && !password.equals("")) {
            loadingBar.setTitle("Seller Account Login");
            loadingBar.setMessage("Please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            finish();
                        }
                        else{
                            Toast.makeText(SellerLoginActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                    else{
                        Toast.makeText(SellerLoginActivity.this, "Your user name or password is not correct", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Please complete the login form", Toast.LENGTH_SHORT).show();
        }
    }
}