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

import com.airbnb.lottie.LottieAnimationView;
import com.example.afinal.crawling.BestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import static java.lang.String.*;

public class NowreadingActivity extends AppCompatActivity  {

    private static final String TAG = "@@NowreadingActivity";

    //Recyclerview
    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_THREE = 3;
    RecyclerView recyclerView;
    NowreadingAdapter nowreadingAdapter;
    GridLayoutManager gridLayoutManager;


    ArrayList<NowreadingData> nowreadingDataArrayList;//현재읽고있는책 arraylist
    ArrayList<NowreadingData>finishReadingArrayList;  //다읽은책 arraylist
    NowreadingData nowreadingData;                  //책에대한 정보



    private String Tag = "NowreadingActivity";
    Button NewBook;     //새로운 책추가 버튼
    Activity activity;
    //하단 5개 버튼
    ImageView Home , Reading, Memo, MyPage;  Button Best;
    private Context mContext;
    //새로운책에대한정보
    String cover,title,author ,page ,date ,category;
    int index;
    Uri modifyUri;
    //최종적으로 받는값
    String Nname , Ccover,Aauthor ,Ppage, Ddate,Ccategory ,Pposition;
    SharedPreferences book_data;String sKey;






    private static final int REQUEST_CODE = 777;  //ReadingActivity requset code
    private static final int MODIFY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nowreading);
        mContext=this;
        activity = this;
        ActionBar actionbar = getSupportActionBar();
        //  ab.show() ;
        actionbar.hide();
        fivemenu_btn(); //하단 5개 버튼
        initItemsData();   //데이터를 집어넣는다.
        MakeRecyclerview(); //리사이클러뷰생성
        loadData(); //책꽂이(NowreaindgActivity)에 저장된 데이터를 불러온다



        //  check = false;
        //현재읽고있는책 페이지를 열면 shared를 통해 저장된 값을 불러옴
