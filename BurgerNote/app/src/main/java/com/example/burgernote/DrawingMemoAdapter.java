package com.example.burgernote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private final ArrayList<DrawingMemoData> MEMO_DATA_LIST;
    private final String IMAGE_PATH;

    public DrawingMemoAdapter(ArrayList<DrawingMemoData> arrayList, String filePath) {
        this.MEMO_DATA_LIST = arrayList;
        IMAGE_PATH = filePath;
    }

    @NonNull
    @Override
    public DrawingMemoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawing_memo_item, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DrawingMemoAdapter.CustomViewHolder holder, int position) {

        String imagePath = IMAGE_PATH + MEMO_DATA_LIST.get(position).getDrawing_memo_image();
        Bitmap bm = BitmapFactory.decodeFile(imagePath);

        holder.iv_image.setImageBitmap(bm);
        holder.tv_date.setText(MEMO_DATA_LIST.get(position).getDrawing_memo_date());

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
        return (null != MEMO_DATA_LIST ? MEMO_DATA_LIST.size() : 0);
    }

    public void remove(int position){
        try {
            MEMO_DATA_LIST.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_image;
        protected TextView tv_date;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_image = (ImageView) itemView.findViewById(R.id.drawing_memo_image);
            this.tv_date = (TextView) itemView.findViewById(R.id.drawing_memo_date);
        }

    }
}