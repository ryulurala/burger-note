package com.example.burgernote;

import android.content.Intent;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;

public class RecordingMemoListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private final ArrayList<RecordMemoData> mMemoDataArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recording_memo_list_fragment, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.recording_memo_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView recyclerView = view.findViewById(R.id.tab3_rv);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        RecordMemoDBHelper mMemoDBHelper = new RecordMemoDBHelper(getActivity());

        recyclerView.setLayoutManager(mLinearLayoutManager);

        mMemoDataArrayList.clear();

        // DB 가져오기 시작
        SQLiteDatabase db = mMemoDBHelper.getWritableDatabase();
        // db.beginTransaction();

        Cursor cursor = db.rawQuery("select * from tb_record_memo order by _id desc", null);
        while(cursor.moveToNext()) {
            RecordMemoData data = new RecordMemoData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            mMemoDataArrayList.add(data);
        }
        // db.endTransaction();
        db.close();
        // DB 가져오기 끝

        String filePath = "";
        try {
            filePath = getContext().getFilesDir().getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RecordMemoAdapter recordMemoAdapter = new RecordMemoAdapter(mMemoDataArrayList, filePath + "/");
        recyclerView.setAdapter(recordMemoAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        return view;
    }

    @Override
    public void onRefresh() {
        // 새로 고침
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        mSwipeRefreshLayout.setRefreshing(false);        // 새로 고침 완료
    }
}
