package com.example.burgernote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordMemoAdapter extends RecyclerView.Adapter<RecordMemoAdapter.CustomViewHolder> {

    private ArrayList<RecordMemoData> MEMO_DATA_LIST;
    private final String RECORD_PATH;

    public RecordMemoAdapter(ArrayList<RecordMemoData> arrayList, String filePath) {
        this.MEMO_DATA_LIST = arrayList;
        RECORD_PATH = filePath;
    }

    @NonNull
    @Override
    public RecordMemoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_memo_item, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordMemoAdapter.CustomViewHolder holder, int position) {

        String recordPath = RECORD_PATH + MEMO_DATA_LIST.get(position).getRecord_id();

        holder.tv_recorder_title.setText(MEMO_DATA_LIST.get(position).getRecord_title());
        holder.tv_recorder_length.setText(MEMO_DATA_LIST.get(position).getRecord_length());
        holder.tv_recorder_date.setText(MEMO_DATA_LIST.get(position).getRecord_date());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // 여기에 한 번 터치했을때 동작을 기술해야합니다.
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != MEMO_DATA_LIST ? MEMO_DATA_LIST.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_recorder_title;
        protected TextView tv_recorder_length;
        protected TextView tv_recorder_date;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_recorder_title = (TextView) itemView.findViewById(R.id.recorder_memo_title);
            this.tv_recorder_length = (TextView) itemView.findViewById(R.id.recorder_memo_length);
            this.tv_recorder_date = (TextView) itemView.findViewById(R.id.recorder_memo_date);
        }
    }
}
