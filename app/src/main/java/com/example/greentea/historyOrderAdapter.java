package com.example.greentea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class historyOrderAdapter extends RecyclerView.Adapter<historyOrderAdapter.Viewholder>{
    private Context context;
    private ArrayList historyOrderListName, historyOrderListTotalAmount, historyOrderListProducts;

    public historyOrderAdapter(Context context, ArrayList historyOrderListName, ArrayList historyOrderListTotalAmount, ArrayList historyOrderListProducts) {
        this.context = context;
        this.historyOrderListName = historyOrderListName;
        this.historyOrderListTotalAmount = historyOrderListTotalAmount;
        this.historyOrderListProducts = historyOrderListProducts;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.nameBuyer.setText(historyOrderListName.get(position).toString());
        holder.totalAmount.setText(historyOrderListTotalAmount.get(position).toString());
        holder.listProduct.setText(historyOrderListProducts.get(position).toString());
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_order, parent, false);
        return new Viewholder(view);
    }

    @Override
    public int getItemCount() {
        return historyOrderListName.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private TextView nameBuyer, totalAmount, listProduct;

        public Viewholder(@NonNull View itemView){
            super(itemView);
            nameBuyer = itemView.findViewById(R.id.history_txt_name_buyer);
            totalAmount = itemView.findViewById(R.id.history_txt_total_amount);
            listProduct = itemView.findViewById(R.id.history_txt_list_product);
        }
    }
}