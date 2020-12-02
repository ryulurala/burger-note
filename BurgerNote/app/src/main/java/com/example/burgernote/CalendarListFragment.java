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

public class CalendarListFragment extends Fragment implements View.OnClickListener {

    private ArrayList<CalendarData> arrayList;
    private CalendarAdapter calendarAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    SQLiteDatabase db;
    CalendarDBHelper helper;

    Button addBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calendar_list_fragment, container, false);

        addBtn = view.findViewById(R.id.calendar_list_add_btn);
        addBtn.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.tab4_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        // DB 가져오기 시작
        helper = new CalendarDBHelper(getActivity());
        db = helper.getReadableDatabase();
        db.beginTransaction();

        Cursor cursor = db.rawQuery("select content, date, start_time, end_time from tb_calendar order by date, start_time, end_time", null);
        while(cursor.moveToNext()) {
            CalendarData data = new CalendarData(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            arrayList.add(data);
        }
        db.endTransaction();
        db.close();
        // DB 가져오기 끝

        calendarAdapter = new CalendarAdapter(arrayList);
        recyclerView.setAdapter(calendarAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), CalendarWriteActivity.class);
        startActivity(intent);
    }
}
