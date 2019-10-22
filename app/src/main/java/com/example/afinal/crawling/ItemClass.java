package com.example.afinal.crawling;



public class ItemClass {

      //책이미지 이름 가격 세부정보 url
    private String book_imgurl, book_name, book_price, book_sub_info, book_innerurl;

    public ItemClass (String book_imgurl, String book_name, String book_price, String book_sub_info, String book_innerurl) {
        this.book_imgurl = book_imgurl;
        this.book_name = book_name;
        this.book_price = book_price;
        this.book_innerurl = book_innerurl;
        this.book_sub_info = book_sub_info;
    }

    public String getBook_imgurl() {
        return book_imgurl;
    }
    public String getBook_innerurl() {
        return book_innerurl;
    }
    public String getBook_name() {
        return book_name;
    }
    public String getBook_price() {
        return book_price;
    }
    public String getBook_sub_info() {
        return book_sub_info;
    }


}