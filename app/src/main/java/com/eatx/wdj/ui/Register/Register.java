package com.eatx.wdj.ui.Register;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.eatx.wdj.R;
import com.eatx.wdj.ui.login.LoginActivity;
import com.eatx.wdj.ui.login.LoginRequest;
import com.eatx.wdj.ui.login.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.eatx.wdj.ui.main.PhoneAuth;
import com.google.android.material.snackbar.Snackbar;
import com.royrodriguez.transitionbutton.TransitionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends AppCompatActivity {

    private EditText et_phone ,et_id ,et_password ,et_name ,et_email,et_password2,et_sid,et_class;
    private AppCompatImageView BackButton;
    private TransitionButton registerButton;
    Boolean dupi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_phone = findViewById(R.id.et_phone);
        et_id = findViewById(R.id.et_id);
        et_password = findViewById(R.id.et_password);
        et_password2 = findViewById(R.id.et_password2);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_sid = findViewById(R.id.et_sid);
        et_class = findViewById(R.id.et_class);
        registerButton = findViewById(R.id.goRegister);
        BackButton = findViewById(R.id.iv_back);



        // Enables Always-on

//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                new Task().execute();
//            }
//        });
        getPhoneNo(); // edittext에 휴대폰번호를 등록해줌


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dupiID();
            }
        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void dupiID() {
        String id = et_id.getText().toString();
        registerButton.startAnimation();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    System.out.println(success);
                    if(success) {
                        registerButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"중복된 아이디입니다",Snackbar.LENGTH_SHORT);
                        snackbar.setAnchorView(findViewById(R.id.goRegister));
                        snackbar.show();
                        return;
                    } else {
                        checkALL();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        IDCheck dupiID = new IDCheck(id,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Register.this);
        queue.add(dupiID);
    }
    private void checkALL() {
        if(et_id.getText().toString().equals("") || et_name.getText().toString().equals("") || et_password.getText().toString().equals("") || et_password2.getText().toString().equals("")
        || et_class.getText().toString().equals("") || et_email.getText().toString().equals("") || et_sid.getText().toString().equals("")) {
            registerButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"빈틈없이 입력해주세요",Snackbar.LENGTH_SHORT);
            snackbar.setAnchorView(findViewById(R.id.goRegister));
            snackbar.show();
            return;
        } else {
            if(et_password.getText().toString().equals(et_password2.getText().toString())) {
                register();
            } else {
                registerButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"비밀번호 확인값이랑 비밀번호가 동일하지 않습니다.",Snackbar.LENGTH_SHORT);
                snackbar.setAnchorView(findViewById(R.id.goRegister));
                snackbar.show();
                return;
            }
        }
    }

    private void getPhoneNo() {
        Intent intent = getIntent();
        String phoneNo = intent.getStringExtra("phoneNo").substring(3);
        et_phone.setText("0"+phoneNo);
    }
    private void register() {
        String id = et_id.getText().toString();
        String password = et_password.getText().toString();
        String name = et_name.getText().toString();
        int sid = Integer.parseInt(et_sid.getText().toString());
        String email = et_email.getText().toString();
        int phone = Integer.parseInt(et_phone.getText().toString());
        String classValue = et_class.getText().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        Toast.makeText(getApplicationContext(),"회원가입에 성공했습니다",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("id",id);
                        intent.putExtra("password",password);
                        startActivity(intent);
                    } else {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"회원가입에 실패했습니다",Snackbar.LENGTH_SHORT);
                        snackbar.setAnchorView(findViewById(R.id.goRegister));
                        snackbar.show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // 서버로 Volley를 이용해서 요청함
        RegisterRequest registerRequest = new RegisterRequest(id,password,sid,name,classValue,phone,email,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Register.this);
        queue.add(registerRequest);

    }
    private boolean isName() {
        return dupi;
    }
}