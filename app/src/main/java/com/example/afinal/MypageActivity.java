package com.example.afinal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

public class MypageActivity extends AppCompatActivity {


    Handler handler = new Handler() {
        //핸들러 메세지 전송 메서드.
        @Override
        public void handleMessage(Message msg) {
            updateThread();
        }
    };

    private int i = 0;  //핸들러 변수
    ImageView image_Ad; //광고이미지
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        ActionBar ab = getSupportActionBar() ;
        ab.hide() ;
        fivemenu_btn();



        //상담 통화버튼
        Button call_btn = findViewById(R.id.mypage_call);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-7105-9135"));
                startActivity(intent);  //액티비티 띄우기
            }
        });






        Thread ADthread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        //3초마다 이미지가 바뀜
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(3000);
                    } catch (Throwable t) {

                    }
                }
            }
        });
        //광고스레드
        ADthread.start();

    }//onCreate


    /*3개의 이미지를 보여주는 함수 updatethread
     이미지 클릭시 교보문고 인터넷연결
     */
    private void updateThread() {
        int mod = i % 3;

        //광고이미지
        image_Ad =findViewById(R.id.mypage_ad);

        switch (mod) {
            case 0:
                i++;
                image_Ad.setImageResource(R.drawable.new1);
                image_Ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791161570716&orderClick=43b"));
                        startActivity(intent);

                    }
                });
break;
            case 1:
                i++;
                image_Ad.setImageResource(R.drawable.new2);
               image_Ad.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent =new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791196797706&orderClick=43e"));
                       startActivity(intent);
                   }
               });

                break;
            case 2:
                i = 0;
                image_Ad.setImageResource(R.drawable.new3);
                image_Ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788954613965&orderClick=43d"));

                        startActivity(intent);
                    }
                });
                break;
        }

    } //updateThread


    private  void  fivemenu_btn(){

        //하단 버튼 5개
        ImageView Home = findViewById(R.id.mypage_home);
        ImageView Reading = findViewById(R.id.mypage_reading);
        Button Best = findViewById(R.id.mypage_best);
        ImageView Memo =findViewById(R.id.mypage_memo);
        ImageView MyPage = findViewById(R.id.mypage_mypage);



        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        Best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BestSellerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        Memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MemoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        Reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NowreadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        MyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MypageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


    }

}
