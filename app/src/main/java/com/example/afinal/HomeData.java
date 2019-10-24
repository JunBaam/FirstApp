package com.example.afinal;

public class HomeData {

    String bookname;      //책이름
    String bookauthor;    //책저자
    String bookcover;     //책커버 이미지
    String bookcategory;   //책분류
    String bookdate;        //책날짜
    String bookend;        //책다읽은날짜
    String bookpage;      //책페이지

    int index;  //독서진행상황
    boolean complete;  //독서완료 유무

    public  HomeData(    String bookcover, String bookname, String bookauthor,
                              String bookcategory ,String bookdate,String bookpage,String bookend,
                              int index ,boolean complete) {
        this.bookcover=bookcover;
        this.bookname=bookname;
        this.bookauthor=bookauthor;
        this.bookcategory =bookcategory;
        this.bookdate = bookdate;
        this.bookpage =bookpage;
        this.bookend =bookend;
        this.index =index;
        this.complete=complete;
    }


    public String getBookend() {
        return bookend;
    }

    public void setBookend(String bookend) {
        this.bookend = bookend;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public   String getBookcover() {
        return bookcover;
    }

    public void setBookcover(   String bookcover) {
        this.bookcover = bookcover;
    }

    public String getBookpage() {
        return bookpage;
    }

    public void setBookpage(String bookpage) {
        this.bookpage = bookpage;
    }


    public String getBookcategory() {
        return bookcategory;
    }

    public void setBookcategory(String bookcategory) {
        this.bookcategory = bookcategory;
    }

    public String getBookdate() {
        return bookdate;
    }

    public void setBookdate(String bookdate) {
        this.bookdate = bookdate;
    }
}