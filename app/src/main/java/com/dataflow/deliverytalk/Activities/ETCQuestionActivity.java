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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dataflow.deliverytalk.Activities.popup.SendQuestionPopupActivity;
import com.dataflow.deliverytalk.R;

public class ETCQuestionActivity extends AppCompatActivity {


    private Spinner type;
    private EditText title;
    private EditText content;
    private EditText email;
    private Button submitButton;

    private ConstraintLayout layout;
    InputMethodManager imm;

    ArrayAdapter adapter;
    String[] spinnerValues = {"개선 요청 사항", "오류 신고"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_question);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        layout = findViewById(R.id.question_layout);
        type = findViewById(R.id.question_typeSpinner);
        title = findViewById(R.id.question_titleText);
        content = findViewById(R.id.question_contentText);
        email = findViewById(R.id.question_emailText);
        submitButton = findViewById(R.id.question_submitButton);

        addListeners();

        adapter = new ArrayAdapter(
                getApplicationContext(),
                R.layout.question_spinner,
                spinnerValues);

        adapter.setDropDownViewResource(R.layout.question_spinner_dropdown);
        type.setAdapter(adapter);







        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    private void validate(){
        if(submitButton.isEnabled()){
            if(type.getSelectedItem().toString().equals("문의 유형을 선택해주세요.")){
                type.requestFocus();
                return;
            }
            if(title.getText().toString().isEmpty()){
                title.requestFocus();
                title.setError("제목을 입력해주세요");
                return;
            }
            if(content.getText().toString().isEmpty()){
                content.requestFocus();
                content.setError("내용을 입력해주세요");
                return;
            }
            send();
        }
    }

    private void send(){
        System.out.println("type : "+type.getSelectedItem().toString()+ ", title : "+ title.getText().toString()+"content: "+content.getText().toString()+"\nemail"+email.getText().toString());

        Intent intent = new Intent(ETCQuestionActivity.this, SendQuestionPopupActivity.class);
        startActivity(intent);
    }


    private void addListeners(){
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(title.getText().length() > 0 && content.getText().length() > 0 && email.getText().length() > 0){
                    submitButton.setBackgroundColor(Color.parseColor("#0DCCB5"));
                    submitButton.setEnabled(true);
                }else{
                    submitButton.setBackgroundColor(Color.parseColor("#DEDEDE"));
                    submitButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(title.getText().length() > 0 && content.getText().length() > 0 && email.getText().length() > 0){
                    submitButton.setBackgroundColor(Color.parseColor("#0DCCB5"));
                    submitButton.setEnabled(true);
                }else{
                    submitButton.setBackgroundColor(Color.parseColor("#DEDEDE"));
                    submitButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(title.getText().length() > 0 && content.getText().length() > 0 && email.getText().length() > 0){
                    submitButton.setBackgroundColor(Color.parseColor("#0DCCB5"));
                    submitButton.setEnabled(true);
                }else{
                    submitButton.setBackgroundColor(Color.parseColor("#DEDEDE"));
                    submitButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        화면 터치시 키보드 숨기기
                imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
            }
        });

    }
}
