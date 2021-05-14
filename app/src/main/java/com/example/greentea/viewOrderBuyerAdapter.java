package com.example.greentea;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greentea.Admin.AdminNewOrdersActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class viewOrderBuyerAdapter extends RecyclerView.Adapter<viewOrderBuyerAdapter.Viewholder>{
    private Context context;
    private ArrayList viewOrderDate, viewOrderTime, viewOrderName, viewOrderPhone, viewOrderTotalAmount, viewOrderListProducts;

    public viewOrderBuyerAdapter(Context context, ArrayList viewOrderDate, ArrayList viewOrderTime, ArrayList viewOrderName, ArrayList viewOrderPhone,
    ArrayList viewOrderTotalAmount, ArrayList viewOrderListProducts) {
        this.context = context;
        this.viewOrderDate = viewOrderDate;
        this.viewOrderTime = viewOrderTime;
        this.viewOrderName = viewOrderName;
        this.viewOrderPhone = viewOrderPhone;
        this.viewOrderTotalAmount = viewOrderTotalAmount;
        this.viewOrderListProducts = viewOrderListProducts;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.viewDate.setText(viewOrderDate.get(position).toString());
        holder.viewTime.setText(viewOrderTime.get(position).toString());
        holder.viewName.setText(viewOrderName.get(position).toString());
        holder.viewPhone.setText(viewOrderPhone.get(position).toString());
        holder.viewTotalAmount.setText(viewOrderTotalAmount.get(position).toString());
        holder.viewListProduct.setText(viewOrderListProducts.get(position).toString());
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_order_buyer, parent, false);
        return new Viewholder(view);
    }

    @Override
    public int getItemCount() {
        return viewOrderDate.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private TextView viewDate, viewTime, viewName, viewPhone, viewTotalAmount, viewListProduct;

        public Viewholder(@NonNull View itemView){
            super(itemView);
            viewDate = itemView.findViewById(R.id.view_order_date_txt);
            viewTime = itemView.findViewById(R.id.view_order_time_txt);
            viewName = itemView.findViewById(R.id.view_order_name_txt);
            viewPhone = itemView.findViewById(R.id.view_order_phone_txt);
            viewTotalAmount = itemView.findViewById(R.id.view_order_total_amount_txt);
            viewListProduct = itemView.findViewById(R.id.view_order_list_products_txt);
        }
    }
}