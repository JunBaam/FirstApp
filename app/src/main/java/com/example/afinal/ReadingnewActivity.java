package com.example.afinal;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReadingnewActivity extends AppCompatActivity {


    Uri imageUri;      //갤러리 uri
    File tempFile;
    TextView tv_result;

    //날짜정하기
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int year ,month, dayOfMonth;
    private Boolean isCamera = false;

    ImageView bookcover ;          //책이미지
    Spinner spinner ;            //책분류 스피너
    Button date ,finsih_btn;       //날짜선택버튼  , 작성완료버튼
    EditText bookname ,bookauthor ,bookpage;   //책제목 ,책저자 ,총페이지수

    private Context mContext;
   //채로운책 작성값.
    String  author, page, title, category, day;
    //shared값을 저장하는변수.
    String shared_bookdata;


    ArrayList<NowreadingData> nowreadingDataArrayList= new ArrayList<NowreadingData>();
    NowreadingData nowreadingData;


    private static final int GET_GALLERY_IMAGE = 200;   // 갤러리 resultcode
    private static final int PICK_FROM_CAMERA = 2;      // 카메라 resultcode
    private static final String TAG = "@@ReadingnewActivity";
    private  static  final  String KEY ="KEY";           //화면회전 resultcode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readingnew);



mContext=this;
        bookcover = findViewById(R.id.readingnew_image);          //책이미지
        spinner = findViewById(R.id.readingnew_spinner);           //책분류 스피너
        bookname = findViewById(R.id.readingnew_booktitle);     //책제목
        bookauthor = findViewById(R.id.readingnew_bookauthor);  //책저자
        bookpage =findViewById(R.id.readingnew_bookpage);       //총페이지수
        date =findViewById(R.id.readingnew_date);                //읽기시작한날자

        ActionBar ab = getSupportActionBar() ;
      //  ab.show() ;     // 앱바(App Bar) 보이기.
         ab.hide() ;     // 앱바(App Bar) 감추기.

        tedPermission(); //권한허가요청
        date();          //캘린더 날짜선택
        ImageView  galleryimage = findViewById(R.id.readingnew_gallery); //게시물이미지 버튼
        ImageView  cameraimage= findViewById(R.id.readingnew_camera);    //카메라 이미지 버튼


        //작성완료 버튼
        finsih_btn = findViewById(R.id.readingnew_writefinish);
        finsih_btn.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NowreadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                // 사용자가  작성한값을 가져온다.
                title = bookname.getText().toString();
                author = bookauthor.getText().toString();
                page = bookpage.getText().toString();
                day = date.getText().toString();
                category = spinner.getSelectedItem().toString(); //카테고리 선택값

                //  intent.putExtra("카메라사진",tempFile);
                intent.putExtra("bookcover", imageUri);
                intent.putExtra("bookname", title);
                intent.putExtra("bookauthor", author);
                intent.putExtra("bookpage", page);
                intent.putExtra("bookdate", day);
                intent.putExtra("bookcategory", category);
                intent.putExtra("index",0);
                intent.putExtra("random",randomkeygenerator());

                setResult(RESULT_OK, intent);
                finish();

            }

        });



        //스피너 선택버튼
         tv_result=findViewById(R.id.tv_result);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //내가선택한 카테고리를 text에 담을수있음.
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //text에 값을준다.
                //현재위치를 가지고와라.  toString 으로 변환.
                tv_result.setText(adapterView.getItemAtPosition(i).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //갤러리 이미지 버튼
                 galleryimage.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         //    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                         //   intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                         Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                         intent.setType("image/*");
                         startActivityForResult(intent, GET_GALLERY_IMAGE);
                     }
                 });
        //카메라 이미지 버튼
        cameraimage.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
         takePhoto();
     }
 });

    }//onCreate

    @Override
    protected void onStop() {
        super.onStop();




    }

    //pasue나 stop은 갤러리를 들어갈때 출력이되서서 null값이 계속추가됨.
    @Override
    protected void onDestroy() {
        super.onDestroy();


    }//ondestroy

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //만일 요청신호 , 결과신호 , 데이터가 비어있지 않다면.

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.d(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        }
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //이미지의 Uri 경로를 얻어온다
            imageUri = data.getData();
            //가져온 Uri경로를 bookcover 이미지뷰에 뿌려준다.
            ImageView bookcover = findViewById(R.id.readingnew_image);
            bookcover.setImageURI(imageUri);
        }
        else if (requestCode == PICK_FROM_CAMERA) {
            setImage();
        }
} //onActivityResult


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


                jArray.put(subObject);
                //position.subObject
                System.out.println( "데이터리스트 " +nowreadingDataArrayList.size());
            }

            mainObject.put("BookData", jArray);  //배열을 넣음


        } catch (JSONException e) {
            e.printStackTrace();
        }
        bookeditor.putString("BookData", String.valueOf(mainObject));
        bookeditor.apply();

    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String dayday = date.getText().toString();
        outState.putString(KEY , dayday);

    }

    //달력날짜정하기 메서드
  private void date(){
         date = findViewById(R.id.readingnew_date);
         date.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 calendar =Calendar.getInstance();
                 year= calendar.get(Calendar.YEAR);
                 month =calendar.get(Calendar.MONTH);
                 dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
                 datePickerDialog = new DatePickerDialog(ReadingnewActivity.this,
                         new DatePickerDialog.OnDateSetListener() {

                      //날짜를 지정해준다.
                      //이유는 모르겠는데 month가 하나적게나와서 +1 해줌
                     @Override
                     public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                         date.setText(year+"년"+(month+1)+"월"+day+"일");
                     }
                     //현재 날짜에 맞춰준다.
                 } , year,month,dayOfMonth );
                 datePickerDialog.show();
             }
         });

    }

    //카메라 사진찍기 메서드
    private void takePhoto() {
        isCamera =true;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }

        if (tempFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.afinal.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            } else {
                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        }
    }
    //이미지 파일생성  메서드
    private File createImageFile() throws IOException {
        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "blackJin_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/blackJin/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }


    //이미지 설정메서드
    //이미지뷰에  사진을 뿌려준다.
    private void setImage() {
        ImageView imageView = findViewById(R.id.readingnew_image);


        ImageResizeUtils.resizeFile(tempFile,tempFile,1280,isCamera);
       //첫 번째 파라미터에 변형시킬 tempFile 을 넣음.
        //
        //두 번째 파라미터에는 변형시킨 파일을 다시 tempFile에 저장
        //
        //세 번째 파라미터는 이미지의 긴 부분을 1280 사이즈로 리사이징 하라는 의미
        //
        //네 번째 파라미터를 통해 카메라에서 가져온 이미지인 경우 카메라의 회전각도를 적용해 줍니다.(앨범에서 가져온경우 회전각도 신경x)

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        imageView.setImageBitmap(originalBm);
    }


 //권한 허가요청 메서드(Tedpermission 라이브러리)
    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    //랜덤키 생성메서드
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


}//ReadingnewActivity
