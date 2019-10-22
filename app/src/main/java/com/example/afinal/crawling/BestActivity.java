package com.example.afinal.crawling;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.afinal.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class BestActivity extends AppCompatActivity {

    private Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView kyoboRecyclerView;

    FrameLayout frame_aladin, frame_kyobo;
    Button aladin_visible_btn, kyobo_visible_btn;

    BestAdapter bestAdapter;
   KyoboAdapter   kyoboAdapter;
  


    ArrayList<ItemClass> list_book = new ArrayList<>();
    ArrayList<ItemClass> list_book_2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best);

        ActionBar ab = getSupportActionBar() ;
        ab.hide() ;
        mContext = this;

        
        frame_aladin = (FrameLayout)findViewById(R.id.main_frame_aladin);
        frame_kyobo = (FrameLayout)findViewById(R.id.main_frame_kyobo);

        aladin_visible_btn = (Button)findViewById(R.id.main_aladin_list_btn);
        aladin_visible_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frame_aladin.setVisibility(View.VISIBLE);
                frame_kyobo.setVisibility(View.INVISIBLE);
            }
        });

        kyobo_visible_btn = (Button)findViewById(R.id.main_kyobo_list_btn);
        kyobo_visible_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frame_aladin.setVisibility(View.INVISIBLE);
                frame_kyobo.setVisibility(View.VISIBLE);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.rv_book);
        kyoboRecyclerView = (RecyclerView)findViewById(R.id.rv_book2);

        new Description().execute();

    }

    private class Description extends AsyncTask<Void, Void, Void> {

        private ProgressDialog gress_book;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            gress_book = new ProgressDialog(BestActivity.this);
            gress_book.setCancelable(false);
            gress_book.setMessage("정보를 불러오는 중 입니다.");
            gress_book.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            gress_book.show();

        }


        ArrayList<String> list_booktitle = new ArrayList<>();
        //빈 값 빼고 넣을 [진짜 책 제목] ArrayList
        ArrayList<String> list_realtitle = new ArrayList<>();

        ArrayList<String> list_bookprice = new ArrayList<>();
        //빈 값 빼고 넣을 [진짜 책 가격] ArrayList
        ArrayList<String> list_realprice = new ArrayList<>();


        // 지은이 따오기 위한 내부 url;
        ArrayList<String> list_href = new ArrayList<>();
        ArrayList<String> list_realhref = new ArrayList<>();
        ArrayList<String> list_bookeditor = new ArrayList<>();
        ArrayList<String> list_realeditor = new ArrayList<>();
        ArrayList<String> list_realimgurl = new ArrayList<>();


        // 교보문고 책 제목, imgUrl, 가격, 내부 Url(href), 지은이 ArrayList<String>;
        ArrayList<String> list_2_booktitle = new ArrayList<>();
        ArrayList<String> list_2_bookprice = new ArrayList<>();
        ArrayList<String> list_2_bookhref = new ArrayList<>();
        ArrayList<String> list_2_bookimgurl = new ArrayList<>();
        ArrayList<String> list_2_bookeditor = new ArrayList<>();


        @Override
        protected Void doInBackground(Void... params) {

            Log.i("데이터를 받는중", "@@@@@@@@@@@@@@@@@@@22 ");
            try{

                // 알라딘 베스트셀러 사이트(1페이지)
                Document doc = Jsoup.connect("https://www.aladin.co.kr/shop/common/wbest.aspx?BranchType=1&start=we").get();

                // 이미지 제외한 책 제목(booktitle), 가격(bookprice), 지은이(editor) 들어가있는 div Tag.
                Elements elements_basicinfo = doc.select("div[id=newbg_body]").select("div[class=ss_book_list]");

                // list_booktitle(ArrayList<String>)에 담을 [책 제목] 추출.
                for(Element elem_title : elements_basicinfo) {

                    Log.d(this.getClass().getName()+" BookTitle_value", elem_title.select("li a[class=bo3]").select("b").text());
                    list_booktitle.add(elem_title.select("li a[class=bo3]").select("b").text());
                }

                // list_bookprice(ArrayList<String>)에 담을 [책 가격] 추출.
                for(Element elem_price : elements_basicinfo) {
                    Log.d(this.getClass().getName()+" BookPrice_value",  elem_price.select("span[class=ss_p2]").select("span").text());
                    list_bookprice.add(elem_price.select("span[class=ss_p2]").select("span").text());
                }
                
                for(Element elem_editor : elements_basicinfo) {
                    Log.d(this.getClass().getName()+" BookEditor_value", elem_editor.select("li a[class=bo3]").attr("href"));
                    list_href.add(elem_editor.select("li a[class=bo3]").attr("href"));
                }

                Log.d(this.getClass().getName(), "내부 href URL 총 개수"+list_href.size());
                Log.d(this.getClass().getName(), "내부 href URL ArrayList.toString()"+list_href.toString());


                for(int h = 0; h<list_href.size()/2; h++) {

                    list_realhref.add(list_href.get(h*2));
                    Document doc_inner = Jsoup.connect(list_realhref.get(h)).get();

                    Elements inner_elem_editor = doc_inner.select("div[class=tlist]").select("li[class=Ere_sub2_title]");
                    Elements inner_elem_imgurl = doc_inner.select("div[class=cover]").select("div[class=swiper-slide]");

                    for(Element elem_realeditor : inner_elem_editor) {

                        Log.d(this.getClass().getName(), "내부 진입, 저자명: "+elem_realeditor.selectFirst("a[class=Ere_sub2_title]").text());

                        // 회사명은 필요하다고 판단되면 추가 예정
                        Log.d(this.getClass().getName(), "내부 진입, 회사명: "+elem_realeditor.select("span[class=Ere_PR10]").next().first().text());

                        list_realeditor.add(elem_realeditor.selectFirst("a[class=Ere_sub2_title]").text());

                    }

                    //이미지가 여러개 담겨나와서 리스트 재생성하면서 1번 원소 땡겨와야함(0번은 빈값이 너무 많아서 1번값으로 선택)
                    ArrayList<String> list_imgurl = new ArrayList<>();

                    for(Element elem_realimgurl : inner_elem_imgurl) {
                        Log.d(this.getClass().getName(), "내부 진입, 이미지url :"+elem_realimgurl.select("img[class=imgbox]").attr("src"));
                        list_imgurl.add(elem_realimgurl.select("img[class=imgbox]").attr("src"));

                    }
                    list_realimgurl.add(list_imgurl.get(1));
                }

                Log.d(this.getClass().getName(), "빈값 제거한 hreflist size"+list_realhref.size());
                Log.d(this.getClass().getName(), "빈값 제거한 hreflist.toString()"+list_realhref.toString());

                Log.d(this.getClass().getName(), "editor_list_size()"+list_realeditor.size());
                Log.d(this.getClass().getName(), "editor_list_toString()"+list_realeditor.toString());

                Log.d(this.getClass().getName(), "imgUrl_list_size()"+list_realimgurl.size());
                Log.d(this.getClass().getName(), "imgUrl_list_toString()"+list_realimgurl.toString());


                for(int i =0; i<list_booktitle.size(); i++) {
                    Log.d(this.getClass().getName()," title_list_size: "+list_booktitle.size()); // 원래 50나와야되는 데 100나옴
                    //  [지랄발랄 하은맘의 십팔년 책육아, , 혼자가 혼자에게, ,  ------ ]
                    //  빈 값이 하나씩 추가적으로 들어가서 나중에 빼고 다시 어레이 담아야 함.

                    Log.d(this.getClass().getName()+" title_value", list_booktitle.get(i).toString());
                }
                Log.d(this.getClass().getName()+"Array_contents_title", "toString_value :"+list_booktitle.toString());

                Log.d(this.getClass().getName()+"Array_contents_price", "toString_value :"+list_bookprice.toString());

                for(int j = 0; j<list_bookprice.size(); j++) {
                    Log.d(this.getClass().getName(), " price_list_array_size: "+list_bookprice.size()); // 원래 50나와야되는 데 100나옴
                    Log.d(this.getClass().getName()+" price_value", list_bookprice.get(j).toString());

                }






                // list_booktitle(빈 값 포함된 ArrayList) ----> 공백 제거를 위한 list_realtitle로 원소 이동
                for(int i2 = 0; i2<list_booktitle.size()/2; i2++) {

                    Log.d(this.getClass().getName(), "title_Empty_value_remove"+list_booktitle.get((i2)*2));
                    list_realtitle.add(list_booktitle.get((i2)*2));
                }


                // list_bookprice(빈 값 포함된 ArrayList) ----> 공백 제거를 위한 list_realprice로 원소 이동
                for(int j2 =0; j2<list_bookprice.size()/2; j2++) {
                    String cut_price_data = list_bookprice.get((j2)*2).split(" ")[0];
                    Log.d(this.getClass().getName(), "price_Empty_value_remove"+cut_price_data);
                    list_realprice.add(cut_price_data);
                }




                // 알라딘 베스트셀러 1페이지 상품 표현개수 50개가 맞는지 검사
                Log.d(this.getClass().getName(),"책 제목 ArrayList 원소 개수"+list_realtitle.size());
                Log.d(this.getClass().getName(),"책 가격 ArrayList 원소 개수"+list_realprice.size());
                Log.d(this.getClass().getName(),"책 저자 ArrayList 원소 개수"+list_realeditor.size());
                Log.d(this.getClass().getName(),"책 이미지 ArrayList 원소 개수"+list_realimgurl.size());


                for(int f = 0; f<list_realtitle.size(); f++) {

                    // ItemObject 생성자 내부 변수 순서
                    // String book_imgurl, String book_name, String book_price, String book_sub_info, String book_innerurl
                    list_book.add(new ItemClass(list_realimgurl.get(f), list_realtitle.get(f), list_realprice.get(f), list_realeditor.get(f), list_realhref.get(f)));

                }
                
                 //교보문고하고 연결 
                
                Document doc_2 = Jsoup.connect("http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?range=1&orderClick=DAA").get();
                Elements elements_kyobo = doc_2.select("ul[class=list_type01]").select("li");
                Log.d(this.getClass().getName()+"elements_html", elements_kyobo.text());

                for(Element elem_info : elements_kyobo) {

                    Log.d(this.getClass().getName()+"_교보문고 제목", elem_info.select("div[class=detail]").select("div[class=title]").select("strong").text());
                    list_2_booktitle.add(elem_info.select("div[class=detail]").select("div[class=title]").select("strong").text());

                    Log.d(this.getClass().getName()+"_교보문고 imgUrl", elem_info.select("div[class=cover]").select("a img").attr("src"));
                    list_2_bookimgurl.add(elem_info.select("div[class=cover]").select("a img").attr("src"));

                    Log.d(this.getClass().getName()+"_교보문고 href", elem_info.select("div[class=detail]").select("div[class=title]").select("a").attr("href"));
                    list_2_bookhref.add(elem_info.select("div[class=detail]").select("div[class=title]").select("a").attr("href"));

                    String cut_editor = elem_info.select("div[class=detail]").select("div[class=author]").text().split(" ")[0];
                    Log.d(this.getClass().getName()+"_교보문고 작가", cut_editor);
                    list_2_bookeditor.add(cut_editor);

                    Log.d(this.getClass().getName()+"_교보문고 가격", elem_info.select("div[class=detail]").select("div[class=price]").select("strong[class=book_price]").text());
                    list_2_bookprice.add(elem_info.select("div[class=detail]").select("div[class=price]").select("strong[class=book_price]").text());
                }

                for(int f2 = 0; f2<list_2_booktitle.size(); f2++) {
                    list_book_2.add(new ItemClass(list_2_bookimgurl.get(f2), list_2_booktitle.get(f2), list_2_bookprice.get(f2), list_2_bookeditor.get(f2), list_2_bookhref.get(f2)));
                }


            }catch(Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            RecyclerView.LayoutManager layoutManager_book = new LinearLayoutManager(BestActivity.this);
            RecyclerView.LayoutManager layoutManager_book2 = new LinearLayoutManager(BestActivity.this);

            recyclerView.setLayoutManager(layoutManager_book);
            kyoboRecyclerView.setLayoutManager(layoutManager_book2);

           bestAdapter = new BestAdapter(list_book);
               kyoboAdapter = new KyoboAdapter(list_book_2);

            recyclerView.setAdapter(bestAdapter);
            kyoboRecyclerView.setAdapter(   kyoboAdapter);

            gress_book.dismiss();

        }

    }

}