//        intent.putExtra("index" ,(arrayList.get(position).index));
//        nowreadingDataArrayList.get(page)

        //리사이클러뷰 아이템 클릭을 가능하게해줌
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            //클릭시 상세정보액티비티(NowreadingDetailActivity)를 띄움
            @Override
            public void onClick(View view, int position) {
                Log.w("책꽂이에서 보내는 포지션", String.valueOf(position));

                NowreadingData data = nowreadingDataArrayList.get(position);
                Toast.makeText(getApplicationContext(), data.getBookname() + "상세정보", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NowreadingActivity.this, NowreadingDetailActivity.class);

                //intent로  nowreadingDataArrayList의 해당 포지션에 맞는
                //책정보를 보낸다.
                intent.putExtra("bookname",nowreadingDataArrayList.get(position).bookname);
                intent.putExtra("bookauthor",nowreadingDataArrayList.get(position).bookauthor);
                intent.putExtra("bookpage",nowreadingDataArrayList.get(position).bookpage);
                intent.putExtra("bookdate",nowreadingDataArrayList.get(position).bookdate);
                intent.putExtra("bookcategory",nowreadingDataArrayList.get(position).bookcategory);
                intent.putExtra("bookcover",nowreadingDataArrayList.get(position).bookcover);
                intent.putExtra("position",  String.valueOf(position));

                intent.putExtra("check",nowreadingDataArrayList.get(position).check);

                intent.putExtra("index" ,nowreadingDataArrayList.get(position).index);

                //               SharedPreferences bookdata = getSharedPreferences("bookInfo", MODE_PRIVATE);
                //               String json = bookdata.getString("BookData", null); //저장된 값을 꺼내온다.

                //               try {
                //제이슨 객체를 가져옴.
                //                   JSONObject jsonObject = new JSONObject(json);
                //                   //jsonobject로부터 key값으로 arraylist를 가져옴.
                //                   JSONArray jArray = jsonObject.getJSONArray("BookData");
                //                  System.out.println("@@@@제이슨Array "+ jArray) ;

                //                 for(int i =0; i<jArray.length(); i++) {
                //                     //jsonlist에서 오브젝트를 가져옴
                //                     JSONObject subObject = jArray.getJSONObject(i);
                //                     System.out.println("@@@@제이슨오브젝트" +  subObject);

                //책이미지 ,이름 ,저자 ,페이지 ,날짜 ,카테고리 ,포지션
//                        intent.putExtra("bookcover", subObject.getString("bookcover"));
//                        intent.putExtra("bookauthor",subObject.getString("bookauthor"));
//                        intent.putExtra("bookpage",subObject.getString("bookpage"));
//                        intent.putExtra("bookdate",subObject.getString("bookdate"));
//                        intent.putExtra("bookcategory", subObject.getString("bookcategory"));
//                        intent.putExtra("position", data.getClass());

                //                }
                //             } catch (JSONException e) {
                //                 e.printStackTrace();
                //              }
                startActivityForResult(intent,MODIFY);

            } //on click


            //롱클릭시 삭제 다이얼로그를 띄움.
            @Override
            public void onLongClick(View view, final int position) {
                // 다이얼로그 바디
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                // 다이얼로그 메세지
                alertDialog.setMessage("정말삭제하시겠어요?");
                // 삭제버튼
                alertDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //data 현재 리스트 포지션
                        NowreadingData data = nowreadingDataArrayList.get(position);
                        Toast.makeText(getApplicationContext(), data.getBookname() + "을 삭제했습니다.", Toast.LENGTH_LONG).show();
                        Log.d("몇번쨰위치하는거야?", String.valueOf(nowreadingDataArrayList.get(position)));

                        nowreadingDataArrayList.remove(position);   //그 position의 아이템을 삭제한다.
                        nowreadingAdapter.notifyDataSetChanged();   //어댑터에 데이터변경을 알려준다.

                        SharedPreferences bookdata = getSharedPreferences("bookInfo", MODE_PRIVATE);
                        String json = bookdata.getString("BookData", null); //저장된 값을 꺼내온다.
                        try {
                            //제이슨 객체를 가져옴.
                            JSONObject jsonObject = new JSONObject(json);
                            //jsonobject로부터 key값으로 arraylist를 가져옴.
                            JSONArray jArray = jsonObject.getJSONArray("BookData");
                            System.out.println("@@@@제이슨Array " + jArray);
                            jArray.remove(position);
                            saveData();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SharedPreferences bookProgress = getSharedPreferences("bookProgress", MODE_PRIVATE);
                        SharedPreferences.Editor progressRemove =bookProgress.edit();

                        System.out.println("독서진행상황삭제 포지션 " + position);
                        progressRemove.remove(String.valueOf(position));
                        progressRemove.apply();



                    }//onclick
                });

                // 취소버튼
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(activity, "취소 합니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                // 메인 다이얼로그 생성
                AlertDialog alert = alertDialog.create();
                // 다이얼로그 보기
                alert.show();
            }
        }));

        //뷰를 바꿔주는 버튼
        //checkbox_check_chage_btn
        CheckBox changeView = findViewById(R.id.nowreading_change);
        changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (gridLayoutManager.getSpanCount() == SPAN_COUNT_ONE) {
                    gridLayoutManager.setSpanCount(SPAN_COUNT_THREE);
                } else {
                    gridLayoutManager.setSpanCount(SPAN_COUNT_ONE);
                }
                nowreadingAdapter.notifyItemRangeChanged(0, nowreadingAdapter.getItemCount());
            }


        });

        //새로운책추가 버튼
        //대문자
        NewBook = findViewById(R.id.nowreading_newbook);
        NewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadingnewActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        Log.w("@@NowreadingActivity" , "onCreate!!");
    } //onCreate


    @Override
    protected void onResume() {
        super.onResume();

        Log.w("@@NowreadingActivity" , "onResume!!");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.w("@@NowreadingActivity" , "onRestart!!");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.w("@@NowreadingActivity" , "onStart!!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("@@NowreadingActivity" , "onPause!!");
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop!! ");

        //saveData();
        super.onStop();

    }

    //새로운책의 정보를 받아오고
    //리사이클러뷰를 추가시킨다.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==REQUEST_CODE && resultCode ==RESULT_OK){

            Log.d("@@NowreadingActivity", valueOf(REQUEST_CODE));
            // String cover = data.("cover");
            Uri uri =data.getParcelableExtra("bookcover");
            cover = uri.toString();
            title =data.getStringExtra("bookname");
            author = data.getStringExtra("bookauthor");
            page =data.getStringExtra("bookpage");
            date = data.getStringExtra("bookdate");
            category = data.getStringExtra("bookcategory");
            index =data.getIntExtra("index",0);

            System.out.println("인덱스값"+index);
//          TextView textView =findViewById(R.id.hidden);
//          textView.setText("book");

            //리사이클러뷰 데이터 객체
            //책커버 제목 저자 카테고리 날짜 페이지 index(독서완료여부 ,0은 안읽음 1 읽음)  순
            NowreadingData nowreadingData = new NowreadingData(cover,title,author,category
                    ,date,page,index,randomkeygenerator());
            nowreadingDataArrayList.add(nowreadingData);            //리사이클러뷰 추가.
            nowreadingAdapter.notifyDataSetChanged();              //바뀐값을 Adapter에 알려준다.
            saveData();

        }
        //책정보수정
        if(requestCode ==MODIFY && resultCode ==RESULT_OK) {

            System.out.println("@@백버튼이 눌리고 값이들어옴");

            modifyUri = data.getParcelableExtra("Ccover");
            Ccover = data.getStringExtra("Ccover");
            Nname=   data.getStringExtra("Nname");
            Aauthor=    data.getStringExtra("Aauthor");
            Ppage=   data.getStringExtra("Ppage");
            Ddate= data.getStringExtra("Ddate");
            Ccategory= data.getStringExtra("Ccategory");


            Pposition=data.getStringExtra("Pposition");

            Log.w("최종수정값" , Ccover +Nname+Aauthor+Ppage+Ddate+Ccategory+Pposition);

            nowreadingData =new NowreadingData(Ccover ,
                    Nname,Aauthor,Ccategory,Ddate,Ppage,0,randomkeygenerator());

            nowreadingDataArrayList.set(Integer.parseInt(Pposition),nowreadingData);
            nowreadingAdapter.notifyDataSetChanged();
            saveData();

        }

    } //ActivityResult





    private  void  loadData() {

        if (nowreadingDataArrayList == null) {
            nowreadingDataArrayList= new ArrayList<>();
        }
        if(finishReadingArrayList==null){
            finishReadingArrayList = new ArrayList<>();
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
                NowreadingData nowreadingData = new NowreadingData(subObject.getString("bookcover"),
                        subObject.getString("bookname") , subObject.getString("bookauthor")
                        ,subObject.getString("bookcategory"),subObject.getString("bookdate")
                        ,subObject.getString("bookpage"),subObject.getInt("bookindex")
                     ,subObject.getString("check"));

                /*
                bookindex의 값이 0이면
                nowreadingDataArrayList 에 값을추가하고

                bookindex의 값이 1이면
                finishReadingArrayList 에 값을 추가한다

                 */
                if(subObject.getInt("bookindex")==0) {
                    nowreadingDataArrayList.add(nowreadingData);
                    nowreadingAdapter.notifyDataSetChanged();


                    //1인 값들도 추가.
                }else  if(subObject.getInt("bookindex")==1) {
                    finishReadingArrayList.add(nowreadingData);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void saveData(){

        SharedPreferences bookdata = getSharedPreferences("bookInfo",MODE_PRIVATE);
        SharedPreferences.Editor bookeditor =bookdata.edit();

        JSONObject mainObject =new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < nowreadingDataArrayList.size(); i++)//배열
            {
                JSONObject  subObject = new JSONObject();  //배열 내에 들어갈 json
                subObject.put("bookname", nowreadingDataArrayList.get(i).getBookname());
                subObject.put("bookcover", nowreadingDataArrayList.get(i).getBookcover());
                subObject.put("bookauthor", nowreadingDataArrayList.get(i).getBookauthor());
                subObject.put("bookcategory", nowreadingDataArrayList.get(i).getBookcategory());
                subObject.put("bookdate", nowreadingDataArrayList.get(i).getBookdate());
                subObject.put("bookpage", nowreadingDataArrayList.get(i).getBookpage());
                subObject.put("bookindex", nowreadingDataArrayList.get(i).getIndex());
                subObject.put("check",nowreadingDataArrayList.get(i).getCheck());

                jArray.put(subObject);

                //position.subObject
                Log.i("데이터책이름",nowreadingDataArrayList.get(i).getBookname());
                Log.i("데이터책이미지",  nowreadingDataArrayList.get(i).getBookcover());
                System.out.println( "데이터리스트 " +nowreadingDataArrayList.size());

            }


            for (int i = 0; i <finishReadingArrayList.size(); i++)//배열
            {
                JSONObject  subObject = new JSONObject();  //배열 내에 들어갈 json
                subObject.put("bookname",finishReadingArrayList.get(i).getBookname());
                subObject.put("bookcover",finishReadingArrayList.get(i).getBookcover());
                subObject.put("bookauthor",finishReadingArrayList.get(i).getBookauthor());
                subObject.put("bookcategory",finishReadingArrayList.get(i).getBookcategory());
                subObject.put("bookdate",finishReadingArrayList.get(i).getBookdate());
                subObject.put("bookpage",finishReadingArrayList.get(i).getBookpage());
                subObject.put("bookindex",finishReadingArrayList.get(i).getIndex());

                jArray.put(subObject);
            }

            mainObject.put("BookData", jArray);  //배열을 넣음
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("llllll", String.valueOf(mainObject));
        bookeditor.putString("BookData", String.valueOf(mainObject));
        bookeditor.apply();

    }

    //데이터를 넣는 메서드
    private void initItemsData() {
        nowreadingDataArrayList = new ArrayList<>();
        // nowreadingDataArrayList.add(new NowreadingData("" ,"파란색","안준범","book"));
    }

    //리사이클러뷰 정보 메서드
    private  void  MakeRecyclerview(){

        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT_THREE);  //SPAN_COUNT에 따라 뷰를 몇개씩 보여줄건지 결정한다.
        nowreadingAdapter = new NowreadingAdapter(nowreadingDataArrayList, gridLayoutManager,this);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setAdapter(nowreadingAdapter);        //nowreadingAdapter와 연결
        recyclerView.setLayoutManager(gridLayoutManager); //그리드레이아웃설정
        recyclerView.setNestedScrollingEnabled(true); //리사이클러뷰 스크롤을 부드럽게해줌
    }




    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    //리사이클러뷰 터치 이벤트 메서드
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private NowreadingActivity.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final NowreadingActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    //랜덤키메서드
    private static final String ALPHA_NUMERIC_STRING = "0123456789";
    public static String randomkeygenerator() {
        int count = 8;
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
    //하단 5개메뉴 버튼
    private  void  fivemenu_btn(){

        Home = findViewById(R.id.nowreading_home);
        Reading = findViewById(R.id.nowreading_nowreading);
        Best = findViewById(R.id.nowreading_best);
        Memo =findViewById(R.id.nowreading_memo);
        MyPage = findViewById(R.id.nowreading_mypage);

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
