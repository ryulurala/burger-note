package com.example.burgernote;

//다른 프래그먼트들과 형식이 좀 달라 다른 예제를 따라한 Parcelable 사용했습니다.
public class RecordMemoData {

    private int id;
    private String record_title; // 녹음 파일 이름
    private String record_id; // 녹음 파일 아이디 -> 녹음한 시각을 yyyyMMddHHmmssSSS 으로 하여 아이디로 사용합니다. 이걸 파일명으로 쓸거에요.
    private String record_length; // 녹음 길이
    private String record_date; // 녹음한 시각

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecord_title() {
        return record_title;
    }

    public void setRecord_title(String record_title) {
        this.record_title = record_title;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getRecord_length() {
        return record_length;
    }

    public void setRecord_length(String record_length) {
        this.record_length = record_length;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }


    public RecordMemoData(int id, String record_id, String record_title, String record_length, String record_date) {
        this.id = id;
        this.record_id = record_id;
        this.record_title = record_title;
        this.record_length = record_length;
        this.record_date = record_date;
    }
}
