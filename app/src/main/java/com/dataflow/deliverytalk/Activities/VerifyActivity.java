package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dataflow.deliverytalk.R;

import java.util.regex.Pattern;

public class VerifyActivity extends AppCompatActivity {

    private EditText verifyCode;
    private ImageButton prevButton;
    private ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // initialize elements
        verifyCode = findViewById(R.id.verify_code);
        prevButton = findViewById(R.id.verify_prevButton);
        nextButton = findViewById(R.id.verify_nextButton);
        nextButton.setEnabled(false);

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
                    Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 첫 로그인시 배송조회 샘플 추가
    private void addSampleParcel(){

    }
}
