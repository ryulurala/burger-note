package com.example.burgernote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecordingMemoListFragment extends Fragment {


    private ArrayList<RecordMemoData> recordMemoDataArrayList;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private RecordMemoAdapter recordMemoAdapter;

    SQLiteDatabase db;
    RecordMemoDBHelper helper;

    TextView recorderTitle;
    TextView recorderLength;
    TextView recorderDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recording_memo_list_fragment, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.text_memo_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        recyclerView = (RecyclerView) view.findViewById(R.id.tab3_rv);

        recorderTitle = view.findViewById(R.id.recorder_memo_title);
        recorderLength = view.findViewById(R.id.recorder_memo_length);
        recorderDate = view.findViewById(R.id.recorder_memo_date);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recordMemoDataArrayList = new ArrayList<>();

        // DB 가져오기 시작
        helper = new RecordMemoDBHelper(getActivity());
        db = helper.getReadableDatabase();
        db.beginTransaction();

        Cursor cursor = db.rawQuery("select title, length, date from tb_record_memo order by _id desc", null);
        while (cursor.moveToNext()) {
            RecordMemoData data = new RecordMemoData(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            recordMemoDataArrayList.add(data);
        }
        db.endTransaction();
        db.close();
        // DB 가져오기 끝

        recordMemoAdapter = new RecordMemoAdapter(recordMemoDataArrayList);
        recyclerView.setAdapter(recordMemoAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        return view;
    }

    @Override // 이게 왜 빨간줄인지가 알 수 없습니다.
    public void onRefresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        mSwipeRefreshLayout.setRefreshing(false);        // 새로 고침 완료
    }
}



// 어플 내부에서 여태껏 녹음한 음성 메모의 리스트를 보여주는 프래그먼트입니다.
// 이 프래그먼트는 main activity 상에서 작동하며
// 이 프래그먼트의 xml 파일은 recording_memo_list_fragment.xml 입니다.

// 이 프래그먼트와 비슷한 동작을 하는 프래그먼트는
// 성률님이 작업하신 그림 메모에 쓰인 DrawingMemoListFragment가 있습니다.
// 혹은 제가 작업한 CalendarListFragment, TextMemoFragment가 있습니다.
// 성률님이 작업한거랑 제가 작업한거 중에 하나 보시고 따라하시면 될 것 같습니다.
// 기능상에 큰 차이는 없습니다.

// 이 프래그먼트에 기본적으로 필요한 xml 파일 세팅은
// recording_memo_list_fragment.xml 에 해두었습니다.
// 아마 추가 작업은 필요 없을 것 같습니다.

// 참고 부탁드립니다.
