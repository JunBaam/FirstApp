package com.example.afinal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.kakaolink.v2.KakaoLinkCallback;
import com.kakao.kakaolink.v2.KakaoLinkService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CompleteActivity extends AppCompatActivity {


    String cover , name ,author,page,date,category, position,dateEnd,review;
    ImageView bookCover;
    TextView bookName ,bookAuthor, bookPage,bookStart, bookEnd ,bookCategory,reviewContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete);
        ActionBar ab = getSupportActionBar() ;
        ab.hide();


        /*    HomeActivity로부터 받은 책정보를 이미지뷰,텍스트뷰에 뿌려준다  */
        Intent intent =getIntent();
        cover=intent.getStringExtra("bookcover");
        name= intent.getStringExtra("bookname");
        author=intent.getStringExtra("bookauthor");
        page=intent.getStringExtra("bookpage");
        date=intent.getStringExtra("bookdate");
        dateEnd=intent.getStringExtra("bookend");
        category=intent.getStringExtra("bookcategory");
        position=intent.getStringExtra("position");
        review=intent.getStringExtra("review");

        System.out.println("@@completeActivity"+cover + name +author+page+date+category+ position
        +dateEnd);

        bookCover =findViewById(R.id.complete_bookimage);
        bookCover.setImageURI(Uri.parse(cover));

        bookName=findViewById(R.id.complete_bookname);
        bookName.setText(name);

        bookAuthor=findViewById(R.id.complete_bookauthor);
        bookAuthor.setText(author);

        bookCategory=findViewById(R.id.complete_bookcategory);
        bookCategory.setText(category);

        bookPage=findViewById(R.id.complete_bookpage);
        bookPage.setText(page);

        bookStart=findViewById(R.id.complete_bookstart);
        bookStart.setText(date);

        bookEnd=findViewById(R.id.complete_bookend);
        bookEnd.setText(dateEnd);


          //독서후기를 저장하는 Shared
        SharedPreferences reviewShared = getSharedPreferences("책리뷰", MODE_PRIVATE);
        String getReview =reviewShared.getString(review,"");

        System.out.println("리뷰리뷰"+getReview );

        reviewContext=findViewById(R.id.complete_review_con);
        reviewContext.setText(getReview);


    }












}
