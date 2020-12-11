package com.example.burgernote;

import android.os.Parcel;
import android.os.Parcelable;

//다른 프래그먼트들과 형식이 좀 달라 다른 예제를 따라한 Parcelable 사용했습니다.
public class RecordMemoData implements Parcelable {

    private String record_title; //녹음파일 이름
    private String record_path; //녹음파일 경로
    private  int record_id; //녹음파일 아이디
    private int record_length; //기록된 시간
    private String record_date; //기록된 날짜 시간...


    public String getFilePath() { return record_title; }

    public void setFilepath(String filePath) { record_path = filePath; }

    public int getLength() { return record_length; }

    public void setLength(int length) { record_length = length; }

    public int getId() { return record_id ; }

    public void setId(int id) { record_id = id; }

    public String getTitle() { return record_title; }

    public  void setTitle(String title) { record_title = title; }

    public String  getDate() { return record_date; }

    public void setDate(String  date) { record_date = date; }


    public RecordMemoData(Parcel in) {
        record_title = in.readString();
        record_path = in.readString();
        record_id = in.readInt();
        record_length = in.readInt();
        record_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(record_title);
        dest.writeString(record_path);
        dest.writeInt(record_id);
        dest.writeInt(record_length);
        dest.writeString(record_date);
    }

    public static final Creator<RecordMemoData> CREATOR = new Creator<RecordMemoData>() {
        @Override
        public RecordMemoData createFromParcel(Parcel in) {
            return new RecordMemoData(in);
        }

        @Override
        public RecordMemoData[] newArray(int size) {
            return new RecordMemoData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
