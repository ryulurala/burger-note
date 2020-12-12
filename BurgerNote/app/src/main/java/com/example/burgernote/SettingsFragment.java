package com.example.burgernote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SettingsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab5_fragment, container, false);

        // 설정탭 프래그먼트입니다.

        return view;
    }

    @Override
    public void onRefresh() {

    }
}
