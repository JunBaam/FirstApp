package com.example.afinal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CompleteActivity extends AppCompatActivity {


    String cover , name ,author,page,date,category, position;
    ImageView bookCover;
    TextView bookName ,bookAuthor, bookPage,bookStart, bookEnd ,bookCategory;


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
        category=intent.getStringExtra("bookcategory");
        position=intent.getStringExtra("position");

        System.out.println("@@completeActivity"+cover + name +author+page+date+category+ position);

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

        //해당날자를 가져온다.
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy년MM월dd일 ", java.util.Locale.getDefault());
        Date date = new Date();
        String getDate = dateFormat.format(date);

        bookEnd =findViewById(R.id.complete_bookend);
        bookEnd.setText(getDate);







        //책정보 수정하기버튼
        Button BookModify = findViewById(R.id.complete_bookmodify_btn);
        BookModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CompleteModifyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


    }

}
