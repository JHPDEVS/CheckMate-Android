package com.eatx.wdj.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.eatx.wdj.R;
import com.eatx.wdj.data.ItemAdapter;
import com.eatx.wdj.data.MainAdapter;
import com.eatx.wdj.data.model.Post;
import com.eatx.wdj.data.model.mainModel;
import com.eatx.wdj.data.model.userInfo;
import com.eatx.wdj.ui.login.LoginActivity;
import com.eatx.wdj.ui.login.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private MainViewModel mViewModel;
    private TextView hello ;
    private String id;
    private MainAdapter adapter;
    private ArrayList<mainModel> menu;
    private userInfo user;
    private RecyclerView recyclerView;
    private String name , classValue,  email;
    private int sid, phoneNumber;
    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    View inflatedview = null;
    private ArrayList<mainModel> dataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        id = getActivity().getIntent().getStringExtra("id");
        inflatedview = inflater.inflate(R.layout.main_fragment, container, false);
        hello = (TextView) inflatedview.findViewById(R.id.text_self_auth_need);
        user = (userInfo) getActivity().getIntent().getSerializableExtra("user");

        getUserInfo();
        menu = new ArrayList<>();
        recyclerView = inflatedview.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new MainAdapter(getActivity(),menu, new MainActivity());
        recyclerView.setAdapter(adapter);
        return inflatedview;

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    private void getUserInfo() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    System.out.println(success);
                    if(success) {
                        id = jsonObject.getString("id");
                        sid = jsonObject.optInt("sid",0);
                        classValue = jsonObject.getString("class");
                        phoneNumber = jsonObject.optInt("phoneNumber",0);
                        email = jsonObject.getString("email");
                        name = jsonObject.getString("name");
                        System.out.println("----------------");
                        System.out.println(id);
                        System.out.println(sid);
                        System.out.println(classValue );
                        System.out.println(phoneNumber );
                        System.out.println(email );
                        System.out.println(name );
                        System.out.println("----------------");


                        setMain();
                    } else {
                        System.out.println("오류 ");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        UserInfoRequest userInfoRequest = new UserInfoRequest(getActivity().getIntent().getStringExtra("id"),responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(userInfoRequest);
    }

    private void setMain() {
        hello.setText(name+"님 반갑습니다!");
        menu.add(new mainModel(name,id,sid,classValue,2,true));
        menu.add(new mainModel("WDJ 시간표",3,true,true));
        menu.add(new mainModel("게시판", 1));
        adapter.notifyDataSetChanged();
    }

}