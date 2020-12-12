package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class RecordMemoPlay extends AppCompatActivity implements View.OnClickListener {

    int id;
    String record_id;
    String record_title;
    String record_length;
    String record_data;

    TextView recordLength;
    TextView recordTitle;

    Button resetBtn;
    Button playBtn;
    Button deleteBtn;
    ImageButton closeBtn;

    MediaPlayer mediaPlayer;
    String filePath;

    int pausePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("음성 메모 재생");
        setContentView(R.layout.activity_record_memo_play);

        Intent intent = new Intent(this.getIntent());
        id = intent.getIntExtra("id", 0);
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
        deleteBtn = findViewById(R.id.record_delete_button);
        recordTitle = findViewById(R.id.record_play_title);
        recordLength = findViewById(R.id.record_play_length);

        resetBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
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
                break;
            }

            case R.id.record_delete_button: {

                RecordMemoDBHelper helper = new RecordMemoDBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                // db.beginTransaction();
                String sql = "DELETE FROM tb_record_memo where _id='"+ id +"'";
                db.execSQL(sql);
                Log.i("테스트", sql);
                Log.i("테스트", record_id);

                db.delete("TB_RECORD_MEMO", "RECORD_ID  LIKE ?", new String[] { record_id });

                //db.endTransaction();
                db.close();

                Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
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