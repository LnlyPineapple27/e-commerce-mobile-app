package com.example.greentea.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greentea.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList cmt_name, cmt_date, cmt, cmt_rating_bar;
    Context context;

    public CommentAdapter(Context context, ArrayList cmt_name, ArrayList cmt_date, ArrayList cmt, ArrayList cmt_rating_bar){
        this.context = context;
        this.cmt_name = cmt_name;
        this.cmt_date = cmt_date;
        this.cmt = cmt;
        this.cmt_rating_bar = cmt_rating_bar;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.comment_name.setText(cmt_name.get(position).toString());
        holder.comment_date.setText(cmt_date.get(position).toString());
        holder.comment.setText(cmt.get(position).toString());
        holder.comment_rating_bar.setRating(Long.parseLong(cmt_rating_bar.get(position).toString()));
    }

    @Override
    public int getItemCount() {
        return cmt_name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView comment_name, comment_date, comment;
        RatingBar comment_rating_bar;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            comment_name = itemView.findViewById(R.id.txt_comment_name);
            comment_date = itemView.findViewById(R.id.txt_comment_date);
            comment = itemView.findViewById(R.id.txt_comment);
            comment_rating_bar = itemView.findViewById(R.id.txt_rating_bar);
        }
    }
}