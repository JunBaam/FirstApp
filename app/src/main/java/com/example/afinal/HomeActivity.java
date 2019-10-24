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

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.example.afinal.crawling.BestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.airbnb.lottie.network.FileExtension.JSON;


public class HomeActivity extends AppCompatActivity implements HomeAdapter.RecyclerViewClickListener {

    //Recyclerview
    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_THREE = 3;
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    GridLayoutManager gridLayoutManager;
    ArrayList<HomeData> homeDataArrayList;
    HomeData homeData;

    NowreadingData nowreadingData;                  //책에대한 정보

    Activity activity;
    Context mContext;
    String TAG ="llllHome";
    String fin;
    JSONObject json ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mContext = this;
        activity = this;

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        loadData();  //다읽은책 정보를 불러온다
        fivemenu_btn(); //하단 5개 메뉴버튼
        MakeRecyclerview(); //리사이클러뷰생성



    /*
    intent로 받은 JsonObject(책의정보) 가  true일때, 리사이클러뷰를 추가하고  쉐어드에 저장한다.
    */

        //현재시간을 가져오는 메서드  년,월 일
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy년MM월dd일 ", java.util.Locale.getDefault());
        Date date = new Date();
        String endTime = dateFormat.format(date);



        Intent intent = getIntent();
        fin = intent.getStringExtra("다읽은책");
        System.out.println("@@@@" + fin);

        if(intent.hasExtra("다읽은책") == true) {
                try {
                    json = new JSONObject(fin);
                    //  System.out.println("@@@@home" + json);
                    String bookname = json.getString("bookname");   //책이름
                    String bookauthor = json.getString("bookauthor");   //책이름    //책저자
                    String bookcover = json.getString("bookcover");   //책이름    //책커버 이미지
                    String bookcategory = json.getString("bookcategory");   //책이름 //책분류
                    String bookdate = json.getString("bookdate");   //책이름    //책날짜
                    String bookpage = json.getString("bookpage");   //책이름     //책페이지


                    HomeData homeData = new HomeData(bookcover, bookname,
                            bookauthor, bookcategory, bookdate, bookpage, endTime, 0, true);

                    System.out.println("@@@@현재시간" + endTime );
                    System.out.println("@@@@현재시간" + homeData );

                    if (homeDataArrayList == null) {
                        homeDataArrayList = new ArrayList<>();
                    }
                    homeDataArrayList.add(homeData);
                    homeAdapter.notifyDataSetChanged();

                    saveData();

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

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
        saveData();
    }

   //다읽은책 쉐어드저장
    public void saveData(){

        if (homeDataArrayList == null) {
            homeDataArrayList= new ArrayList<>();
        }
        SharedPreferences bookdata = getSharedPreferences("다읽은책",MODE_PRIVATE);
        SharedPreferences.Editor bookeditor =bookdata.edit();
        JSONObject mainObject =new JSONObject();


        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < homeDataArrayList.size(); i++)//배열
            {
                JSONObject  subObject = new JSONObject();  //배열 내에 들어갈 json
                subObject.put("bookname", homeDataArrayList.get(i).getBookname());
                subObject.put("bookcover", homeDataArrayList.get(i).getBookcover());
                subObject.put("bookauthor", homeDataArrayList.get(i).getBookauthor());
                subObject.put("bookcategory", homeDataArrayList.get(i).getBookcategory());
                subObject.put("bookdate", homeDataArrayList.get(i).getBookdate());
                subObject.put("bookpage", homeDataArrayList.get(i).getBookpage());
                subObject.put("bookend", homeDataArrayList.get(i).getBookend());
                subObject.put("bookindex",homeDataArrayList.get(i).getIndex());

                jArray.put(subObject);

                //position.subObject
                Log.i("데이터책이름",homeDataArrayList.get(i).getBookname());
                Log.i("데이터책이미지",  homeDataArrayList.get(i).getBookcover());
                System.out.println( "데이터리스트 " +homeDataArrayList.size());
            }


            mainObject.put("BookFinish", jArray);  //배열을 넣음
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("llllll", String.valueOf(mainObject));
        bookeditor.putString("BookFinish", String.valueOf(mainObject));
        bookeditor.apply();

    }


    private  void  loadData() {

        if ( homeDataArrayList == null) {
            homeDataArrayList= new ArrayList<>();
        }


        SharedPreferences bookdata = getSharedPreferences("다읽은책", MODE_PRIVATE);
        String json = bookdata.getString("BookFinish", null); //저장된 값을 꺼내온다.
        try {
            //제이슨 객체를 가져옴.
            JSONObject jsonObject = new JSONObject(json);
            //jsonobject로부터 key값으로 arraylist를 가져옴.
            JSONArray jArray = jsonObject.getJSONArray("BookFinish");
            System.out.println("@@@@제이슨Array "+ jArray) ;

            for(int i =0; i<jArray.length(); i++) {
                //jsonlist에서 오브젝트를 가져옴
                JSONObject subObject = jArray.getJSONObject(i);
                System.out.println("@@@@제이슨오브젝트" +  subObject);
                //책커버 ,책이름 ,저자 ,카테고리,날짜 ,페이지,다읽은 날짜 순


                HomeData homeData =new HomeData(subObject.getString("bookcover"),
                        subObject.getString("bookname") , subObject.getString("bookauthor")
                        ,subObject.getString("bookcategory"),subObject.getString("bookdate")
                        ,subObject.getString("bookpage"),subObject.getString("bookend"),
                        subObject.getInt("bookindex"),false);

                    homeDataArrayList.add(homeData);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    //어댑터에서 클릭하는 메서드
    @Override
    public void onItemClicked(int position) {
        Intent intent =new Intent(getApplicationContext() ,CompleteActivity.class);

         //책이름 ,책저자 , 페이지 ,날짜 ,카테고리 ,커버이미지 ,position

        intent.putExtra("bookname",homeDataArrayList.get(position).bookname);
        intent.putExtra("bookauthor",homeDataArrayList.get(position).bookauthor);
        intent.putExtra("bookpage",homeDataArrayList.get(position).bookpage);
        intent.putExtra("bookdate",homeDataArrayList.get(position).bookdate);
        intent.putExtra("bookcategory",homeDataArrayList.get(position).bookcategory);
        intent.putExtra("bookcover",homeDataArrayList.get(position).bookcover);
        intent.putExtra("position",  String.valueOf(position));

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
                Intent intent = new Intent(getApplicationContext(),ChartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NowreadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
