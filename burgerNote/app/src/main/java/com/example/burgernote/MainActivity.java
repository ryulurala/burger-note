package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = findViewById(R.id.host);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("tab1");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_tab_memo_24px, null));
        spec.setContent(R.id.tab_content1);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab2");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_tab_drawing_24px, null));
        spec.setContent(R.id.tab_content2);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab3");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_tab_recording_24px, null));
        spec.setContent(R.id.tab_content3);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab4");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_tab_calendar_24px, null));
        spec.setContent(R.id.tab_content4);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab5");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_tab_setting_24px, null));
        spec.setContent(R.id.tab_content5);
        tabHost.addTab(spec);

    }
}