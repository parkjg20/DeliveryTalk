package com.dataflow.deliverytalk.Activities.popup;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.retrofit.FunctionRetrofit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                Log.d("uid", user.getUid());
                Call<Void> res = FunctionRetrofit.getInstance().getService().deleteUser(user.getUid());
                res.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        Log.d("success", "suceess");
                        SharedPreferences.Editor e = appData.edit();
                        e.putString("phonenumber", null);
                        e.putBoolean("isLogin", false);
                        e.commit();
//                        ActivityCompat.finishAffinity(LogoutPopupActivity.this);
//                        System.exit(0);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("Error", t.getMessage().toLowerCase());
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
}
