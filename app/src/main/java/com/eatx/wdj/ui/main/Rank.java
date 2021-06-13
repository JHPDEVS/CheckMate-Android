package com.eatx.wdj.ui.main;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eatx.wdj.R;
import com.eatx.wdj.data.BoardAdapter;
import com.eatx.wdj.data.RankAdapter;
import com.eatx.wdj.data.model.Rankers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Rank extends Fragment {

    final static private String url = "https://ckmate.shop/.well-known/Rank.php";
    private MainViewModel mViewModel;
    private List<Rankers> rankers;
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    public static CheckFragment newInstance() {
        return new CheckFragment();
    }
    View inflatedview = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inflatedview =  inflater.inflate(R.layout.fragment_rank, container, false);
        recyclerView = (RecyclerView) inflatedview.findViewById(R.id.rankRecyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        rankers = new ArrayList<>();
        mAdapter = new RankAdapter(getActivity(), rankers);
        getRank();
        return inflatedview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    private void getRank (){
        rankers.clear();
        System.out.println("다이얼로그");
        ProgressDialog load = new ProgressDialog(getActivity());
        load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        load.setMessage("랭크 불러오는중~");
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
                            String classValue = object.getString("class");
                            String name = object.optString("name","에러");
                            int sid = object.getInt("sid");
                            int totalrun = object.getInt("totalrun");
                            int countlate = object.getInt("countlate");

                            Rankers rank = new Rankers();
                            rank.setName(name);
                            rank.setSid(sid);
                            rank.setClassValue(classValue);
                            rank.setTotalrun(totalrun);
                            rank.setCountlate(countlate);

                            rankers.add(rank);
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
}