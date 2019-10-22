package com.example.afinal;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

public class NowreadingDetailAdapter extends RecyclerView.Adapter<NowreadingDetailAdapter.ViewHolder> {


    private ArrayList<NowreadingDetailData> detailDataArrayList;
    private Context context;
    NowreadingDetailAdapter.RecyclerViewClickListener listener;





    //리사이클러뷰 클릭 인터페이스
    public interface RecyclerViewClickListener {
        void onItemClicked(int position);
        void onRemoveClicked(int position);
    }
    public void setOnClickListener(NowreadingDetailAdapter.RecyclerViewClickListener listener) {
        this.listener = listener;
    }

    NowreadingDetailAdapter(ArrayList<NowreadingDetailData> detailDataArrayList ,  Context context) {
          this.detailDataArrayList = detailDataArrayList;
          this.context=context;
    }


    @NonNull
    @Override
    public NowreadingDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.nowreadingdetail_list , parent ,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NowreadingDetailAdapter.ViewHolder holder, final int position) {

        NowreadingDetailData detailData = detailDataArrayList.get(position);

        holder.date.setText(detailData.date);
        holder.page.setText(detailData.page);


        // 아이템클릭.
        if (listener != null) {
          final int pos = position;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(pos);
                }
            });
            //독서진행상황 삭제
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("테스트", "remove 클릭완료 ");
                    // remove(holder.getAdapterPosition());
                    listener.onRemoveClicked(pos);

                }
            });
            //독서진행상황 수정
            //꾹르면 수정 다이얼로그가 뜸
           holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View view) {

                   AlertDialog.Builder alert = new AlertDialog.Builder(context);
                   alert.setTitle("페이지 수정");
                   final EditText input = new EditText(context);            //다이얼로그 EditText
                   input.setText(detailDataArrayList.get(position).getPage()); //페이지에 적힌 내용을 다이얼로그 edittext로 가져온다.
                   alert.setView(input);                                     //다이얼로그 에 설정.

                   //수정 버튼
                   alert.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int whichButton) {

                           String Page = input.getText().toString();
                           detailDataArrayList.get(position).page = Page;
                           notifyDataSetChanged();

                           Toast.makeText(context, "수정되었습니다.", LENGTH_SHORT).show();
                       }
                   });
                   //취소버튼
                   alert.setNegativeButton("취소",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int whichButton) {
                                   Toast.makeText(context, "취소되었습니다..", LENGTH_SHORT).show();
                               }
                           });
                   alert.show();
                   return true;
               }
           });  //linear holder


        } //if문
    } //bindviewholder

    @Override
    public int getItemCount() {
  // return detailDataArrayList.size();
    return (null != detailDataArrayList ? detailDataArrayList.size() : 0);

    }

    // 어댑터 삭제 메서드

    public void removeItem(int position) {
        detailDataArrayList.remove(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView page;
         TextView date;
         ImageView remove;

          LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            page = itemView.findViewById(R.id.nowreadingdetail_list_page);
            date = itemView.findViewById(R.id.nowreadingdetail_list_date);
             remove = itemView.findViewById(R.id.newreadingdetail_list_remove);

            linearLayout= itemView.findViewById(R.id.detail_linear);

        }
    }




}


