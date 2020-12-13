package com.example.burgernote;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TextMemoAdapter extends RecyclerView.Adapter<TextMemoAdapter.CustomViewHolder> {

    private ArrayList<TextMemoData> arrayList;

    public TextMemoAdapter(ArrayList<TextMemoData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public TextMemoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_memo_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TextMemoAdapter.CustomViewHolder holder, int position) {

        holder.text_memo_context.setText(arrayList.get(position).getText_memo_content());
        holder.text_memo_date.setText(arrayList.get(position).getText_memo_date());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TextMemoPlay.class);

                intent.putExtra("id", arrayList.get(position).getId());
                intent.putExtra("text_memo_content",arrayList.get(position).getText_memo_content());
                intent.putExtra("text_memo_date",arrayList.get(position).getText_memo_date());

                view.getContext().startActivity(intent);
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

        protected TextView text_memo_context;
        protected TextView text_memo_date;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.text_memo_context = (TextView) itemView.findViewById(R.id.text_memo_context);
            this.text_memo_date = (TextView) itemView.findViewById(R.id.text_memo_date);
        }

    }
}