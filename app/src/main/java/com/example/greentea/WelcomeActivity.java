package com.example.greentea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.greentea.Buyers.JoinForm;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private TextView[] dotsTV;
    private int[] layouts;
    private Button btnSkip;
    private Button btnNext;
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isFirstTimeStartApp()){
            startMainActivity();
            finish();
        }
        setStatusBarTransparent();
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        viewPager = findViewById(R.id.view_pager_welcome);
        layoutDot = findViewById(R.id.dotLayout);
        btnSkip = findViewById(R.id.btn_skip_welcome);
        btnNext = findViewById(R.id.btn_next_welcome);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage = viewPager.getCurrentItem() + 1;
                if(currentPage < layouts.length){
                    viewPager.setCurrentItem(currentPage);
                }
                else{
                    startMainActivity();
                }
            }
        });
        layouts = new int[]{R.layout.slider_1, R.layout.slider_2, R.layout.slider_3};
        pagerAdapter = new MyPagerAdapter(layouts, getApplicationContext());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == layouts.length - 1){
                    btnNext.setText("START");
                    btnSkip.setVisibility(View.GONE);
                    layoutDot.setVisibility(View.GONE);
                }
                else{
                    btnNext.setText("NEXT");
                    btnSkip.setVisibility(View.VISIBLE);
                    layoutDot.setVisibility(View.VISIBLE);
                }
                setDotStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);
    }

    private boolean isFirstTimeStartApp(){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("EcommerceApp", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlag", true);
    }

    private void setFirstTimeStartStatus(boolean stt){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("EcommerceApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlag", stt);
        editor.commit();
    }

    private void setDotStatus(int page){
        layoutDot.removeAllViews();
        dotsTV = new TextView[layouts.length];
        for(int i = 0; i < dotsTV.length; i++){
            dotsTV[i] = new TextView(this);
            dotsTV[i].setText(Html.fromHtml("&#8226;"));
            dotsTV[i].setTextSize(30);
            dotsTV[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDot.addView(dotsTV[i]);
        }
        //set current dot active
        if(dotsTV.length > 0){
            dotsTV[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void startMainActivity(){
        setFirstTimeStartStatus(false);
        startActivity(new Intent(WelcomeActivity.this, JoinForm.class));
        finish();
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