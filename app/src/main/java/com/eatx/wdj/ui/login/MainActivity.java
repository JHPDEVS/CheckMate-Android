package com.eatx.wdj.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.eatx.wdj.ui.login.ui.main.Board;
import com.eatx.wdj.ui.login.ui.main.MainFragment;

import me.ibrahimsn.lib.BottomBarItem;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import com.eatx.wdj.R;
import com.eatx.wdj.ui.login.ui.main.SecondFragment;
import com.eatx.wdj.ui.login.ui.main.TimeTable;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        me.ibrahimsn.lib.SmoothBottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                switch(i) {
                    case 0: System.out.println("첫번째탭");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new MainFragment()).commit();
                    break;
                    case 1: System.out.println("두번째탭");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new SecondFragment()).commit();
                    break;
                    case 2: System.out.println("세번째탭");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new TimeTable()).commit();
                    break;
                    case 3: System.out.println("네번째탭");
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


}