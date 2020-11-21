package com.example.textmemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

    ArrayList<MemoList> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemoList item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(MemoList item) {
        items.add(item);
    }

    public void setItems(ArrayList<MemoList> items) {
        this.items = items;
    }

    public MemoList getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, MemoList item) {
        items.set(position, item);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titlepart;
        private TextView contentpart;

        public ViewHolder(View itemView) {
            super(itemView);

            titlepart = itemView.findViewById(R.id.titlepart);
            contentpart = itemView.findViewById(R.id.contentpart);

        }

        public void setItem(MemoList item) {
            titlepart.setText(item.getContent());
            contentpart.setText(item.getContent());
        }
    }
}