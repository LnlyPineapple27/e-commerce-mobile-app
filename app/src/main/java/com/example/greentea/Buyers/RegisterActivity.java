package com.example.greentea.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greentea.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword, confirmInputPassword;
    private ProgressDialog loadingBar;
    private TextView txtTermOfUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        setStatusBarTransparent();
        CreateAccountButton = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.register_username_input);
        InputPhoneNumber = findViewById(R.id.register_phone_number_input);
        InputPassword = findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
        //highlight term of use
        txtTermOfUse = findViewById(R.id.txtTermOfUse);
        SpannableString ss = new SpannableString("By registering, you are agreeing to the Green Tea Terms of Use and Privacy Policy");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RegisterActivity.this);
                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.term_of_use_bottom_sheet_layout, findViewById(R.id.bottom_sheet_term_of_use));
                sheetView.findViewById(R.id.close_and_close1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(sheetView);
                DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
                bottomSheetDialog.getBehavior().setPeekHeight((int)(displayMetrics.heightPixels * 1.0));
                bottomSheetDialog.show();
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(RegisterActivity.this);
                View sheetView1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.privacy_policy_bottom_sheet_layout, findViewById(R.id.bottom_sheet_privacy_policy));
                sheetView1.findViewById(R.id.close_and_close2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog1.dismiss();
                    }
                });
                bottomSheetDialog1.setContentView(sheetView1);
                DisplayMetrics displayMetrics1 = getApplicationContext().getResources().getDisplayMetrics();
                bottomSheetDialog1.getBehavior().setPeekHeight((int)(displayMetrics1.heightPixels * 1.0));
                bottomSheetDialog1.show();
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan1, 50, 62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 67, 81, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtTermOfUse.setText(ss);
        txtTermOfUse.setMovementMethod(LinkMovementMethod.getInstance());
        txtTermOfUse.setHighlightColor(Color.TRANSPARENT);
        //highlight term of use
        confirmInputPassword = findViewById(R.id.confirm_register_password_input);
    }

    private void CreateAccount(){
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        String confirm_password = confirmInputPassword.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please input your name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please input your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please input your password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirm_password)){
            Toast.makeText(this, "Please input your confirm password", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirm_password)){
            Toast.makeText(this, "Your password doesn't match your confirm password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidatephoneNumber(name, phone, password);
        }
    }

    private void ValidatephoneNumber(String name, String phone, String password) {
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);
                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Create account successfully !!!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                            else{
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "This phone number " + phone + " has already existed", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again with another phone number", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, JoinForm.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setStatusBarTransparent(){
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}