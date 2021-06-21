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
import com.eatx.wdj.data.model.Post;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BoardSearch extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchView searchView;
    private List<Post> posts;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchView = (SearchView) findViewById(R.id.searchView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        searchView.setQueryHint("작성자,제목 ,내용으로 검색할 수 있습니다");
        posts = new ArrayList<>();
        mAdapter = new BoardAdapter(this, posts);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println(searchView.getQuery() + "검색됨");
                getBoard();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getBoard() {
        ProgressDialog load = new ProgressDialog(this);
        load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        load.setMessage("게시글 불러오는중~");
        load.setCanceledOnTouchOutside(false);
        load.show();
        CharSequence searchQuery = searchView.getQuery();
        System.out.println(searchQuery.toString() + "검색합니다");
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    System.out.println(jsonArray.length() + "길이");
                    if(jsonArray.length() <1) {
                        System.out.println("적음");
                        Toast.makeText(getApplicationContext(),"검색된 게시물이 없습니다",Toast.LENGTH_SHORT);
                    }
                    int count=0;
                        while(count<jsonArray.length()) {

                            JSONObject object = jsonArray.getJSONObject(count);
                            int bno = object.getInt("bno");
                            String id = object.getString("id");
                            String type = object.getString("type");
                            String password = object.getString("password");
                            String subject = object.getString("subject");
                            String content = object.getString("content");
                            String writer = object.getString("writer");
                            String wdate = object.getString("wdate");

                            Post post = new Post();
                            post.setBno(bno);
                            post.setId(id);
                            post.setPassword(password);
                            post.setSubject(subject);
                            post.setContent(content);
                            post.setWriter(writer);
                            post.setWdate(wdate);
                            post.setType(type);
                            posts.add(post);
                            mAdapter.notifyDataSetChanged();
                            count++;
                        }
                    recyclerView.setAdapter(mAdapter);
                    load.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(searchQuery.toString(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(BoardSearch.this);
        queue.add(boardSearchRequest);
        posts.clear();
    }
}