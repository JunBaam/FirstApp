package com.example.afinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

//어댑터 클래스
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ItemViewHolder> {



    private Context mcontext;
    HomeAdapter.RecyclerViewClickListener listener;

    //그리드레이아웃 매니저  ,  spanCount를 통해 한줄에 몇개의 아이템이 들어갈지를 정할수가있다.
    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_THREE = 3;

    private static final int VIEW_TYPE_SMALL = 1;
    private static final int VIEW_TYPE_BIG = 2;

    private  ArrayList<HomeData> homeDataArrayList; //아이템을 담은 arraylist
    private GridLayoutManager gridLayoutManager;



    public interface RecyclerViewClickListener {
        void onItemClicked(int position);
        void onModifyClicked(int position);
        void onRemoveClicked(int position);
    }
    public void setOnClickListener(HomeAdapter.RecyclerViewClickListener listener) {
        this.listener = listener;
    }


    //어댑터 생성자
    public HomeAdapter(ArrayList<HomeData> homeDataArrayList, GridLayoutManager gridLayoutManager,Context mcontext) {
        this.homeDataArrayList=homeDataArrayList;
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


        GridLayout gridLayout;
        //뷰홀더 생성자
        public   ItemViewHolder(View itemView, int viewType) {
            super(itemView);

            //뷰홀더 아이템클릭 이벤트처리
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition(); // 현재자신의 위치(position)를 확인할수있는 메서드.
                    if (pos != RecyclerView.NO_POSITION) {
                        HomeData item = homeDataArrayList.get(pos);//데이터 리스트로부터 위치를 가져온다.
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
    public void onBindViewHolder(ItemViewHolder holder, final int position) {

         //갯수를 제한한다 position%4
        HomeData item = homeDataArrayList.get(position);

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
        }

        if (listener != null) {
            final int pos = position;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(pos);
                    Log.d("TEST ",  position+"번째 눌림 ");
                    // Log.i("@@@@@@HomeAdapter", Adapter.p+"번쨰 눌림");
                }
            });

         holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View view) {


                 AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
                 // 다이얼로그 메세지
                 alertDialog.setMessage("정말삭제하시겠어요?");
                 // 삭제버튼
                 alertDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         listener.onRemoveClicked(pos);
                     }
                 });

                 alertDialog.show();
                 return true;
             }
         });

        }






    }// bindViewholder

    //아이템의 갯수를설정.
    @Override
    public int getItemCount()
    {
        //  return nowreadingDataArrayList.size();

        return (null != homeDataArrayList? homeDataArrayList.size() : 0);
    }


    // 어댑터 삭제 메서드
    public void removeItem(int position) {
       homeDataArrayList.remove(position);
    }



}
