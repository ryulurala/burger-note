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

public class TextMemoListFragment extends Fragment {

    private ArrayList<TextMemoData> arrayList;
    private TextMemoAdapter textMemoAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.text_memo_list_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.tab2_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        textMemoAdapter = new TextMemoAdapter(arrayList);
        recyclerView.setAdapter(textMemoAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));


        // Button btn_add = (Button) btn_add.findViewById();
        Button btn_add = (Button) view.findViewById(R.id.tab2_btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TextMemoData mainData = new TextMemoData("동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세. 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세. 남산위에 저 소나무 철갑을 두른듯 바람서리 불변함은 우리 기상일세. 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세.", "2020년 12월 31일");
                arrayList.add(mainData);
                textMemoAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
