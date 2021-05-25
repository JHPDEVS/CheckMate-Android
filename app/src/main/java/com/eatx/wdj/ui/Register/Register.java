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
import com.eatx.wdj.ui.login.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.eatx.wdj.ui.main.PhoneAuth;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends AppCompatActivity {

    private EditText et_phone ,et_id ,et_password ,et_name ,et_email,et_password2,et_sid,et_class;
    private TextView registerButton;
    private AppCompatImageView BackButton;
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
        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getPhoneNo() {
        Intent intent = getIntent();
        String phoneNo = intent.getStringExtra("phoneNo").substring(3);
        et_phone.setText("0"+phoneNo);
    }

    private boolean isName() {
        return dupi;
    }

//    class DupiNameTask extends AsyncTask<Boolean,Boolean,Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Boolean... booleans) {
//            PreparedStatement st;
//            ResultSet rs;
//            String id = et_id.getText().toString();
//            String query = "SELECT * FROM `students` WHERE `id` = ?";
//            try {
//                Class.forName("org.mariadb.jdbc.Driver");
//                Connection connection = DriverManager.getConnection("jdbc:mariadb://eatx.shop:3307/student_db","wdj","wdj123");
//                st = connection.prepareStatement(query);
//                st.setString(1,id);
//                rs = st.executeQuery();
//
//                if(rs.next()) {
//                    Handler mHandler = new Handler(Looper.getMainLooper());
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            // 사용하고자 하는 코드
//
//                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"중복된 닉네임입니다",Snackbar.LENGTH_SHORT);
//                            snackbar.setAnchorView(findViewById(R.id.goRegister));
//                            snackbar.show();
//                        }
//                    }, 0);
//                    return dupi = true;
//                } else {
//                    return dupi = false;
//                }
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//                System.out.println("로그인 실패");
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            return dupi;
//        }
//    }
//    class Task extends AsyncTask<Void,Void,Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            PreparedStatement st;
//            String id = et_id.getText().toString();
//            String password = et_password.getText().toString();
//            String nickname = et_name.getText().toString();
//            String sid = et_sid.getText().toString();
//            String email = et_email.getText().toString();
//            String phone = et_phone.getText().toString();
//
//            String query = "INSERT INTO `students`(`id`, `password`, `sid`, `name`,`phoneNumber`,`email`) VALUES (?,?,?,?,?,?)";
//            try {
//                new DupiNameTask().execute();
//                if(!isName()) {
//                    Class.forName("org.mariadb.jdbc.Driver");
//                    Connection connection = DriverManager.getConnection("jdbc:mariadb://eatx.shop:3307/student_db", "wdj", "wdj123");
//                    st = connection.prepareStatement(query);
//                    st.setString(1, id);
//                    st.setString(2, password);
//                    st.setString(3, sid);
//                    st.setString(4, nickname);
//                    st.setString(5, phone);
//                    st.setString(6, email);
//                    if (st.executeUpdate() != 0) {
//                        Handler mHandler = new Handler(Looper.getMainLooper());
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                // 사용하고자 하는 코드
//                                Toast.makeText(Register.this,"회원가입 완료" ,Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(Register.this, MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.putExtra("id",id);
//                                startActivity(intent);
//                            }
//                        }, 0);
//                    } else {
//                        System.out.println("오류 발생");
//                    }
//                } else {
//
//                }
//
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//            return null;
//
//        }
//
//    }
}