package com.eatx.wdj.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.eatx.wdj.ui.Register.Register;
import com.eatx.wdj.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eatx.wdj.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerficationId;
    private static final String TAG = "MAIN_TAG";
    private FirebaseAuth firebaseAuth;

    private ProgressDialog pd;
    private TextView AuthButton;
    private TextView phoneNo, codeNo,reButton;
    private TextView NextButton;
    private EditText et_code;
    private AppCompatImageView BackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);


        firebaseAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog((this));
        pd.setTitle("체크메이트");
        pd.setCanceledOnTouchOutside(false);

        AuthButton = findViewById(R.id.tv_auth_request);
        phoneNo = findViewById((R.id.et_phone));
        reButton = findViewById(R.id.tv_retry_auth);
        NextButton = findViewById(R.id.tv_auth_next);
        codeNo = findViewById(R.id.et_enter_code);
        et_code = findViewById(R.id.et_enter_code);
        BackButton = findViewById(R.id.iv_back);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                signInWithPone(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                pd.dismiss();
                Toast.makeText(PhoneAuth.this, "인증실패", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String vertificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(vertificationId, forceResendingToken);

                mVerficationId = vertificationId;
                forceResendingToken = token;
                pd.dismiss();

                Toast.makeText(PhoneAuth.this, "코드 전송됨", Toast.LENGTH_SHORT).show();
            }
        };

        AuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneEditTextValue = phoneNo.getText().toString().trim();
                if (TextUtils.isEmpty(phoneEditTextValue)) {
                    Toast.makeText(PhoneAuth.this, "휴대폰 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    String phone = "+82"+phoneEditTextValue.substring(1);
                    System.out.println(phone);
                    reSend(phone , forceResendingToken);
                    et_code.setEnabled(true);
                    NextButton.setEnabled(true);
                }
            }
        });

        reButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneEditTextValue = phoneNo.getText().toString().trim();
                if (TextUtils.isEmpty(phoneEditTextValue)) {
                    Toast.makeText(PhoneAuth.this, "휴대폰 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    String phone = "+82"+phoneEditTextValue.substring(1);
                    System.out.println(phone);
                    reSend(phone , forceResendingToken);
                    et_code.setEnabled(true);
                    NextButton.setEnabled(true);
                }
            }
        });
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeNo.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(PhoneAuth.this, "인증번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    verifyPhoneNumberWithCode(mVerficationId,code);
                }
            }
        });
    }
    private void startPhoneNumberVerification(String phone) {
        pd.setMessage("휴대폰 인증");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void reSend(String phone , PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("잠시 기달려주세요");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L,TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String vid, String code) {
        pd.setMessage("확인 중");
        pd.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(vid,code);
        signInWithPone(credential);
    }

    private void signInWithPone(PhoneAuthCredential credential) {
        pd.setMessage("인증 중");

        firebaseAuth.signInWithCredential((credential))
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //로그인 성공
                            pd.dismiss();
                            String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                            Toast.makeText(PhoneAuth.this, "인증 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhoneAuth.this, Register.class
                            );
                            intent.putExtra("phoneNo",phone);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(PhoneAuth.this, "인증 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
    }
}