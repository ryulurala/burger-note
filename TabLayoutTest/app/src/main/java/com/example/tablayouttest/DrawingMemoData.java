package com.example.tablayouttest;

public class DrawingMemoData {

    private int drawing_memo_thumbnail;
    private String drawing_memo_date;

    public DrawingMemoData(int drawing_memo_thumbnail, String drawing_memo_date){
        this.drawing_memo_thumbnail = drawing_memo_thumbnail;
        this.drawing_memo_date = drawing_memo_date;
    }

    public String getDrawing_memo_date() {
        return drawing_memo_date;
    }

    public void setDrawing_memo_date(String drawing_memo_date) {
        this.drawing_memo_date = drawing_memo_date;
    }

    public int getDrawing_memo_thumbnail() {
        return drawing_memo_thumbnail;
    }

    public void setDrawing_memo_thumbnail(int drawing_memo_thumbnail) {
        this.drawing_memo_thumbnail = drawing_memo_thumbnail;
    }
}