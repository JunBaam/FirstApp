package com.example.afinal.crawling;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.GlideApp;
import com.example.afinal.R;

import java.util.ArrayList;

public class KyoboAdapter extends RecyclerView.Adapter<KyoboAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ItemClass> mList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView book_name, book_price, book_subinfo, book_innerurl;
        private ImageView book_img;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mContext = itemView.getContext();

            book_innerurl = (TextView)itemView.findViewById(R.id.item_book_innerurl);
            book_img = (ImageView)itemView.findViewById(R.id.item_book_img);
            book_name = (TextView)itemView.findViewById(R.id.item_book_name);
            book_price = (TextView)itemView.findViewById(R.id.item_book_price);
            book_subinfo = (TextView)itemView.findViewById(R.id.item_book_subinfo);

        }

    }

    public KyoboAdapter(ArrayList<ItemClass> list_book2) {

        this.mList = list_book2;

    }

    @NonNull
    @Override
    public KyoboAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crolling_list, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //책이름 가격 저자 내부 url 책이미지
        holder.book_name.setText((position+1)+". "+String.valueOf(mList.get(position).getBook_name()));
        holder.book_price.setText(String.valueOf("가격 : "+mList.get(position).getBook_price()));
        holder.book_subinfo.setText(String.valueOf("저자 : "+mList.get(position).getBook_sub_info()));
        holder.book_innerurl.setText(String.valueOf(mList.get(position).getBook_innerurl()));
        GlideApp.with(holder.itemView).load(mList.get(position).getBook_imgurl()).override(100, 150).into(holder.book_img);



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}