package com.example.afinal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class NowreadingDetailActivity extends AppCompatActivity implements NowreadingDetailAdapter.RecyclerViewClickListener {

    //datepicker를 위한변수들
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth;
    Button btn_date;  //날짜선택버튼
    Activity activity;

    //현재 읽고있는책 리스트
    ArrayList<NowreadingData> nowreadingDataArrayList =new ArrayList<NowreadingData>();
    //독서진행상황 리스트
    ArrayList<NowreadingDetailData> detailDataArrayList =new ArrayList<NowreadingDetailData>();
    //독서진행상황 어댑터
    NowreadingDetailAdapter detailAdapter;
    NowreadingAdapter nowreadingAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    private static final int GET_GALLERY_IMAGE = 200; //갤리러 resultcode
    private static final int MODIFY = 1;              //책정보수정 resultcode

    String position; //Arraylist(현재읽고있는에대한) position
    //기존책의 정보
    String cover ,name ,author,page,date,category;
    TextView bookName, bookAuthor ,bookPage ,bookCategory, bookDate ;
    Button bookModify_btn , bookFinsih_btn;  //책정보수정 ,독서완료 버튼
    ImageView detaiImage;  //이미지 상세보기

    //수정된책정보
    String getCover, getTitle, getAuthor, getPage, getDate, getCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nowreadingdetail);
        ActionBar actionBar = getSupportActionBar();
        //  ab.show() ;
        actionBar.hide();
        activity=this;


       loadData();

        //책꽂이(NowreadingActivity)화면이 보낸 책정보를 받는다.
         Intent intent =getIntent();
        cover=intent.getStringExtra("bookcover");
        name= intent.getStringExtra("bookname");
        author=intent.getStringExtra("bookauthor");
        page=intent.getStringExtra("bookpage");
        date=intent.getStringExtra("bookdate");
        category=intent.getStringExtra("bookcategory");

         //책꽂이(NowreadingActivity로부터 position값을받는다
        position=intent.getStringExtra("position");

        Log.w("상세보기에서받은 포지션값", String.valueOf(position));

        System.out.println("상세정보받은값"+cover+"@@@@"+
                name +"@@@@"+author +"@@@@"+page+"@@@@"+date+"@@@@"+category);

        //받아온값 책정보값을 view에 뿌려준다.
        detaiImage =findViewById(R.id.detailbookimage);
        detaiImage.setImageURI(Uri.parse(cover));
        bookName=findViewById(R.id.detail_bookname);
        bookAuthor=findViewById(R.id.detail_bookauthor);
        bookPage =findViewById(R.id.detail_bookpage);
        bookCategory=findViewById(R.id.detail_category);
        bookDate=findViewById(R.id.detail_bookdate);

        bookName.setText(name);
        bookAuthor.setText(author);
        bookPage.setText(page);
        bookCategory.setText(category);
        bookDate.setText(date);

        //책정보수정버튼
        //클릭시 책정보수정화면(ReadingnewModifyActivity)로 값을 보낸다.
        bookModify_btn = findViewById(R.id.detail_modify_information);
        bookModify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modifyIntent = new Intent(getApplicationContext() ,ReadingnewModifyActivity.class);

                modifyIntent.putExtra("modifyName",name);
                modifyIntent.putExtra("modifyCover",cover);
                modifyIntent.putExtra("modifyPage",page);
                modifyIntent.putExtra("modifyCategory",category);
                modifyIntent.putExtra("modifyDate",date);
                modifyIntent.putExtra("modifyAuthor",author);
                startActivityForResult(modifyIntent , MODIFY);
            }
        });

        //독서완료버튼
        bookFinsih_btn=findViewById(R.id.detail_finish);
        bookFinsih_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent send = new Intent(getApplicationContext(),HomeActivity.class);

                SharedPreferences bookdata = getSharedPreferences("bookInfo", MODE_PRIVATE);
                SharedPreferences.Editor bookeditor = bookdata.edit();

            //    String json = bookdata.getString("BookData", null);
                              String json = bookdata.getString("NowRead", null);
                System.out.println(" 수정중:  " +  json);
                try {
                    //책의 정보를 가지고있는 jsonObject
                    JSONObject jsonObject =new JSONObject(json);
                    JSONArray jArray = jsonObject.getJSONArray("BookData");
                    /* position으로 내가원하는 jsonObject값을 가져온다
                       subObject(책정보가들어있는 객체)
                       에서  bookindex를 1로 바꿔준다.
                     */
                    //포지션에 맞는 jsonobject를 불러온다.
                    JSONObject subObject = jArray.getJSONObject(Integer.parseInt(position));


                    System.out.println(" 수정중: 내가선택한책의정보 " +  subObject);
                    //bookindex의 정보를 삭제하고 새로 값을 넣는다.
                 //   jArray.put(Integer.parseInt(position),subObject);
                //    System.out.println(" 수정중: jsonArray에담는다 " + jArray.put(Integer.parseInt(position),subObject));

                //    jsonObject.put("BookData", jArray.put(Integer.parseInt(position),subObject));
                 //   System.out.println(" 수정중: jsonobject에 담는다  " +   jsonObject.put("BookData", jArray.put(Integer.parseInt(position),subObject)));
                    //  Integer.parseInt(position) 내가 가져온 포지션값
                           jArray.remove(Integer.parseInt(position));

                      JSONArray finishArray = new JSONArray();
                         finishArray.put(Integer.parseInt(position),subObject);
                    System.out.println(" 수정중: jsonArray에담는다 " +    finishArray.put(subObject));

                    JSONObject finsihObject =new JSONObject();
                        finsihObject.put("BookData", finishArray);
                    System.out.println(" 수정중: jsonobject에 담는다  " +    finsihObject.put("BookData", finishArray));


                    bookeditor.putString("FinishBook", String.valueOf(jsonObject));
                    //해당 오브젝트삭제
                    bookeditor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                send.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(send);
                finish();
            }
        });

        //독서진행상황 리사이클러뷰만들기

        linearLayoutManager = new LinearLayoutManager(this);
        detailAdapter = new NowreadingDetailAdapter(detailDataArrayList,this);  //어댑터로 arraylist와 homeactivtiy를 보내줌
        detailAdapter.setOnClickListener(NowreadingDetailActivity.this);

        recyclerView = findViewById(R.id.detail_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));           //리니어 레이아웃매니저를 Borad에 적용
        recyclerView.setAdapter(detailAdapter);                                        // 리사이클러뷰에 셋팅




        //독서진행상황 추가버튼
        ImageView add_btn = findViewById(R.id.detail_add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();

            }
        });

        //백버튼  수정된 책정보를 보내준다.
        Button back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext() , NowreadingActivity.class);

                intent.putExtra("Nname",getTitle);
                intent.putExtra("Ccover",getCover);
                intent.putExtra("Aauthor",getAuthor);
                intent.putExtra("Ppage",getPage);
                intent.putExtra("Ddate",getDate);
                intent.putExtra("Ccategory",getCategory);
                intent.putExtra("Pposition",position);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });


    }// oncreate

    @Override
    protected void onStop() {
        super.onStop();

        saveData();

    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        System.out.println("@@백버튼이 눌립니다");
//        //Intent intent = new Intent(NowreadingDetailActivity.this, LodingActivity.class);
//
//    }

    //수정된 책의 정보를 받아옴
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MODIFY && resultCode == RESULT_OK) {

            // String cover = data.("cover");

            //책정보수정 (ReadingnewModifyActivity) 화면으로 부터 책정보를받음
            Uri uri = data.getParcelableExtra("returnCover");
            getCover = uri.toString();
            getTitle = data.getStringExtra("returnName");
            getAuthor = data.getStringExtra("returnAuthor");
            getPage = data.getStringExtra("returnPage");
            getDate = data.getStringExtra("returnDate");
            getCategory = data.getStringExtra("returnCategory");

            //받은 책정보를 뷰에 뿌려준다.
            bookName.setText(getTitle);
            bookAuthor.setText(getAuthor);
            bookPage.setText(getPage);
            bookCategory.setText(getCategory);
            bookDate.setText(getDate);
            detaiImage.setImageURI(Uri.parse(getCover));


            //     NowreadingData nowreadingData = new NowreadingData( getCover, getTitle,getAuthor,"book", getCategory
            //   ,getDate,getPage  );

            //   Log.d("@@DetailAcitivity" , String.valueOf(nowreadingData));
            //  nowreadingDataArrayList.set(Integer.parseInt(position),nowreadingData);
            //   saveData();
        }

    } //ActivityResult



    @Override
    public void onItemClicked(int position) {
    }

    //독서진행상황 삭제
    //다이얼로그를 띄워서 삭제 여부를 물어보고 삭제한다.
    @Override
    public void onRemoveClicked(final int position) {

        // 다이얼로그 바디
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        // 다이얼로그 메세지
        alertDialog.setMessage("정말삭제하시겠어요?");
        // 삭제버튼
        alertDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which ) {
                NowreadingDetailData detailData = detailDataArrayList.get(position);
                Toast.makeText(getApplicationContext() ,  detailData.getDate() +"을 삭제했습니다." , Toast.LENGTH_LONG).show();

                detailAdapter.removeItem(position);
                detailAdapter.notifyDataSetChanged();   //어댑터에 데이터변경을 알려준다.

            }
        });
        // 취소버튼
        alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, "취소 합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }



    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("bookProgress", MODE_PRIVATE);

        //인텐트로 position값을 받는다.
        Intent intent = getIntent();
        int progressKey =intent.getIntExtra("index",0);
       // String index = intent.getStringExtra("position");
        String to = Integer.toString(progressKey);


        String json =  sharedPreferences.getString(to, "");

        try {

            //JsonArray에 있는 오브젝트를 꺼내오기 위해서 for을 이용해서 JasonObject를 꺼내온다.
            JSONArray preoressArray =new JSONArray(json);

            for(int i=0; i<preoressArray.length(); i++) {

                Log.w("현재 저장된 정보 ", String.valueOf(preoressArray));
                JSONObject progressObject = preoressArray.getJSONObject(i);

                NowreadingDetailData nowreadingDetailData = new NowreadingDetailData(
                        progressObject.getString("bookPage")
                        , progressObject.getString("bookDate")
                );

                detailDataArrayList.add(nowreadingDetailData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (detailDataArrayList == null) {
            detailDataArrayList = new ArrayList<>();
            Log.e("독서진행상황 ", "독서진행상황 arraylist가 추가됩니다.");
        }

    }


    //독서진행상황을 저장한다.
    public void saveData(){
        //JasonArray   [ {},{} ] 형식으로 저장
        SharedPreferences bookdata = getSharedPreferences("bookProgress",MODE_PRIVATE);
        SharedPreferences.Editor bookeditor =bookdata.edit();

        Intent intent= getIntent();
        int progressKey =intent.getIntExtra("index",0);

        String to = Integer.toString(progressKey);
        Log.w("저장할 키값  ",   to );
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < detailDataArrayList.size(); i++)//배열
            {
                JSONObject  subObject = new JSONObject();  //배열 내에 들어갈 json Object
                subObject.put("bookDate", detailDataArrayList.get(i).getDate());
                subObject.put("bookPage", detailDataArrayList.get(i).getPage());
                jArray.put(subObject);
                //position.subObject
            }
           //key arraylist.size
            bookeditor.putString(to, jArray.toString());
            bookeditor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//    private void buildRecyclerView() {
//
//        recyclerView = findViewById(R.id.detail_rv);             //리사이클러뷰는 Borad
//        recyclerView.setHasFixedSize(true);
//        linearLayoutManager = new LinearLayoutManager(this);
//        detailAdapter = new NowreadingDetailAdapter(detailDataArrayList);  //어댑터로 arraylist와 homeactivtiy를 보내줌
//        recyclerView.setLayoutManager(linearLayoutManager);           //리니어 레이아웃매니저를 Borad에 적용
//      //  detailAdapter.setOnClickListener(NowreadingDetailActivity.this);
//        recyclerView.setAdapter(detailAdapter);   //homeAdpter 를 리사이클러뷰에 셋팅
//    }

    //날짜 , 페이지 선택하는 다이얼로그
    private void ShowDialog()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View dialogLayout = layoutInflater.inflate(R.layout.addpage_dialog , null);   //다이얼로그 레이아웃을 가져온다.
        final Dialog mydialog = new Dialog(NowreadingDetailActivity.this);

        mydialog.setContentView(dialogLayout);
        mydialog.show();

        //확인버튼 누를시 NowreadingDetailActivity(현재읽고있는책 액티비티)로  값(날짜선택, 페이지) 을 보낸다
        Button btn_ok =dialogLayout.findViewById(R.id.addpage_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText dialog_page = dialogLayout.findViewById(R.id.addpage_writepage); //다이얼로그 edittexdt값을 받아옴
                Button dialog_date = dialogLayout.findViewById(R.id.addpage_date);   //다이얼로그  날짜선택버튼  값을 받아옴

                String page = dialog_page.getText().toString();
                String date = dialog_date.getText().toString();

                //독서진행상황을 추가한다.
                NowreadingDetailData detailData = new NowreadingDetailData(page,date);
                detailDataArrayList.add(detailData);
                detailAdapter.notifyDataSetChanged();
                //추가한 데이터를 저장
                saveData();

                Toast.makeText(NowreadingDetailActivity.this,"독서진행상황 추가했습니다.", Toast.LENGTH_SHORT).show();
                mydialog.cancel();

            }
        });



        //날짜 선택 버튼
        btn_date = dialogLayout.findViewById(R.id.addpage_date);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NowreadingDetailActivity.this , "날짜를선택 합니다.", Toast.LENGTH_SHORT).show();

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(NowreadingDetailActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            //날짜를 지정해준다.
                            //이유는 모르겠는데 month가 하나적게나와서 +1 해줌
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                //날짜를 선택하고 btn_date버튼에 값을 입력함
                                btn_date.setText(year + "년" + (month + 1) + "월" + day + "일");

                                Log.d("@@@@@@Nowreading",btn_date.toString() );
                                Toast.makeText(getApplicationContext(), year + "년" + (month + 1) + "월" + day + "일", Toast.LENGTH_SHORT).show();
                            }
                            //현재 날짜에 맞춰준다.
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    } //showdialog



}
