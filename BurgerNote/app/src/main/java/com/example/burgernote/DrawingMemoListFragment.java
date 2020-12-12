package com.example.burgernote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class DrawingMemoListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private final ArrayList<DrawingMemoData> mMemoDataArrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.drawing_memo_list_fragment, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.drawing_memo_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tab1_rv);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        DrawingMemoDBHelper mMemoDBHelper = new DrawingMemoDBHelper(getActivity());

        recyclerView.setLayoutManager(mLinearLayoutManager);

        mMemoDataArrayList.clear();

        // DB 가져오기 시작
        SQLiteDatabase db = mMemoDBHelper.getReadableDatabase();
        db.beginTransaction();

        Cursor cursor = db.rawQuery("select image, date from tb_drawing_memo order by _id desc", null);
        while(cursor.moveToNext()) {
            DrawingMemoData data = new DrawingMemoData(cursor.getString(0), cursor.getString(1));
            mMemoDataArrayList.add(data);
        }
        db.endTransaction();
        db.close();
        // DB 가져오기 끝

        String filePath = getContext().getFilesDir().toString();
        try {
            filePath = getContext().getFilesDir().getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DrawingMemoAdapter drawingMemoAdapter = new DrawingMemoAdapter(mMemoDataArrayList, filePath + "/");
        recyclerView.setAdapter(drawingMemoAdapter);

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
