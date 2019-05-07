package com.dataflow.deliverytalk.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dataflow.deliverytalk.Activities.popup.EventDialogPopup;
import com.dataflow.deliverytalk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class VerifyActivity extends AppCompatActivity {

    //elements
    private ConstraintLayout layout;
    private EditText verifyCode;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private String phonenumber;

    // firebase auth
    private FirebaseAuth mAuth;
    private String verificationId;
    private SharedPreferences appData;
    private CountDownTimer countDownTimer;
    private TextView verifyTimer;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // initialize elements
        layout = findViewById(R.id.verify_layout);
        verifyCode = findViewById(R.id.verify_code);
        verifyTimer = findViewById(R.id.verify_timer);
        prevButton = findViewById(R.id.verify_prevButton);
        nextButton = findViewById(R.id.verify_nextButton);
        nextButton.setEnabled(false);

        verifyTimer.bringToFront();
        phonenumber = getIntent().getStringExtra("phonenumber");
        Log.d("phone", "message"+phonenumber);
        if(phonenumber.isEmpty()||phonenumber.length() < 1){
            Log.e("error", "phonenumber is invalid");
            finish();
        }
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mAuth = FirebaseAuth.getInstance();

        setListeners();


        sendVerificationCode(phonenumber);
        countDownTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                verifyTimer.setText(String.format(((((millisUntilFinished % 60000L)/1000)/10>0)?"%d:%d":"%d:0%d"),( millisUntilFinished / 60000L), (millisUntilFinished % 60000L)/1000));
            }
            public void onFinish() {
                Intent intent = new Intent(VerifyActivity.this, EventDialogPopup.class);
                intent.putExtra("title", "[error 1]");
                intent.putExtra("content", "제한시간이 초과되었습니다.\n다시 시도하세요.");

                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void setListeners(){
        verifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern ps = Pattern.compile("[0-9]*$");
                Editable temp = verifyCode.getText();
                if(!ps.matcher(temp.toString()).matches()){
                    temp.delete(temp.length() - 1, temp.length());
                    verifyCode.setText(temp);
                    verifyCode.setError("숫자만 입력해주세요");
                    verifyCode.setSelection(temp.length());
                    return;
                }
                if(verifyCode.length() == 6){
                    verifyCode.setBackground(getDrawable(R.drawable.edit_text_verifying));
                    nextButton.setEnabled(true);
                }else{
                    verifyCode.setBackground(getDrawable(R.drawable.edit_text_border_background));
                    nextButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nextButton.isEnabled()){
                    countDownTimer.cancel();
                    String code = verifyCode.getText().toString().trim();
                    verifyCode(code);

                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //        화면 터치시 키보드 숨기기
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(verifyCode.getWindowToken(),0);
            }
        });
    }

    //인증번호 확인
    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
//                            countDownTimer.cancel();
                            Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                            saveUserLogin();
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(VerifyActivity.this, EventDialogPopup.class);
                            intent.putExtra("title","[error 1]");
                            intent.putExtra("content", "전화번호 인증에 실패했습니다.");

                        }
                    }
                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, 120, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallback
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(VerifyActivity.this)
                    .setTitle("Exception!")
                    .setMessage(e.getMessage()+"\n"+e.getStackTrace().toString())
                    .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {
                        }
                    }) .show(); // 팝업창 보여줌

        }
    };

    public void saveUserLogin(){
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("isLogin", true);
        phonenumber = phonenumber.replace("+82", "0");
        editor.putString("phonenumber", phonenumber);

        editor.apply();
    }



    // 첫 로그인시 배송조회 샘플 추가
    private void addSampleParcel(){

    }
}
