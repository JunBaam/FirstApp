package com.example.afinal;

public class NowreadingData {


    String bookname;      //책이름
    String bookauthor;    //책저자
    String bookcover;     //책커버 이미지
    String bookcategory;   //책분류
    String bookdate;        //책날짜
    String bookpage;      //책페이지
   int index;
    String check;  //독서진행상황 key값

    public NowreadingData(    String bookcover, String bookname, String bookauthor,
                              String bookcategory ,String bookdate,String bookpage,
                                int index, String check ) {
        this.bookcover=bookcover;
        this.bookname=bookname;
        this.bookauthor=bookauthor;
        this.bookcategory =bookcategory;
        this.bookdate = bookdate;
        this.bookpage =bookpage;
        this.index =index;
        this.check=check;

    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public int getIndex() {
       return index;
   }

    public void setIndex(int index) {
        this.index = index;
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