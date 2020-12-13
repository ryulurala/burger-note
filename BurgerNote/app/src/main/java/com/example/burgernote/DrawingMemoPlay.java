package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class DrawingMemoPlay extends AppCompatActivity implements View.OnClickListener {

    int id;
    String drawing_memo_image;
    String drawing_memo_date;

    ImageView drawingMemoImage;
    TextView drawingMemoDate;

    Button deleteBtn;
    ImageButton closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_memo_play);

        Intent intent = new Intent(this.getIntent());
        id = intent.getIntExtra("id", 0);
        drawing_memo_image = intent.getStringExtra("drawing_memo_image");
        drawing_memo_date = intent.getStringExtra("drawing_memo_date");

        closeBtn = findViewById(R.id.drawing_memo_close_button);
        deleteBtn = findViewById(R.id.drawing_memo_delete_button);

        drawingMemoImage = findViewById(R.id.drawing_memo_play_image);
        drawingMemoDate = findViewById(R.id.drawing_memo_play_date);

        closeBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        drawingMemoDate.setText(drawing_memo_date);

        String filePath = "";
        try {
            filePath = getFilesDir().getCanonicalPath() + "/";
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imagePath = filePath + drawing_memo_image;
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        drawingMemoImage.setImageBitmap(bm);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawing_memo_close_button: {
                finish();
                break;
            }

            case R.id.drawing_memo_delete_button: {

                DrawingMemoDBHelper helper = new DrawingMemoDBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                // db.beginTransaction();
                String sql = "DELETE FROM tb_drawing_memo where _id='"+ id +"'";
                db.execSQL(sql);

                //db.endTransaction();
                db.close();

                Toast.makeText(this, "그림 메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }
    }
}