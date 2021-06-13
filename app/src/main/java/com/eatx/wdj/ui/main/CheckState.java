package com.eatx.wdj.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
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
import com.eatx.wdj.data.CheckStateAdapter;
import com.eatx.wdj.data.model.Checkers;
import com.eatx.wdj.data.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CheckState extends AppCompatActivity  {
    RecyclerView recyclerView;
    SearchView searchView;
    long now = System.currentTimeMillis();
    private List<Checkers> checkers;
    private CheckStateAdapter mAdapter;
    private TextView date;
    private int mDate,mMonth,mYear , mDayNum;
    private FloatingActionButton dateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_state);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("이름으로 검색할 수 있습니다");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        checkers = new ArrayList<>();
        mAdapter = new CheckStateAdapter(this, checkers);
        updateTime();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println(searchView.getQuery() + "검색됨");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        date = findViewById(R.id.date);

        dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });
    }

    private void dateDialog() {
            // DatePickerDialog
            final Calendar Cal = Calendar.getInstance();
            mDate = Cal.get(Calendar.DATE);
            mMonth = Cal.get(Calendar.MONTH);
            mYear = Cal.get(Calendar.YEAR);
            mDayNum = Cal.get(Calendar.DAY_OF_WEEK);
            DatePickerDialog datePickerDialog = new DatePickerDialog(CheckState.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    String dayof = "";
                    if (month >= 0){
                        month = month+1;
                    }
                    SimpleDateFormat simpleDate = new SimpleDateFormat("EE");
                    Date date2 = new Date(year, month-1, day-1);
                    String dayOfWeek = simpleDate.format(date2);
                    date.setText(year+"년 " +month+"월 "+day+"일 "+dayOfWeek+"요일" );
                    searchView.clearFocus();
                    getBoard();

                }
            },mYear,mMonth,mDate);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
    }
    private void filter(String text) {
        List<Checkers> filteredlist = new ArrayList<Checkers>();

        for(Checkers item : checkers) {
            if( item.getName().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }

        mAdapter.filterList(filteredlist);
        mAdapter.notifyDataSetChanged();
    }
    public void updateTime() {
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy년 M월 d일 EE요일");
        String getDate = simpleDate.format(mDate);
        date = findViewById(R.id.date);
        date.setText(getDate);

        getBoard();
    }
    private void getBoard() {
        checkers.clear();
        mAdapter.getFilter().filter("");
        System.out.println(date.toString());
        ProgressDialog load = new ProgressDialog(this);
        load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        load.setMessage("게시글 불러오는중~");
        load.setCanceledOnTouchOutside(false);
        load.show();
        CharSequence searchQuery = searchView.getQuery();
        System.out.println(date.toString() + "검색합니다");

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
                        String id = object.getString("id");
                        String name = object.getString("name");
                        int sid = object.getInt("sid");
                        String classValue = object.getString("class");
                        String time = object.getString("timestamp");

                        Checkers check = new Checkers();
                        check.setName(name);
                        check.setSid(sid);
                        check.setClassValue(classValue);
                        check.setTimestamp(time);
                        checkers.add(check);
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
        CheckStateRequest checkStateRequest = new CheckStateRequest(date.getText().toString(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(CheckState.this);
        queue.add(checkStateRequest);
    }

}