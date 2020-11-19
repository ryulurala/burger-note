package com.example.tablayouttest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DrawingMemoAdapter extends RecyclerView.Adapter<DrawingMemoAdapter.CustomViewHolder> {

    private ArrayList<DrawingMemoData> arrayList;

    public DrawingMemoAdapter(ArrayList<DrawingMemoData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DrawingMemoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_memo, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DrawingMemoAdapter.CustomViewHolder holder, int position) {

        holder.iv_thumbnail.setImageResource(arrayList.get(position).getDrawing_memo_thumbnail());
        holder.tv_date.setText(arrayList.get(position).getDrawing_memo_date());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curName = holder.tv_date.getText().toString();
                Toast.makeText(view.getContext(),curName,Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                remove(holder.getAdapterPosition());

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position){
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_thumbnail;
        protected TextView tv_date;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_thumbnail = (ImageView) itemView.findViewById(R.id.drawing_memo_thumbnail);
            this.tv_date = (TextView) itemView.findViewById(R.id.drawing_memo_date);
        }

    }
}