package com.example.tablayouttest;

public class TextMemoData {

    private String text_memo_context;
    private String text_memo_date;

    public TextMemoData(String text_memo_context, String text_memo_date){
        this.text_memo_context = text_memo_context;
        this.text_memo_date = text_memo_date;
    }

    public String getText_memo_context() {
        return text_memo_context;
    }

    public void setText_memo_context(String text_memo_context) {
        this.text_memo_context = text_memo_context;
    }

    public String getText_memo_date() {
        return text_memo_date;
    }

    public void setText_memo_date(String text_memo_date) {
        this.text_memo_date = text_memo_date;
    }
}