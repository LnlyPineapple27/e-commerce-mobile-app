package com.example.greentea.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.greentea.Model.Products;
import com.example.greentea.Prevalent.Prevalent;
import com.example.greentea.R;
import com.example.greentea.ViewHolder.CommentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state = "Normal";
    //rating and comment system
    private RatingBar ratingBarProduct;
    private Button ratingProductBtn;
    private EditText edit_comment;
    private RatingBar updateRatingBar;
    //check product is favourite or not
    private Toolbar toolbar;
    private boolean isFavouriteProduct = false;
    //show product sales
    private TextView productSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("pid");
        addToCartButton = findViewById(R.id.pd_add_to_cart_button);
        numberButton = findViewById(R.id.number_btn);
        productImage = findViewById(R.id.product_image_details);
        productName = findViewById(R.id.product_name_details);
        productDescription = findViewById(R.id.product_description_details);
        productPrice = findViewById(R.id.product_price_details);
        getProductDetails(productID);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(ProductDetailsActivity.this, "you can purchase more products, once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
                }
                else{
                    addingToCartList();
                }
            }
        });
        //rating and comment system
        ratingProductBtn = findViewById(R.id.rate_product_btn);
        ratingProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
                builder.setTitle("Rating Product");
                builder.setMessage("Please fill information");
                View itemView = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.layout_rating, null);
                ratingBarProduct = itemView.findViewById(R.id.rating_bar_product);
                edit_comment = itemView.findViewById(R.id.comment_product);
                builder.setView(itemView);
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("productRating")
                                .child(Prevalent.currentOnlineUser.getPhone()).child(productID);
                        HashMap<String, Object> userRatingMap = new HashMap<>();
                        userRatingMap.put("rating", ratingBarProduct.getRating());
                        ref.updateChildren(userRatingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProductDetailsActivity.this, "Thanks for your rating", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //update firebase for comment among rating
                        String saveCurrentDate;
                        Calendar calForDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                        saveCurrentDate = currentDate.format(calForDate.getTime());
                        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("commentRating")
                                .child(Prevalent.currentOnlineUser.getPhone()).child(productID);
                        HashMap<String, Object> userCommentMap = new HashMap<>();
                        userCommentMap.put("reviewer", Prevalent.currentOnlineUser.getName());
                        userCommentMap.put("rating", ratingBarProduct.getRating());
                        userCommentMap.put("review", edit_comment.getText().toString());
                        userCommentMap.put("dateComment", saveCurrentDate);
                        commentRef.updateChildren(userCommentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProductDetailsActivity.this, "Thank for your comment", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        updateRatingBar = findViewById(R.id.update_rating_bar);
        DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference().child("productRating");

        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float count = 0;
                long count_vote_1 = 0;
                //int count_star1 = 0, count_star2 = 0, count_star3 = 0, count_star4 = 0, count_star5 = 0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    float total_rating = ds.child(productID).getChildrenCount();
                    count += total_rating;
                    if(ds.child(productID).child("rating").exists()){
                        long tt1 = (long) ds.child(productID).child("rating").getValue();
                        count_vote_1 += tt1;
                        /*
                        if((long)ds.child(productID).child("rating").getValue() == 1) count_star1++;
                        else if((long) ds.child(productID).child("rating").getValue() == 2) count_star2++;
                        else if((long) ds.child(productID).child("rating").getValue() == 3) count_star3++;
                        else if((long) ds.child(productID).child("rating").getValue() == 4) count_star4++;
                        else if((long) ds.child(productID).child("rating").getValue() == 5) count_star5++;
                        */
                    }
                }
                updateRatingBar.setRating(count_vote_1 / count);
                Log.d("tag", "aaa: " + count + " bbb: " + count_vote_1);
                //Log.d("tag", "star 1: " + count_star1 + " star 2: " + count_star2 + " star 3: " + count_star3
                //+ " star 4: " + count_star4 + " star 5: " + count_star5);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //comment system
        RecyclerView cmtRecyclerview;
        cmtRecyclerview = findViewById(R.id.recycler_comment);
        ArrayList<String> cmtName = new ArrayList<>();
        ArrayList<String> cmtDate = new ArrayList<>();
        ArrayList<String> cmt = new ArrayList<>();
        ArrayList<Long> cmtRatingBar = new ArrayList<>();
        DatabaseReference cmtRef = FirebaseDatabase.getInstance().getReference().child("commentRating");
        cmtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child(productID).child("dateComment").exists() || ds.child(productID).child("review").exists()
                    || ds.child(productID).child("reviewer").exists() || ds.child(productID).child("rating").exists()){
                        String val1 = ds.child(productID).child("dateComment").getValue().toString();
                        String val2 = ds.child(productID).child("review").getValue().toString();
                        String val3 = ds.child(productID).child("reviewer").getValue().toString();
                        long val4 = (long) ds.child(productID).child("rating").getValue();
                        cmtRatingBar.add(val4);
                        cmtDate.add(val1);
                        cmt.add(val2);
                        cmtName.add(val3);
                    }
                }
                cmtRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                CommentAdapter commentAdapter = new CommentAdapter(ProductDetailsActivity.this, cmtName, cmtDate, cmt, cmtRatingBar);
                cmtRecyclerview.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        toolbar = findViewById(R.id.nav_act_product_details);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //show product sales
        productSales = findViewById(R.id.product_sales_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_details_main, menu);
        //show red heart if product is added to wishlist
        isFavouriteProduct = false;
        DatabaseReference checkWishlistRef = FirebaseDatabase.getInstance().getReference().child("productWishlist")
                .child(Prevalent.currentOnlineUser.getPhone());
        checkWishlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child(productID).child("isFavourite").exists()){
                        if(snapshot.child(productID).child("isFavourite").getValue().toString().equals("yes")){
                            MenuView.ItemView favourite_btn = findViewById(R.id.action_product_favorite);
                            favourite_btn.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_favorite_24_clicked));
                            isFavouriteProduct = true;
                        }
                        else{
                            MenuView.ItemView favourite_btn = findViewById(R.id.action_product_favorite);
                            favourite_btn.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_favorite_24));
                            isFavouriteProduct = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_product_favorite:
                if(isFavouriteProduct){
                    MenuView.ItemView favourite_btn = findViewById(R.id.action_product_favorite);
                    favourite_btn.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_favorite_24));
                    DatabaseReference wishlistRemoveRef = FirebaseDatabase.getInstance().getReference().child("productWishlist")
                            .child(Prevalent.currentOnlineUser.getPhone());
                    wishlistRemoveRef.child(productID).removeValue();
                    Toast.makeText(this, "Remove product from wishlist successfully!!!", Toast.LENGTH_SHORT).show();
                    isFavouriteProduct = false;
                }
                else{
                    DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference().child("productWishlist")
                            .child(Prevalent.currentOnlineUser.getPhone()).child(productID);
                    MenuView.ItemView favourite_btn = findViewById(R.id.action_product_favorite);
                    favourite_btn.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_favorite_24_clicked));
                    HashMap<String, Object> wishlistMap = new HashMap<>();
                    wishlistMap.put("isFavourite", "yes");
                    wishlistRef.updateChildren(wishlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ProductDetailsActivity.this, "Add product to wishlist successfully!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    isFavouriteProduct = true;
                }
                return true;
            case R.id.action_product_back_to_home:
                startActivity(new Intent(ProductDetailsActivity.this, HomeActivity.class));
                return true;
            case R.id.action_product_category:
                Toast.makeText(this, "Category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_product_share:
                Toast.makeText(this, "Share product", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default: return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void addingToCartList(){
        //check max quantity of product before adding to cart
        DatabaseReference checkQuantity = FirebaseDatabase.getInstance().getReference().child("Products");
        checkQuantity.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    long total_cur_quantity = Long.parseLong(snapshot.child("quantity").getValue().toString());
                    if(total_cur_quantity == 0){
                        Toast.makeText(ProductDetailsActivity.this, "This product is out of stock", Toast.LENGTH_SHORT).show();
                    }
                    else if(total_cur_quantity > 0 && Long.parseLong(numberButton.getNumber()) > total_cur_quantity){
                        Toast.makeText(ProductDetailsActivity.this, "Max quantity of this product is " + total_cur_quantity, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String saveCurrentTime, saveCurrentDate;
                        Calendar calForDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                        saveCurrentDate = currentDate.format(calForDate.getTime());
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        saveCurrentTime = currentTime.format(calForDate.getTime());
                        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
                        final HashMap<String, Object> cartMap = new HashMap<>();
                        cartMap.put("pid", productID);
                        cartMap.put("pname", productName.getText().toString());
                        cartMap.put("price", productPrice.getText().toString());
                        cartMap.put("date", saveCurrentDate);
                        cartMap.put("time", saveCurrentTime);
                        cartMap.put("quantity", numberButton.getNumber());
                        cartMap.put("discount", "");
                        //show product sales
                        cartMap.put("sales", productSales.getText().toString());
                        //
                        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(productID).updateChildren(cartMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(productID).updateChildren(cartMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(ProductDetailsActivity.this, "Added to cart list", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                                startActivity(intent);
                                                                //decrease product quantity when buyer add to cart
                                                                DatabaseReference decreaseQuantity = FirebaseDatabase.getInstance().getReference().child("Products");
                                                                decreaseQuantity.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                        if(snapshot.exists()){
                                                                            long quantity_bought = Long.parseLong(numberButton.getNumber());
                                                                            long cur_quantity = Long.parseLong(snapshot.child("quantity").getValue().toString());
                                                                            String total_quantity = Long.toString(cur_quantity - quantity_bought);
                                                                            decreaseQuantity.child(productID).child("quantity").setValue(total_quantity);
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                    }
                                                                });
                                                                //
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    //calc product after sales
                    if(products.getSales().equals("")){
                        productSales.setText("No sales");
                    }
                    else{
                        productSales.setText(String.valueOf((Integer.valueOf(products.getPrice()) * (100 - Integer.valueOf(products.getSales()))) / 100));
                    }
                    //
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckOrderState(){
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippingState = snapshot.child("state").getValue().toString();
                    if(shippingState.equals("shipped")){
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped")){
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}