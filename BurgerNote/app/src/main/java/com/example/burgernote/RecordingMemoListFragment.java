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

import java.util.ArrayList;

public class RecordingMemoListFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



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

        View view = inflater.inflate(R.layout.recording_memo_list_fragment, container, false);

        return view;
    }
}
