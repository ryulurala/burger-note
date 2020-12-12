package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class RecordMemoPlay extends AppCompatActivity implements View.OnClickListener {


    String record_id;
    String record_title;
    String record_length;
    String record_data;

    TextView recordLength;
    TextView recordTitle;

    Button resetBtn;
    Button playBtn;
    Button closeBtn;

    MediaPlayer mediaPlayer;
    String filePath;

    int pausePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("음성 메모 재생");
        setContentView(R.layout.activity_record_memo_play);

        Intent intent = new Intent(this.getIntent());
        record_id = intent.getStringExtra("record_id");
        record_title = intent.getStringExtra("record_title");
        record_length = intent.getStringExtra("record_length");
        record_data = intent.getStringExtra("record_data");

        filePath = getFilesDir().toString() + "/" + record_id;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(filePath));
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        resetBtn = findViewById(R.id.record_reset_button);
        playBtn = findViewById(R.id.record_play_button);
        closeBtn = findViewById(R.id.record_close_button);
        recordTitle = findViewById(R.id.record_play_title);
        recordLength = findViewById(R.id.record_play_length);

        resetBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        recordTitle.setText(record_title);
        recordLength.setText(record_length);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.record_play_button:{
                if(!mediaPlayer.isPlaying()) {
                    try {
                        playBtn.setText("중지");
                        mediaPlayer.seekTo(pausePosition);
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    playBtn.setText("재생");
                    mediaPlayer.pause();
                    pausePosition = mediaPlayer.getCurrentPosition();
                }
                break;
            }

            case R.id.record_reset_button: {
                playBtn.setText("중지");
                pausePosition = 0;
                mediaPlayer.seekTo(pausePosition);
                mediaPlayer.start();
                break;
            }

            case R.id.record_close_button: {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}