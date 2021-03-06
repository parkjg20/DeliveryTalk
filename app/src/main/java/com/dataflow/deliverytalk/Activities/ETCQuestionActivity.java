package com.dataflow.deliverytalk.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.popup.EventDialogPopup;
import com.dataflow.deliverytalk.Activities.popup.SendQuestionPopupActivity;
import com.dataflow.deliverytalk.Models.QuestionModel;
import com.dataflow.deliverytalk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ETCQuestionActivity extends AppCompatActivity {

    private final String databaseUrl = "https://deliverytalk-31595.firebaseio.com";
    private Spinner type;
    private EditText title;
    private EditText content;
    private EditText email;
    private Button submitButton;
    private ImageButton prevButton;
    private TextView contentCnt;

    private boolean doubleClick = false;
    private ConstraintLayout layout;
    InputMethodManager imm;

    DatabaseReference ref;

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_question);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        prevButton = findViewById(R.id.question_prevButton);
        layout = findViewById(R.id.question_layout);
        type = findViewById(R.id.question_typeSpinner);
        title = findViewById(R.id.question_titleText);
        content = findViewById(R.id.question_contentText);
        email = findViewById(R.id.question_emailText);
        submitButton = findViewById(R.id.question_submitButton);
        contentCnt = findViewById(R.id.question_contentCounter);
        contentCnt.bringToFront();

        ref = FirebaseDatabase.getInstance(databaseUrl).getReference("Questions");

        setListeners();

        adapter = new ArrayAdapter(
                getApplicationContext(),
                R.layout.question_spinner,
                getResources().getStringArray(R.array.types));

        adapter.setDropDownViewResource(R.layout.question_spinner_dropdown);
        type.setAdapter(adapter);

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
        if(!doubleClick) {
            QuestionModel qm = new QuestionModel();

            qm.setType(type.getSelectedItem().toString());
            qm.setContent(content.getText().toString());
            qm.setEmail( email.getText().toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date(System.currentTimeMillis()));
            qm.setWdate(time);
            qm.setSolved(false);

            ref.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child(title.getText().toString()).setValue(qm)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(ETCQuestionActivity.this, SendQuestionPopupActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(ETCQuestionActivity.this, EventDialogPopup.class);
                                intent.putExtra("title", "[error 2]");
                                intent.putExtra("content", "문의 등록 중 문제가 발생했습니다.");
                                startActivity(intent);
                            }
                        }
                    });
            doubleClick = false;
        }

    }


    private void setListeners(){

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

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
                contentCnt.setText("("+content.getText().length()+"/500)");
                if(title.getText().length() > 0 && content.getText().length() > 0 && email.getText().length() > 0){
                    submitButton.setBackgroundColor(Color.parseColor("#0DCCB5"));
                    submitButton.setEnabled(true);
                }else{
                    submitButton.setBackgroundColor(Color.parseColor("#DEDEDE"));
                    submitButton.setEnabled(false);
                }
                if(content.getText().length() > 500) {
                    Editable temp = content.getText();
                    temp.delete(500, temp.length());
                    content.setSelection(temp.length());
                    content.setText(temp);
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
