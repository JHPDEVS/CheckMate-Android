package com.eatx.wdj.ui.main;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eatx.wdj.R;
import com.eatx.wdj.data.BoardAdapter;
import com.eatx.wdj.data.NoticeBoardAdapter;
import com.eatx.wdj.data.model.Post;
import com.eatx.wdj.data.model.noticeBoardModel;
import com.eatx.wdj.ui.login.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class noticeBoardSearch extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchView searchView;
    private List<noticeBoardModel> posts;
    private RecyclerView.Adapter mAdapter;
    private ProgressDialog write;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchView = (SearchView) findViewById(R.id.searchView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        searchView.setQueryHint("제목으로 검색할 수 있습니다");
        posts = new ArrayList<>();
        mAdapter = new NoticeBoardAdapter(this, posts);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println(searchView.getQuery() + "검색됨");
                Update();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    protected void Update() {
        new Thread() {
            public void run() {
                try {

                    posts.clear();
                    Document doc = Jsoup.connect("https://computer.yju.ac.kr/?_filter=search&act=&vid=&mid=board_XwRR82&category=&search_target=title&search_keyword="+searchView.getQuery()).get();
                    Elements contents = doc.select("tbody").select("tr.notice");
                    Elements contents2 = doc.select("tbody").select("tr");
                    System.out.println(contents2);
                    List<String> imageUrls = new ArrayList<>();

//                    for(Element content : contents) {
//                          imageUrls.add(content.select("td.title").select("a").attr("abs:href")); // 하이퍼링크
//                          imageUrls.add(content.select("td.title").select("a").select("strong").text()); // 제목
//                          imageUrls.add(content.select("td.author").select("a").text()); // 작성자
//                          imageUrls.add(content.select("td.time").text()); // 작성날짜
//                    }

                    for(Element content : contents2) {
                        imageUrls.add(content.select("td.no").text());
                        imageUrls.add(content.select("td.title").select("a").attr("abs:href"));
                        imageUrls.add(content.select("td.title").select("a").text()); // 제목
                        imageUrls.add(content.select("td.author").select("a").text()); // 작성자
                        imageUrls.add(content.select("td.time").text()); // 작성날짜

                        noticeBoardModel post = new noticeBoardModel();
                        post.setTitle(content.select("td.title").select("a").text());
                        post.setHref(content.select("td.title").select("a").attr("abs:href"));
                        post.setDate(content.select("td.time").text());
                        post.setAuthor(content.select("td.author").select("a").text());
                        post.setNum(content.select("td.no").text());
                        if(post.getTitle() != "") {
                            if( post.getNum().equals("공지")) {
                            } else {
                                posts.add(post);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(mAdapter);
                            }
                        });
                    }
                    System.out.println(imageUrls);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}