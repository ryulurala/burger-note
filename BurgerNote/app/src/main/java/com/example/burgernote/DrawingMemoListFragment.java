package com.example.burgernote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DrawingMemoListFragment extends Fragment {

    private ArrayList<DrawingMemoData> arrayList;
    private DrawingMemoAdapter drawingMemoAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    SQLiteDatabase db;
    DrawingMemoDBHelper helper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.drawing_memo_list_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.tab1_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        // DB 가져오기 시작
        helper = new DrawingMemoDBHelper(getActivity());
        db = helper.getReadableDatabase();
        db.beginTransaction();

        Cursor cursor = db.rawQuery("select image, date from tb_drawing_memo order by _id desc", null);
        while(cursor.moveToNext()) {
            DrawingMemoData data = new DrawingMemoData(cursor.getString(0), cursor.getString(1));
            arrayList.add(data);
        }
        db.endTransaction();
        db.close();
        // DB 가져오기 끝

        drawingMemoAdapter = new DrawingMemoAdapter(arrayList);
        recyclerView.setAdapter(drawingMemoAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        return view;
    }
}
