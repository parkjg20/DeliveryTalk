package com.dataflow.deliverytalk.Activities.popup;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.dataflow.deliverytalk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class LogoutPopupActivity extends AppCompatActivity {

    private Button yesButton;
    private Button noButton;

    private SharedPreferences appData;
    private FirebaseAuth auth;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_logout_popup);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        yesButton = findViewById(R.id.logoutPopup_yes);
        noButton = findViewById(R.id.logoutPopup_no);
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor e = appData.edit();
                e.putString("phonenumber", null);
                e.putBoolean("isLogin", false);

                PhoneAuthCredential credential = PhoneAuthProvider
                        .getCredential(user.getUid(), null);

                user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    //재인증 성공시
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //삭제
                        user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("result", "User account deleted.");
                                }else{
                                    Log.d("", task.getException().getStackTrace().toString());
                                }
                            }
                        });
                    }
                });



                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

            }
        });
    }
}
