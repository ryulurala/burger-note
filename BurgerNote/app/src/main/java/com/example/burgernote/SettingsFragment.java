package com.example.burgernote;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private final boolean[] mFlags = new boolean[4];  // 모두 체크 + 4 개의 메모

    private CheckBox mCheckedAll;
    private ArrayList<CheckBox> mCheckedList = new ArrayList<CheckBox>(4);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 설정탭 프래그먼트입니다.
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        mCheckedList.clear();

        mCheckedAll = view.findViewById(R.id.checked_all);      // 모두 체크
        mCheckedList.add(view.findViewById(R.id.checked_text_memo));        // 텍스트
        mCheckedList.add(view.findViewById(R.id.checked_drawing_memo));     // 그림
        mCheckedList.add(view.findViewById(R.id.checked_record_memo));   // 녹음
        mCheckedList.add(view.findViewById(R.id.checked_calendar_memo));    // 일정

        initCheckBoxes();       // 기존꺼 로드
        
        // 콜백함수 등록
        mCheckedAll.setOnClickListener(this);
        for(int i=0; i<mCheckedList.size(); i++){
            mCheckedList.get(i).setOnClickListener(this);
        }

//        Log.d("myLog","SettingsFragment onCreateView()");

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.d("myLog","SettingsFragment onStop()");

        setFlags();         // 커스터마이징 설정
        
        ((MainActivity) getActivity()).mService.customizeMemos(mFlags);
    }

    @Override
    public void onClick(View v) {
        boolean isChecked = ((CheckBox) v).isChecked();

        if(v.getId() == R.id.checked_all){
            setCheckedAll(isChecked);
//            Log.d("myLog", "allCheck O: " + isChecked);
        } else if(!isChecked) {             // 다른 버튼이 체크 상태가 풀렸을 경우
            mCheckedAll.setChecked(isChecked);
//            Log.d("myLog", "allCheck X: " + isChecked);
        }
    }

    void initCheckBoxes(){
        boolean isCheckedAll = true;

        for(int i = 0; i< mFlags.length; i++){
            if(!mFlags[i]) {
                isCheckedAll = false;
                continue;
            }
            mCheckedList.get(i).setChecked(mFlags[i]);
        }

        if(isCheckedAll) mCheckedAll.setChecked(true);
    }

    void setFlags(){
        for(int i=0; i<mFlags.length; i++){
            mFlags[i] = mCheckedList.get(i).isChecked();
        }
    }

    void setCheckedAll(boolean isChecked){
        for(int i=0; i<mCheckedList.size(); i++){
            mCheckedList.get(i).setChecked(isChecked);
        }
    }

}
