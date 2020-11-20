package com.example.tablayouttest;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.drawing_memo_list_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.tab1_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        drawingMemoAdapter = new DrawingMemoAdapter(arrayList);
        recyclerView.setAdapter(drawingMemoAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));


        // Button btn_add = (Button) btn_add.findViewById();
        Button btn_add = (Button) view.findViewById(R.id.tab1_btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DrawingMemoData mainData = new DrawingMemoData(R.drawable.test_image, "2020년 12월 31일");
                arrayList.add(mainData);
                drawingMemoAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
