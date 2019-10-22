package com.example.afinal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MemoWriteActivity extends AppCompatActivity {


    Button finish ;          //작성완료버튼
    EditText title , cont;  // 메모장 제목,내용
    String memotitle , mcont;  //작성한 메모장의  제목,내용을 담는 변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_write);

        ActionBar ab = getSupportActionBar() ;
        //  ab.show() ;     // 앱바(App Bar) 보이기.
        ab.hide() ;     // 앱바(App Bar) 감추기.

        title =findViewById(R.id.memowrite_writetitle);
        cont= findViewById(R.id.memowrite_writecon);
        finish= findViewById(R.id.memowrite_complete);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext() , MemoActivity.class);

                memotitle = title.getText().toString();
            //mcont = cont.getText().toString();

                intent.putExtra("TITLE" ,memotitle);
              //  intent.putExtra("메모내용", mcont);
             Log.d("MemoWriteActivity"  , "제목 타이틀값을 보냅니다 " + memotitle);

                setResult(RESULT_OK, intent);
                 finish();
            }
        });

    } //onCreate

    @Override
    protected void onPause() {
        super.onPause();
     //   Toast.makeText(this, "임시저장되었습니다." , Toast.LENGTH_LONG).show();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();

      //  Toast.makeText(this, "저장된값을 불러옵니다." , Toast.LENGTH_LONG).show();
        restoreState();

    }
    @Override
    protected void onDestroy() {
           clearState();
       // Toast.makeText(this, "destroy호출." , Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    //정보복구 메서드
    protected void restoreState(){
        SharedPreferences pref = getSharedPreferences("pref" , Activity.MODE_PRIVATE);
          //pref 로부터 저장되어있는  title ,context 값을 가져온다.
          String t =pref.getString("title","");
          String c =pref.getString("context","");
          title.setText(t);
          cont.setText(c);

    }

    //정보저장 메서드
    //메모제목과 내용을 저장
    protected  void  saveState(){
        SharedPreferences pref =getSharedPreferences("pref" , Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("title", title.getText().toString());
        editor.putString("context",cont.getText().toString());
        editor.commit();

    }
    protected void  clearState() {
        SharedPreferences pref = getSharedPreferences("pref" , Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //Toast.makeText(this, "초기화" , Toast.LENGTH_LONG).show();

        editor.clear();
        editor.commit();
    }


}//Meme_writeActivity
