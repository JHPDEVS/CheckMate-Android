package com.eatx.wdj.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eatx.wdj.R;
import com.eatx.wdj.data.Util;
import com.eatx.wdj.data.model.Post;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;



public class TimeTable extends Fragment {
    final static private String url = "https://ckmate.shop/.well-known/TimeTable.php";
    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    private int day;
    private Button addBtn;
    private Button clearBtn;
    private Button saveBtn;
    private Button loadBtn;
    private String shareBody;
    private TimetableView timetable;
    private MainViewModel mViewModel;
    View inflatedview = null;
    private Schedule schedule;
    private ImageView shareButton , settingButton;
    public static TimeTable newInstance() {
        return new TimeTable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inflatedview =  inflater.inflate(R.layout.fragment_time_table, container, false);

        timetable =  inflatedview.findViewById(R.id.timetable);
        shareButton = inflatedview.findViewById(R.id.share);
        settingButton = inflatedview.findViewById(R.id.setting);
        getTimeTable();
        setToday(); // 오늘 날짜로 설정

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "오늘의 시간표\n");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "어디에 공유할까요?"));
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("반 선택");
                String[] types = {"WDJ"};
                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        switch(which){
                            case 0:

                                break;

                        }
                    }

                });

                b.show();
            }
        });
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

                                showContent(classTitle,classValue.toUpperCase()+" 반" +"\n" + startHour +":00~" +EndTime+":00",professorName,classPlace);
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
        day = time.get(Calendar.DAY_OF_WEEK) - 1;
        timetable.setHeaderHighlight(day);
    }
    private void showContent(String title, String content,String pro , String place) {
        final Dialog dialog = Util.getCustomDialog(getActivity(), R.layout.timetable_dialog);
        dialog.setCancelable(false);
        dialog.show();
        TextView dTitle = dialog.findViewById(R.id.dialog_title);
        TextView dContent = dialog.findViewById(R.id.dialog_content);
        Button bClose = dialog.findViewById(R.id.dialog_close);
        TextView dPlace = dialog.findViewById(R.id.place);
        TextView dPro = dialog.findViewById(R.id.pro);
        dTitle.setText(title);
        dContent.setText(content);
        dPlace.setText(place);
        dPro.setText(pro);
        dContent.setMovementMethod(new ScrollingMovementMethod());
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void getTimeTable() {

        StringBuilder s = new StringBuilder(200);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);// save
                        System.out.println(array);
                        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                        Schedule schedule = new Schedule();
                        int count=0;
                        if(day == 1) {
                            s.append("오늘은 월요일 ㅠㅠ");
                        } else if(day == 2) {
                            s.append("화요일");
                        } else if(day == 3) {
                            s.append("수요일");
                        } else if(day == 4) {
                            s.append("목요일");
                        } else if(day == 5) {
                            s.append("불타는 금요일");
                        } else {
                            s.append("오늘은 쉬는날입니다");
                        }
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
                            if(object.getInt("day") == day-1) {
                                s.append( "\n"+object.getString("classTitle") +"\n" + object.getString("professorName") +"\n"+ object.getString("classPlace") +"\n"+object.getInt("startHour")+":00 ~ "+ object.getInt("EndTime") +":00" + "\n");
                            } else {
                            }
                            System.out.println(s);
                            count++;
                        }
                        s.append("\n-체크메이트-");
                        shareBody = String.valueOf(s);
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