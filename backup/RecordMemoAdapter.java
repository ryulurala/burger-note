package com.example.burgernote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordMemoAdapter extends RecyclerView.Adapter<RecordMemoAdapter.CustomViewHolder> {

    private ArrayList<RecordMemoData> RECORD_DATA_LIST;

    private static final String LOG_TAG = "RecordMemoAdapter";

    private RecordMemoDBHelper record_Database;

    RecordMemoData data;
    Context mContext;


    @NonNull
    @Override
    public RecordMemoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_memo_item, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordMemoAdapter.CustomViewHolder holder, int position) {

//        item = getItem(position);
//
//        long itemDuration =
//
//        holder.tv_recorder_title.setText(RECORD_DATA_LIST.get(position).getTitle());
//        holder.tv_recorder_length.setText(String.format("%02d;%02d", minutes, seconds));
//        holder.tv_recorder_date.setText((RECORD_DATA_LIST).get(position).getDate());


    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_recorder_fileviewer;
        protected TextView tv_recorder_title;
        protected TextView tv_recorder_length;
        protected TextView tv_recorder_date;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_recorder_fileviewer = (ImageView) itemView.findViewById(R.id.recorder_memo_fileviewer);
            this.tv_recorder_title = (TextView) itemView.findViewById(R.id.recorder_memo_title);
            this.tv_recorder_length = (TextView) itemView.findViewById(R.id.recorder_memo_length);
            this.tv_recorder_date = (TextView) itemView.findViewById(R.id.recorder_memo_date);
        }
    }
}
