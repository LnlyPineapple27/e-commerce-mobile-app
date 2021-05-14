package com.example.greentea.Buyers;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.example.greentea.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import com.example.greentea.NotificationModels.MixedNotification;
import com.example.greentea.NotificationModels.DiscountNotifications;
import com.example.greentea.NotificationModels.OrdersNotifications;

public class NotificationsActivity extends AppCompatActivity {
    BottomNavigationView botNav;
    FragmentTransaction ft;
    MixedNotificationFragment frag;
    ArrayList<MixedNotification> generate_date(){
        ArrayList<MixedNotification> r = new ArrayList<>();
        for(int i = 0; i <10; ++i){
            if(i % 2 == 0)
                r.add(new OrdersNotifications(new java.util.Date().getTime(), "DEMO notification " + String.valueOf(i), "message " + String.valueOf(i)));
            else
                r.add(new DiscountNotifications(new java.util.Date().getTime(), "DEMO notification " + String.valueOf(i), "message " + String.valueOf(i)));
        }
        return r;
    }
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_notifications);
        botNav = findViewById(R.id.nav_bottom_buyer_notification);
        if(savedInstanceState == null) {
            refresh(MixedNotificationFragment.MODE_ALL);
        }
        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_all_notifications:
                        refresh(MixedNotificationFragment.MODE_ALL);
                        return true;
                    case R.id.navigation_discounts_notifications:
                        refresh(MixedNotificationFragment.MODE_DISCOUNTS);
                        return true;
                    case R.id.navigation_orders_notifications:
                        refresh(MixedNotificationFragment.MODE_ORDER);
                        return true;
                }
                return false;
            }
        });
    }
    private void refresh(int mode){
        frag = MixedNotificationFragment.new_instance(generate_date(), mode);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.notifications_container, frag);
        ft.commit();
    }
}
