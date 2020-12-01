package com.example.tablayouttest;

public class CalendarData {

    private String calendar_content;
    private String calendar_date;
    private String calendar_start_time;
    private String calendar_end_time;

    // 일정 메모는 content, date, start time, end time을 String 포맷으로 가짐.


    public CalendarData(String calendar_content, String calendar_date, String calendar_start_time, String calendar_end_time) {
        this.calendar_content = calendar_content;
        this.calendar_date = calendar_date;
        this.calendar_start_time = calendar_start_time;
        this.calendar_end_time = calendar_end_time;
    }

    public String getCalendar_content() {
        return calendar_content;
    }

    public void setCalendar_content(String calendar_content) {
        this.calendar_content = calendar_content;
    }

    public String getCalendar_date() {
        return calendar_date;
    }

    public void setCalendar_date(String calendar_date) {
        this.calendar_date = calendar_date;
    }

    public String getCalendar_start_time() {
        return calendar_start_time;
    }

    public void setCalendar_start_time(String calendar_start_time) {
        this.calendar_start_time = calendar_start_time;
    }

    public String getCalendar_end_time() {
        return calendar_end_time;
    }

    public void setCalendar_end_time(String calendar_end_time) {
        this.calendar_end_time = calendar_end_time;
    }
}
