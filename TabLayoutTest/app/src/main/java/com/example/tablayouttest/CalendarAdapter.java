package com.example.tablayouttest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CustomViewHolder> {

    private ArrayList<CalendarData> arrayList;

    public CalendarAdapter(ArrayList<CalendarData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CalendarAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false);
        CalendarAdapter.CustomViewHolder holder = new CalendarAdapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CalendarAdapter.CustomViewHolder holder, int position) {

        holder.calendar_content.setText(arrayList.get(position).getCalendar_content());
        holder.calendar_date.setText(arrayList.get(position).getCalendar_date());
        holder.calendar_start_time.setText(arrayList.get(position).getCalendar_start_time());
        holder.calendar_end_time.setText(arrayList.get(position).getCalendar_end_time());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curName = holder.calendar_date.getText().toString();
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

        protected TextView calendar_content;
        protected TextView calendar_date;
        protected TextView calendar_start_time;
        protected TextView calendar_end_time;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.calendar_content = (TextView) itemView.findViewById(R.id.calendar_content);
            this.calendar_date = (TextView) itemView.findViewById(R.id.calendar_date);
            this.calendar_start_time = (TextView) itemView.findViewById(R.id.calendar_start_time);
            this.calendar_end_time = (TextView) itemView.findViewById(R.id.calendar_end_time);
        }

    }
}
