package com.example.greentea.Buyers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greentea.NotificationModels.MixedNotification;
import com.example.greentea.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter {
    Context mcotext;
    ArrayList<MixedNotification> not_list;
    public NotificationAdapter(Context context, ArrayList<MixedNotification> notilist) {
        mcotext = context;
        not_list = notilist;
    }

    @Override
    public int getItemViewType(int position) {
        return not_list.get(position).getType();
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType){
            case MixedNotification.TYPE_DISCOUNT:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_item, parent, false);
                return new DiscountsHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case MixedNotification.TYPE_DISCOUNT:
                ((DiscountsHolder) holder).bindView(position);
                break;
            default:
                ((OrderHolder) holder).bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        return not_list.size();
    }
    class OrderHolder extends RecyclerView.ViewHolder {
        TextView order_title, order_message, order_timestamp;
        OrderHolder(View itemView) {
            super(itemView);
            order_title = itemView.findViewById(R.id.order_title);
            order_message = itemView.findViewById(R.id.order_message);
            order_timestamp = itemView.findViewById(R.id.order_time_stamp);
        }
        void bindView(int pos){
            order_title.setText(not_list.get(pos).getTitle());
            order_timestamp.setText(not_list.get(pos).getDateString());
            order_message.setText(not_list.get(pos).getMessage());
        }
    }
    class DiscountsHolder extends RecyclerView.ViewHolder {
        TextView discount_title, discount_message, discount_timestamp;
        DiscountsHolder(View itemView) {
            super(itemView);
            discount_title = itemView.findViewById(R.id.discount_title);
            discount_message = itemView.findViewById(R.id.discount_message);
            discount_timestamp = itemView.findViewById(R.id.discount_time_stamp);
        }
        void bindView(int pos){
            discount_title.setText(not_list.get(pos).getTitle());
            discount_timestamp.setText(not_list.get(pos).getDateString());
            discount_message.setText(not_list.get(pos).getMessage());
        }
    }
}
