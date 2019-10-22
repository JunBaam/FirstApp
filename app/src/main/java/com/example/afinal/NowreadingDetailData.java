package com.example.afinal;

public class NowreadingDetailData {

    String page;
    String date;

 public  NowreadingDetailData(String page , String date){
 this.page=page;
 this.date=date;

 }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
