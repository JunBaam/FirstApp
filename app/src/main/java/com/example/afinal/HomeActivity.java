package com.example.afinal;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.Transliterator;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.crawling.BestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity implements HomeAdapter.RecyclerViewClickListener {

    //Recyclerview
    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_THREE = 3;
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    GridLayoutManager gridLayoutManager;
    ArrayList<HomeData> homeDataArrayList;
    HomeData homeData;

    ArrayList<NowreadingData> nowreadingDataArrayList;//현재읽고있는책 arraylist
    ArrayList<NowreadingData>finishReadingArrayList;  //다읽은책 arraylist
    NowreadingData nowreadingData;                  //책에대한 정보

    Activity activity;
    Context mContext;
    String TAG ="llllHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mContext=this;
        activity = this;
        loadData();
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        // initItemsData();   //데이터를 집어넣는다.
        fivemenu_btn(); //하단 5개 버튼
        MakeRecyclerview(); //리사이클러뷰생성

        Log.i(TAG, "onCreate" );

        //뷰를 바꿔주는 버튼
        //checkbox_check_chage_btn
        CheckBox changeView = findViewById(R.id.home_change);
        changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (gridLayoutManager.getSpanCount() == SPAN_COUNT_ONE) {
                    gridLayoutManager.setSpanCount(SPAN_COUNT_THREE);
                } else {
                    gridLayoutManager.setSpanCount(SPAN_COUNT_ONE);
                }
                homeAdapter.notifyItemRangeChanged(0, homeAdapter.getItemCount());
            }


        });



    } //onCreate

    @Override
    public void onStop() {
        super.onStop();

    }


    private  void  loadData() {


        if ( homeDataArrayList == null) {
            homeDataArrayList= new ArrayList<>();
        }



        SharedPreferences bookdata = getSharedPreferences("bookInfo", MODE_PRIVATE);
        String json = bookdata.getString("BookData", null); //저장된 값을 꺼내온다.

        try {
            //제이슨 객체를 가져옴.

            JSONObject jsonObject = new JSONObject(json);
            //jsonobject로부터 key값으로 arraylist를 가져옴.
            JSONArray jArray = jsonObject.getJSONArray("BookData");
            System.out.println("@@@@제이슨Array "+ jArray) ;

            for(int i =0; i<jArray.length(); i++) {
                //jsonlist에서 오브젝트를 가져옴
                JSONObject subObject = jArray.getJSONObject(i);
                System.out.println("@@@@제이슨오브젝트" +  subObject);
                //책커버 ,책이름 ,저자 ,카테고리,날짜 ,페이지 순

                Log.i(TAG, subObject.getString("bookpage"));
                Log.i(TAG, String.valueOf(subObject.getInt("bookindex")));

                HomeData homeData =new HomeData(subObject.getString("bookcover"),
                        subObject.getString("bookname") , subObject.getString("bookauthor")
                        ,subObject.getString("bookcategory"),subObject.getString("bookdate")
                        ,subObject.getString("bookpage"),subObject.getInt("bookindex"),false);

                if(subObject.getInt("bookindex")==1) {
                    homeDataArrayList.add(homeData);
                }

                //   homeAdapter.notifyDataSetChanged();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }










    //어댑터에서 클릭하는 메서드
    @Override
    public void onItemClicked(int position) {
        Intent intent =new Intent(getApplicationContext() ,CompleteActivity.class);


        startActivity(intent);

    }

    @Override
    public void onModifyClicked(int position) {

    }

    @Override
    public void onRemoveClicked(int position) {

        Log.d("삭제버튼", position+"번째 삭제");
        homeAdapter.removeItem(position);
        homeAdapter.notifyDataSetChanged();

    }



    //리사이클러뷰 정보 메서드
    private  void  MakeRecyclerview(){
        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT_THREE);  //SPAN_COUNT에 따라 뷰를 몇개씩 보여줄건지 결정한다.
        homeAdapter = new HomeAdapter(homeDataArrayList, gridLayoutManager,this);
        homeAdapter.setOnClickListener(HomeActivity.this);   //리사이클러뷰 클릭가능하게해줌

        recyclerView = (RecyclerView) findViewById(R.id.home_rv);
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setLayoutManager(gridLayoutManager); //그리드레이아웃설정
        recyclerView.setNestedScrollingEnabled(true); //리사이클러뷰 스크롤을 부드럽게해줌

    }


    private void initItemsData() {
        homeDataArrayList = new ArrayList<>();
        // nowreadingDataArrayList.add(new NowreadingData("" ,"파란색","안준범","book"));
        homeDataArrayList.add(new HomeData("","파란색","안준범","",
                "","",1,false));

        homeDataArrayList.add(new HomeData("","퍼런","범","",
                "","",1,false));
        //  homeAdapter.notifyDataSetChanged();
    }



    //    //하단 다섯개 버튼
    private  void  fivemenu_btn(){
        ImageView Home = findViewById(R.id.home_home);
        ImageView Reading = findViewById(R.id.home_reading);
        Button Best = findViewById(R.id.home_best);
        ImageView Memo =findViewById(R.id.home_memo);
        ImageView MyPage = findViewById(R.id.home_mypage);

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
                Intent intent = new Intent(getApplicationContext(), BestActivity.class);
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
}//NowreadingActivity
