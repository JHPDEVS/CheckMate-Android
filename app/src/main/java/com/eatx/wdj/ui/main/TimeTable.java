package com.eatx.wdj.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eatx.wdj.R;
import com.eatx.wdj.data.model.Post;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTable extends Fragment {
    final static private String url = "https://ckmate.shop/.well-known/TimeTable.php";
    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;

    private Button addBtn;
    private Button clearBtn;
    private Button saveBtn;
    private Button loadBtn;

    private TimetableView timetable;
    private MainViewModel mViewModel;
    View inflatedview = null;
    private Schedule schedule;
    public static TimeTable newInstance() {
        return new TimeTable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inflatedview =  inflater.inflate(R.layout.fragment_time_table, container, false);

        timetable =  inflatedview.findViewById(R.id.timetable);
        getTimeTable();
        setToday(); // 오늘 날짜로 설정
        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                System.out.println(idx+"선택됨");
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            System.out.println(success);
                            if(success) {
                                String classTitle = jsonObject.getString("classTitle");
                                String classPlace = jsonObject.getString("classPlace");
                                String professorName = jsonObject.getString("professorName");
                                int day = jsonObject.optInt("day",0);
                                int startHour = jsonObject.optInt("startHour",0);
                                int EndTime = jsonObject.optInt("EndTime",0);
                                String classValue = jsonObject.getString("class");
                                System.out.println("----------------");
                                System.out.println(classTitle);
                                System.out.println(classPlace);
                                System.out.println(professorName );
                                System.out.println(day );
                                System.out.println(startHour );
                                System.out.println(EndTime );
                                System.out.println(classValue );
                                System.out.println("----------------");
                            } else {
                                System.out.println("error");
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                TimeTableRequest timeTableRequest = new TimeTableRequest(idx,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(timeTableRequest);
            }
        });
        return inflatedview;
    }
    private void setToday() {
        Calendar time = Calendar.getInstance();
        int day = time.get(Calendar.DAY_OF_WEEK) - 1;
        timetable.setHeaderHighlight(day);
    }
    private void getTimeTable() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);// save
                        System.out.println(array);
                        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                        Schedule schedule = new Schedule();
                        int count=0;
                        while(count<array.length()){
                            JSONObject object = array.getJSONObject(count);
                            schedule.setClassTitle(object.getString("classTitle"));
                            schedule.setClassPlace(object.getString("classPlace"));
                            schedule.setDay(object.getInt("day"));
                            schedule.setProfessorName(object.getString("professorName"));
                            schedule.setStartTime(new Time(object.getInt("startHour"),object.getInt("startMinute")));
                            schedule.setEndTime(new Time(object.getInt("EndTime"),object.getInt("EndMinute")));
                            schedules.add(schedule);
                            timetable.add(schedules);
                            count++;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);


    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }
    private void getProducts (){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    System.out.println("다이얼로그");

                    try {

                        JSONArray array = new JSONArray(response);// save
                        System.out.println(array);
                        for(int i=0;i<array.length();i++) {
                            JSONObject tempJson = array.getJSONObject(i);
                            System.out.println(tempJson);
                            timetable.load(String.valueOf(tempJson));
                        }


                        } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);

    }


}