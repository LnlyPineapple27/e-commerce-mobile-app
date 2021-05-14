package com.example.greentea.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.greentea.Buyers.JoinForm;
import com.example.greentea.Model.Products;
import com.example.greentea.R;
import com.example.greentea.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*for new android version
public class SellerHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
}*/

//for old android version
public class SellerHomeActivity extends AppCompatActivity{
    //private TextView mTextMessage;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;
    //check account verified email before logging to home page
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.navigation_home:
                    Intent intentHome = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_add:
                    Intent intentCate = new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                    startActivity(intentCate);
                    return true;
                case R.id.navigation_logout:
                    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intentMain = new Intent(SellerHomeActivity.this, JoinForm.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentMain);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check account verified email before logging to home page
        if(mAuth.getCurrentUser().isEmailVerified()){
            setContentView(R.layout.activity_seller_home);
            getSupportActionBar().hide();
            BottomNavigationView navView = findViewById(R.id.nav_view);
            //mTextMessage = findViewById(R.id.message);
            navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
            recyclerView = findViewById(R.id.seller_home_recyclerview);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
        }
        else{
            startActivity(new Intent(SellerHomeActivity.this, SellerLoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check account verified email before logging to home page
        if(mAuth.getCurrentUser().isEmailVerified()){
            FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                    .setQuery(unverifiedProductsRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class).build();
            FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Products model) {
                    holder.txtProductName.setText(model.getPname());
                    holder.txtProductDescription.setText(model.getDescription());
                    holder.txtProductStatus.setText("State: " + model.getProductState());
                    holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                    Picasso.get().load(model.getImage()).into(holder.imageView);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String productID = model.getPid();
                            CharSequence options[] = new CharSequence[]{
                                    "Yes", "No"
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                            builder.setTitle("Do you want to delete this product?");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position) {
                                    if(position == 0){
                                        deleteProduct(productID);
                                    }
                                    if(position == 1){

                                    }
                                }
                            });
                            builder.show();
                        }
                    });
                }

                @NonNull
                @Override
                public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                    ItemViewHolder holder = new ItemViewHolder(view);
                    return holder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }

    private void deleteProduct(String productID){
        unverifiedProductsRef.child(productID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SellerHomeActivity.this, "This product has been deleted successfully!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}