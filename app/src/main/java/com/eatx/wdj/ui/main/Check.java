package com.eatx.wdj.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.eatx.wdj.R;
import com.eatx.wdj.ui.login.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.royrodriguez.transitionbutton.TransitionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Check extends AppCompatActivity {
    private TextView date , time;
    private final Handler handler = new Handler();
    private TransitionButton checkstate;
    private TimerTask updateTime;
    private String getTim;
    private Button check;
    long now = System.currentTimeMillis();

    private int mDate,mMonth,mYear , mDayNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        updateTime();

        checkstate = findViewById(R.id.checkState);
        checkstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        check = findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"출석이 완료되었습니다",Snackbar.LENGTH_SHORT);
                snackbar.setAnchorView(findViewById(android.R.id.content));
                snackbar.show();
            }
        });
        date = findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),  "클릭", Toast.LENGTH_LONG).show();
                // DatePickerDialog
                String day = "";
                final Calendar Cal = Calendar.getInstance();
                mDate = Cal.get(Calendar.DATE);
                mMonth = Cal.get(Calendar.MONTH);
                mYear = Cal.get(Calendar.YEAR);
                mDayNum = Cal.get(Calendar.DAY_OF_WEEK);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Check.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        if (month >= 0){
                            month = month+1;
                        }
                        date.setText(year+"년 " +month+"월 "+day+" 일");
                    }
                },mYear,mMonth,mDate);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();

            }
        });
    }

    public void updateTime() {
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일");
        String getDate = simpleDate.format(mDate);

        date = findViewById(R.id.date);
        date.setText(getDate);
        time = findViewById(R.id.time);
        updateTime = new TimerTask() {
            @Override
            public void run() {
                Update();
            }
        };
        Timer timer = new Timer();
        timer.schedule(updateTime, 0, 1000);
    }
    protected void Update() {
        Runnable updater = new Runnable() {
            public void run() {
                long currentTime = System.currentTimeMillis();
                Date mTime = new Date(currentTime);
                SimpleDateFormat getTime = new SimpleDateFormat("aa h시 mm분 ss초");
                getTim = getTime.format(mTime);
                time.setText(getTim);
            }
        };
        handler.post(updater);
    }

    private void doStuff() {
        // Thread를 생성한다.
        new Thread(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();
    }
}