package com.example.afinal;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//어댑터 클래스
public class NowreadingAdapter extends RecyclerView.Adapter<NowreadingAdapter.ItemViewHolder> {

    private    ImageView bookcover;   //책표지
    private   TextView bookname;      //책이름
    private  TextView bookauthor;    //책저자
    private TextView hidden;   //비밀key값
    private Context mcontext;


    //그리드레이아웃 매니저  ,  spanCount를 통해 한줄에 몇개의 아이템이 들어갈지를 정할수가있다.
    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_THREE = 3;

    private static final int VIEW_TYPE_SMALL = 1;
    private static final int VIEW_TYPE_BIG = 2;

    private ArrayList<NowreadingData> nowreadingDataArrayList;  //아이템을 담은 arraylist
    private GridLayoutManager gridLayoutManager;

    //어댑터 생성자
    public NowreadingAdapter(ArrayList<NowreadingData>nowreadingDataArrayList, GridLayoutManager gridLayoutManager,Context mcontext) {
        this.nowreadingDataArrayList =nowreadingDataArrayList;
        this.gridLayoutManager=gridLayoutManager;
        this.mcontext=mcontext;
    }




    // 뷰홀더 클래스
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView bookcover;   //책표지
        TextView bookname;      //책이름
        TextView bookauthor;    //책저자
        TextView hidden;   //비밀key값
        TextView bookpage,bookdate,bookcategory;

        //뷰홀더 생성자
        public   ItemViewHolder(View itemView, int viewType) {
            super(itemView);

            //뷰홀더 아이템클릭 이벤트처리
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition(); // 현재자신의 위치(position)를 확인할수있는 메서드.
                    if (pos != RecyclerView.NO_POSITION) {
                        NowreadingData item = nowreadingDataArrayList.get(pos); //데이터 리스트로부터 위치를 가져온다.
                    }
                }
            }); //onclick

            if (viewType == VIEW_TYPE_BIG) {
                bookcover = (ImageView) itemView.findViewById(R.id.image_big);
                bookname = (TextView) itemView.findViewById(R.id.title_big);
                bookauthor = (TextView) itemView.findViewById(R.id.tv_info);
                hidden = itemView.findViewById(R.id.hidden);
                bookdate=itemView.findViewById(R.id.date);
                bookcategory=itemView.findViewById(R.id.category);
                bookpage=itemView.findViewById(R.id.page);

            } else {
                bookcover = (ImageView) itemView.findViewById(R.id.image_small);
                bookname = (TextView) itemView.findViewById(R.id.title_small);
                hidden = itemView.findViewById(R.id.hidden);
                bookdate=itemView.findViewById(R.id.date);
                bookcategory=itemView.findViewById(R.id.category);
                bookpage=itemView.findViewById(R.id.page);

                //    hidden = itemView.findViewById(R.id.hidden);
            }
        } //뷰홀더 생성자
    }//뷰홀더 클래스





    //뷰타입을 설정하느 메서드
    @Override
    public int getItemViewType(int position) {
        int spanCount = gridLayoutManager.getSpanCount();
        if (spanCount == SPAN_COUNT_ONE) {
            return VIEW_TYPE_BIG;
        } else {
            return VIEW_TYPE_SMALL;
        }
    }

    //설정에따라 다른레이아웃을 보여준다
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_BIG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nowreading_linear, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nowreading_grid, parent, false);
        }
        return new ItemViewHolder(view, viewType);

    }


    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시한다 . BindViewHolder
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        NowreadingData item = nowreadingDataArrayList.get(position);        //갯수를 제한한다 position%4

        holder.bookname.setText(item.bookname);
        holder.bookcover.setImageURI(Uri.parse(item.bookcover));

        // holder.hidden.setText(item.hidenkey);
        //   holder.bookcover.setImageResource(item.getBookcover()); int용
        //   holder.bookauthor.setText(item.bookauthor);
        //  holder.bookname.setText(arrayList.get(position).getBookname());
        //  holder.bookcover.setImageURI(Uri.parse(arrayList.get(position).getBookcover()));
        //  holder.bookauthor.setText(arrayList.get(position).getBookauthor());


        //뷰의 타입이 바뀌면 저자의 이름도 같이 보여준다.
        if (getItemViewType(position) == VIEW_TYPE_BIG) {
            holder.bookauthor.setText(item.bookauthor);


            //     holder.hidden.setText(item.hidenkey);
        }


    }// bindViewholder

    //아이템의 갯수를설정.
    @Override
    public int getItemCount()
    {
        //  return nowreadingDataArrayList.size();
        return (null != nowreadingDataArrayList ? nowreadingDataArrayList.size() : 0);
        }





        }
