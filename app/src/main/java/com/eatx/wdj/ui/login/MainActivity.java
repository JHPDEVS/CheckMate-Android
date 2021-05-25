package com.eatx.wdj.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eatx.wdj.ui.main.Board;
import com.eatx.wdj.ui.main.Check;
import com.eatx.wdj.ui.main.CheckActivity;
import com.eatx.wdj.ui.main.CheckFragment;
import com.eatx.wdj.ui.main.MainFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import com.eatx.wdj.R;
import com.eatx.wdj.ui.main.SecondFragment;
import com.eatx.wdj.ui.main.TimeTable;


public class MainActivity extends AppCompatActivity {
    public static Context mContext;

    private long backpressedTime = 0;
    private me.ibrahimsn.lib.SmoothBottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mContext = this;
        bottomBar = (SmoothBottomBar) findViewById(R.id.bottomBar);
        bottomBar.bringToFront();
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                switch(i) {
                    case 0: System.out.println("첫번째탭");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new MainFragment()).commit();
                    break;
                    case 1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new CheckFragment()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new SecondFragment()).commit();
                    break;
                    case 3: ;
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new TimeTable()).commit();
                    break;
                    case 4:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new Board()).commit();
                    break;
                }
                return true;
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }



    }
    public void goCheck(View v) {
        Intent intent = new Intent(this, Check.class);
        startActivity(intent);
    }

    public void setBoardTab() {
        bottomBar = (SmoothBottomBar) findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(4);
    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
            return;
        }

    }
}