package com.dataflow.deliverytalk.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dataflow.deliverytalk.R;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout header;
    private ConstraintLayout layout;

    private EditText phoneText;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private String phoneNumber;

    InputMethodManager imm;
    private boolean doubleClick= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        header = findViewById(R.id.login_header);
        header.setBackgroundColor(Color.parseColor("#FFFFFF"));
        layout = findViewById(R.id.login_layout);

        phoneText = findViewById(R.id.login_phoneText);
        nextButton = findViewById(R.id.login_nextButton);
        nextButton.setEnabled(false);
        prevButton = findViewById(R.id.login_prevButton);

        phoneText = (EditText) findViewById(R.id.login_phoneText);
        phoneNumber = getIntent().getStringExtra("phonenumber");

        // 전화번호 초기화
        if(phoneNumber != null ||!phoneNumber.equals("")) {
            if(phoneNumber.startsWith("+82")){
                phoneNumber = phoneNumber.substring(2);
            }
            phoneText.setText(phoneNumber);
        }

        setListeners();
    }

    // 리스너 등록
    private void setListeners(){

        // 전화번호
        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Pattern ps = Pattern.compile("[0-9]*$");
                Editable temp = phoneText.getText();
                if(!ps.matcher(temp.toString()).matches()){
                    temp.delete(temp.length() - 1, temp.length());
                    phoneText.setText(temp);
                    phoneText.setError("숫자만 입력해주세요");
                    phoneText.setSelection(temp.length());
                    return;
                }
                if(temp.length() == 11){
                    phoneText.setBackground(getDrawable(R.drawable.edit_text_verifying));
                    nextButton.setEnabled(true);
                }else {
                    phoneText.setBackground(getDrawable(R.drawable.edit_text_border_background));
                    nextButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 11){
                    nextButton.setEnabled(true);
                }
            }
        });

//        화면 터치시 키보드 숨기기
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(phoneText.getWindowToken(),0);
            }
        });

        // 다음 버튼
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doubleClick){
                    doubleClick = true;
                    if(nextButton.isEnabled()){
                        String code = "82";
                        phoneNumber = phoneText.getText().toString().trim();
                        if(phoneNumber.equals("16505551234")){
                            phoneNumber = "+16505551234";
                        }else{
                            phoneNumber = "+"+code+phoneNumber.substring(1);
                        }

                        Intent intent = new Intent(LoginActivity.this, VerifyActivity.class );
                        intent.putExtra("phoneNumber", phoneNumber);
                        startActivity(intent);
                    }
                    doubleClick = false;
                }
            }
        });

        // 이전 버튼
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
