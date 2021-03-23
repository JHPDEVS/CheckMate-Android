package com.eatx.wdj.ui.Register;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.eatx.wdj.R;
import com.eatx.wdj.ui.login.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends AppCompatActivity {

    EditText usernameEdit, passwordEdit , nicknameEdit , sidEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
        nicknameEdit = (EditText) findViewById(R.id.nickname);
        sidEdit = (EditText) findViewById(R.id.sid);
        final Button registerButton = (Button) findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        // Enables Always-on

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                new Task().execute();
            }
        });


    }

    private boolean isName() {
        PreparedStatement st;
        ResultSet rs;
        String username = usernameEdit.getText().toString();
        Boolean dupi = false;
        String query = "SELECT * FROM `students` WHERE `id` = ?";
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mariadb://eatx.shop:3307/student_db","wdj","wdj123");
            st = connection.prepareStatement(query);
            st.setString(1,username);
            rs = st.executeQuery();

            if(rs.next()) {
                return dupi = true;
            } else {
                return dupi = false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("로그인 실패");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dupi;
    }
    class Task extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            PreparedStatement st;
            String username = usernameEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            String nickname = nicknameEdit.getText().toString();
            String sid = sidEdit.getText().toString();

            String query = "INSERT INTO `students`(`id`, `password`, `sid`, `name`) VALUES (?,?,?,?)";
            try {
                if(!isName()) {
                    System.out.println("중복된 닉네임아님");
                    Class.forName("org.mariadb.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mariadb://eatx.shop:3307/student_db", "wdj", "wdj123");
                    st = connection.prepareStatement(query);
                    st.setString(1, username);
                    st.setString(2, password);
                    st.setString(3, sid);
                    st.setString(4, nickname);
                    if (st.executeUpdate() != 0) {
                        System.out.println("업데이트 완료");
                    } else {
                        System.out.println("오류 발생");
                    }
                } else {
                    System.out.println("중복된 닉네임");
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;

        }

    }
}