package com.eatx.wdj.ui.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eatx.wdj.R;
import com.eatx.wdj.data.BoardAdapter;
import com.eatx.wdj.data.MainAdapter;
import com.eatx.wdj.data.Util;
import com.eatx.wdj.data.model.BoardModel;
import com.eatx.wdj.data.model.Post;
import com.eatx.wdj.data.model.mainModel;
import com.eatx.wdj.ui.Register.Register;
import com.eatx.wdj.ui.Register.RegisterRequest;
import com.eatx.wdj.ui.main.WriteRequest;
import com.eatx.wdj.ui.login.LoginActivity;
import com.eatx.wdj.ui.login.LoginRequest;
import com.eatx.wdj.ui.login.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.royrodriguez.transitionbutton.TransitionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Board extends Fragment {

    private RecyclerView recyclerView;
    final static private String url = "https://ckmate.shop/.well-known/Board.php";
    private MainViewModel mViewModel;
    private ArrayList<BoardModel> dataList;
    private BoardAdapter adapter;
    public static Board newInstance() {
        return new Board();
    }
    View inflatedview = null;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter mAdapter;
    private List<Post> posts;
    private ProgressDialog pDialog;
    private FloatingActionButton fab;
    private String id , type , name ,bno;
    private ImageView searchButton;
    String[] items = {"공지","자유"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inflatedview = inflater.inflate(R.layout.fragment_board, container, false);
        recyclerView = inflatedview.findViewById(R.id.boardrecyclerview);
        fab = inflatedview.findViewById(R.id.fab);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        posts = new ArrayList<>();
        mAdapter = new BoardAdapter(getActivity(),posts);
        searchButton = inflatedview.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BoardSearch.class);
                startActivity(intent);
            }
        });
        id = getActivity().getIntent().getStringExtra("id");
        getWriter(); // 이름 불러오기
        getBoard();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWrite();
            }
        });
        return inflatedview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    public void showItemList(){
        mAdapter = new BoardAdapter(getActivity(),posts);
        recyclerView.setAdapter(mAdapter);
    }

    private void showWrite() {
        final Dialog dialog = Util.getCustomDialog(getActivity(), R.layout.writer_dialog);
        dialog.setCancelable(false);
        dialog.show();

        EditText dTitle = dialog.findViewById(R.id.dialog_title);
        EditText dContent = dialog.findViewById(R.id.dialog_content);
        ImageView dClose = dialog.findViewById(R.id.dialog_close_img);
        Button send = dialog.findViewById(R.id.dialog_close);
        EditText password = dialog.findViewById(R.id.password);
        Spinner spinner = dialog.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,items
        );
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("M월 d일 aa h:mm");
        String getTime = simpleDate.format(mDate);
        System.out.println(getTime + "시간 ㅇㅇㅇㅇㅇㅇㅇ");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = items[0];
            }
        });
        dContent.setMovementMethod(new ScrollingMovementMethod());
        dClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog write = new ProgressDialog(getActivity());
                write.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                write.setMessage("게시글 작성하는중~");
                write.setCanceledOnTouchOutside(false);
                write.show();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                Toast.makeText(getActivity().getApplicationContext(),"글 작성에 성공했습니다",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                write.dismiss();
                            } else {
                                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"글 작성에 실패했습니다",Snackbar.LENGTH_SHORT);
                                snackbar.setAnchorView(getActivity().findViewById(R.id.dialog_content));
                                snackbar.show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getBoard();
                    }

                };
                // 서버로 Volley를 이용해서 요청함
                WriteRequest writeRequest = new WriteRequest(type,id,password.getText().toString(),dTitle.getText().toString(),dContent.getText().toString(),name,getTime,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(writeRequest);
            }
        });
    }
    private void getWriter() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    System.out.println(success);
                    if(success) {
                        name = jsonObject.getString("name");
                        System.out.println(name + "이름이름");
                    } else {
                        System.out.println("오류 ");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        nameRequest nameRequest = new nameRequest(getActivity().getIntent().getStringExtra("id"),responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(nameRequest);
    }

    private void getBoard (){
        posts.clear();
        System.out.println("다이얼로그");
        ProgressDialog load = new ProgressDialog(getActivity());
        load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        load.setMessage("게시글 불러오는중~");
        load.setCanceledOnTouchOutside(false);
        load.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {

                        JSONArray array = new JSONArray(response);
                        System.out.println(array.length() + "길이");
                        int count=0;
                        while(count<array.length()){

                            JSONObject object = array.getJSONObject(count);
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

                    }catch (Exception e){
                        System.out.println(e + "에러발생");
                    }

                    recyclerView.setAdapter(mAdapter);
                    load.dismiss();
                },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}