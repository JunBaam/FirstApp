package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MemoModifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_modify);


        Intent get = getIntent();

        //받은 title 값을 tv에 넣는다.
       final TextView getText = findViewById(R.id.memomodify_writetitle);
        String modify = get.getStringExtra("title");
        getText.setText(modify);


        Button modify_finsih = findViewById(R.id.memomodify_complete);
        modify_finsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String returnModify =  getText.getText().toString();

                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.putExtra("modifytitle" , returnModify);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });








    }





}
