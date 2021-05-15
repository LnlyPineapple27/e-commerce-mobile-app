package com.example.greentea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.greentea.Buyers.HomeActivity;
import com.example.greentea.Buyers.SearchProductsActivity;

public class MenuCategoryActivity extends AppCompatActivity {
    private ImageView categoryBack, categorySearch, hero1, hero2, hero3, hero4, hero5, hero6, hero7, hero8, hero9, hero10, hero11, hero12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_category);
        getSupportActionBar().hide();
        categoryBack = findViewById(R.id.category_back_iv);
        categorySearch = findViewById(R.id.category_search_iv);
        hero1 = findViewById(R.id.hero1);
        hero2 = findViewById(R.id.hero2);
        hero3 = findViewById(R.id.hero3);
        hero4 = findViewById(R.id.hero4);
        hero5 = findViewById(R.id.hero5);
        hero6 = findViewById(R.id.hero6);
        hero7 = findViewById(R.id.hero7);
        hero8 = findViewById(R.id.hero8);
        hero9 = findViewById(R.id.hero9);
        hero10 = findViewById(R.id.hero10);
        hero11 = findViewById(R.id.hero11);
        hero12= findViewById(R.id.hero12);
        categoryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, HomeActivity.class));
            }
        });
        categorySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, SearchProductsActivity.class));
            }
        });
        hero1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity1.class));
            }
        });
        hero2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity2.class));
            }
        });
        hero3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity3.class));
            }
        });
        hero4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity4.class));
            }
        });
        hero5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity5.class));
            }
        });
        hero6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity7.class));
            }
        });
        hero7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity6.class));
            }
        });
        hero8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity8.class));
            }
        });
        hero9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity9.class));
            }
        });
        hero10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity10.class));
            }
        });
        hero11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity11.class));
            }
        });
        hero12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuCategoryActivity.this, CategoryActivity12.class));
            }
        });
    }
}