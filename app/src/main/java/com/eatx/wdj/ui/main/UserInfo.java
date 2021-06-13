package com.eatx.wdj.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eatx.wdj.R;
import com.eatx.wdj.ui.login.LoginActivity;
import com.eatx.wdj.ui.login.MainActivity;

public class UserInfo extends AppCompatActivity {

    private TextView idT, nameT, sidT, phoneT, emailT, classT;
    private ImageView back;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        int sid = intent.getIntExtra("sid",0);
        int phoneNumber = intent.getIntExtra("phoneNumber",0);
        String email = intent.getStringExtra("email");
        String classValue = intent.getStringExtra("classValue");


        System.out.println(id);
        System.out.println(name);
        System.out.println(sid);
        System.out.println(phoneNumber);
        System.out.println(email);
        System.out.println(classValue);


        idT = findViewById(R.id.user_id);
        nameT = findViewById(R.id.user_name);
        sidT = findViewById(R.id.user_sid);
        phoneT = findViewById(R.id.user_phone);
        emailT = findViewById(R.id.user_email);
        classT = findViewById(R.id.user_class);

        idT.setText(id);
        nameT.setText(name);
        sidT.setText(String.valueOf(sid));
        phoneT.setText("0"+String.valueOf(phoneNumber));
        emailT.setText(email);
        classT.setText(classValue);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfo.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}