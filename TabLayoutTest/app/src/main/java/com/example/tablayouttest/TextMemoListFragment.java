package com.example.tablayouttest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

    SQLiteDatabase db;
    TextMemoDBHelper helper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.text_memo_list_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.tab2_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        // DB 가져오기끝 시
        helper = new TextMemoDBHelper(getActivity());
        db = helper.getReadableDatabase();
        db.beginTransaction();
        Log.d("디버그", "1번");

        Cursor cursor = db.rawQuery("select title, content from tb_text_memo order by _id desc", null);
        while(cursor.moveToNext()) {
            TextMemoData data = new TextMemoData(cursor.getString(0), cursor.getString(1));
            arrayList.add(data);
        }
        db.close();
        // DB 가져오기

        textMemoAdapter = new TextMemoAdapter(arrayList);
        recyclerView.setAdapter(textMemoAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));


        // 아래는 더미데이터 등록을 위한 add 버튼임.
        Button btn_add = (Button) view.findViewById(R.id.tab2_btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TextMemoData mainData = new TextMemoData("동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세. 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세. 남산위에 저 소나무 철갑을 두른듯 바람서리 불변함은 우리 기상일세. 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세.", "2020년 12월 31일");
                arrayList.add(0, mainData);
                textMemoAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
