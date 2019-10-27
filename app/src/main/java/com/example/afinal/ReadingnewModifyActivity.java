package com.example.afinal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;



public class ReadingnewModifyActivity extends AppCompatActivity {
    Uri imageUri; //사진 uri 값


    //날짜정하기
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int year ,month, dayOfMonth;

    String name ,cover ,page, category,date,author,check;
    Button modifyDate;
    ImageView modifyImage;
    EditText modifyName ,modifyPage ,modifyAuthor;
    Spinner spinner;
    //수정한 값을 되돌려준다.
    String returnTitle ,returnAuthor,returnPage,returnDay,returnCategory;


    ArrayList<NowreadingData> nowreadingDataArrayList;
    private static final int GET_GALLERY_IMAGE = 200; //갤리러 resultcode

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readingnew_modify);

        ActionBar actionBar = getSupportActionBar();
        //  ab.show() ;     // 앱바(App Bar) 보이기.
        actionBar.hide();     // 앱바(App Bar) 감추기.
               date();//날짜고르기 버튼 메서드


       //책내용 수정하기버튼  , 클릭시 상세정보 페이지로 돌아감
        Button modifyFinish = findViewById(R.id.readingnew_modify_writefinish);
        modifyFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),NowreadingDetailActivity.class);

                // 사용자가  작성한값을 가져온다.
           //    Uri returnCover = modifyImage.setImageURI();

                returnTitle =   modifyName.getText().toString();
                returnAuthor = modifyAuthor.getText().toString();
                returnPage = modifyPage.getText().toString();
                returnDay = modifyDate.getText().toString();
               returnCategory = spinner.getSelectedItem().toString();

                System.out.println("카테고리선택값" + returnCategory);
                //  intent.putExtra("카메라사진",tempFile);
                 intent.putExtra("returnCategory",   returnCategory);
                intent.putExtra("returnCover", imageUri);
                intent.putExtra("returnName", returnTitle);
                intent.putExtra("returnAuthor",  returnAuthor);
                intent.putExtra("returnPage",returnPage);
                intent.putExtra("returnDate",  returnDay);



                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });


        //보낸값을받는다.
        Intent get = getIntent();
        name = get.getStringExtra("modifyName");
        cover=get.getStringExtra("modifyCover");
        page=get.getStringExtra("modifyPage");
        category=get.getStringExtra("modifyCategory");
        date=get.getStringExtra("modifyDate");
        author=get.getStringExtra("modifyAuthor");

        System.out.println("받은값"+cover+"@@@@"+
                name +"@@@@"+author +"@@@@"+page+"@@@@"+date+"@@@@"+category);


        //각 뷰에 원래 값들을 뿌려준다.
         modifyImage = findViewById(R.id.readingnew_modify_image);
        modifyImage.setImageURI(Uri.parse(cover));
         //날짜
        modifyDate=findViewById(R.id.readingnew_modify_date);
        modifyDate.setText(date);
        //책이름 ,페이지 ,저자 에 값을 부려준다,
        modifyName=findViewById(R.id.readingnew_modify_booktitle);
        modifyName.setText(name);
        modifyPage=findViewById(R.id.readingnew_modify_bookpage);
        modifyPage.setText(page);
        modifyAuthor=findViewById(R.id.readingnew_modify_bookauthor);
        modifyAuthor.setText(author);
        spinner=findViewById(R.id.readingnew_modify_spinner);



        //갤러리에서 사진을 가져옴.
        ImageView gallery = findViewById(R.id.readingnew_modify_gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });



    }//onCreate


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //만일 요청신호 , 결과신호 ,  비어있지 않다면.
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK ) {
            //이미지의 Uri 경로를 얻어온다
            imageUri = data.getData();
            //가져온 Uri경로를 bookcover 이미지뷰에 뿌려준다.
            ImageView modify_image = findViewById(R.id.readingnew_modify_image);
            modify_image.setImageURI(imageUri);
        }

    } //onActivityResult


    private void date(){
     final Button choiceDate = findViewById(R.id.readingnew_modify_date);
        choiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year= calendar.get(Calendar.YEAR);
                month =calendar.get(Calendar.MONTH);
                dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(ReadingnewModifyActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            //날짜를 지정해준다.
                            //이유는 모르겠는데 month가 하나적게나와서 +1 해줌
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                choiceDate.setText(year+"년"+(month+1)+"월"+day+"일");
                            }
                            //현재 날짜에 맞춰준다.
                        } , year,month,dayOfMonth );
                datePickerDialog.show();
            }
        });

    }

}
