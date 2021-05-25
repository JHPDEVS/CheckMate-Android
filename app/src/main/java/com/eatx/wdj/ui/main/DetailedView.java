package com.eatx.wdj.ui.main;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.eatx.wdj.R;


public class DetailedView extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ImageView mImage;
    private TextView title , content , writer, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.title);
        content = findViewById(R.id.desc);
        writer = findViewById(R.id.writer);
        date = findViewById(R.id.date);


        // Catching incoming intent
        Intent intent = getIntent();
        String typeValue = intent.getStringExtra("type");
        String titleValue = intent.getStringExtra("title");
        String contentValue = intent.getStringExtra("content");
        String writerValue = intent.getStringExtra("writer");
        String dateValue = intent.getStringExtra("date");
        if (intent !=null){
            System.out.println(typeValue);
            title.setText(typeValue + " " +titleValue);
            content.setText(contentValue);
            writer.setText(writerValue);
            date.setText(dateValue);
        }

    }
}