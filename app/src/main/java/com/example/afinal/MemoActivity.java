package com.example.afinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MemoActivity extends AppCompatActivity {

    ImageView Home, Reading, Memo, MyPage  ;  //하단 메뉴버튼 ,메모추가버튼
    Button Best;                //하단 Best버튼

    private static final int REQUEST_CODE = 200;  //메모작성  resultcode
    private static final int MODIFY_CODE = 2;    //메모수정 resultcode

    ListView listview;

  Activity activity;
     MemoAdapter memoAdapter = new MemoAdapter();
  ArrayList<MemoData> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo);
          activity=this;
        ActionBar ab = getSupportActionBar();
        //  ab.show() ;     // 앱바(App Bar) 보이기.
        ab.hide();     // 앱바(App Bar) 감추기.
       fivemenu_btn();   //하단 5개 메뉴버튼



        // 리스트뷰 참조 및 Adapter달기
       listview=findViewById(R.id.memo_Lv);
        listview.setAdapter(memoAdapter);

        // 임시로 아이템추가
        memoAdapter.addItem("이것은 메모 ");


        //아이템추가 버튼
        ImageView addButton = findViewById(R.id.memoadd_btn);
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MemoWriteActivity.class);
                startActivityForResult(intent, 200);

            }
        }); //additme button

       //parent : ListView 자체에 대한 참조.
        //view : 클릭이 발생한 View에 대한 참조.
        //position : Adapter에서의 view의 position.
        //id : 클릭된 아이템의 row id.
// parent를 통해 getItemAtPosition() 함수를 사용하면 position에 해당하는 아이템 데이터를 가져올 수 있다.
      listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView parent, View v, int position, long id) {

            Intent intent = new Intent(getApplicationContext(), MemoModifyActivity.class);

             //메모 제목을 putextra로 보냄
              TextView text_modify =findViewById(R.id.memo_listview_btn);
              String  modify =text_modify.getText().toString();
              intent.putExtra("title" ,modify );

           startActivityForResult(intent ,  MODIFY_CODE );


              MemoData memoData = (MemoData) parent.getItemAtPosition(position);
              String title = memoData.getTitle();

              Toast.makeText(getApplicationContext(),title , Toast.LENGTH_LONG).show();

          }
      });


       //리스트뷰를 오래 누를시  삭제 다이얼로그를 띄움.
    listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(final AdapterView parent, View v, final int position, long id) {

            // 다이얼로그 바디
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(activity);
            // 다이얼로그 메세지
            alertdialog.setMessage("정말삭제하시겠어요?");
            // 삭제버튼
            alertdialog.setPositiveButton("삭제", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which ) {

                    memoAdapter.removeItem(position); //해당위치 데이터를 삭제
                   memoAdapter.notifyDataSetChanged();   //어댑터에 데이터변경을 알려준다.
                }
            });
            // 취소버튼
            alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(activity, "취소 합니다.", Toast.LENGTH_SHORT).show();
                }
            });
            // 메인 다이얼로그 생성
            AlertDialog alert = alertdialog.create();
            // 다이얼로그 보기
            alert.show();
            return true;
        }
    });


        }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode==RESULT_OK ){

        TextView  memotitle = findViewById(R.id.memo_listview_btn);
        String rmemotitle = data.getStringExtra("TITLE");


            Log.d("MemoActivity"  , "제목 타이틀값을 받습니다 " +rmemotitle );
            //            int count;
          //              count = adapter.getCount();
        //     아이템 추가.

            memoAdapter.addItem(rmemotitle);

            // listview 갱신
            memoAdapter.notifyDataSetChanged();

        }  //memo 추가
        if(requestCode == MODIFY_CODE && resultCode==RESULT_OK ){


            String rmemotitle = data.getStringExtra("modifytitle");

            System.out.println( rmemotitle);
            Toast.makeText(getApplicationContext(), "수정내용: " + rmemotitle, Toast.LENGTH_LONG).show();

            TextView  memotitle = findViewById(R.id.memo_listview_btn);


            memotitle.setText(rmemotitle);


        }

    }


    private  void  fivemenu_btn(){



        Home = findViewById(R.id.memo_home);
        Reading = findViewById(R.id.memo_reading);
        Best = findViewById(R.id.memo_best);
        Memo =findViewById(R.id.memo_memo);
        MyPage = findViewById(R.id.memo_mypage);

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



}//MemoActivity
