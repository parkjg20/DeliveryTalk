package com.dataflow.deliverytalk.Activities.popup;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.dataflow.deliverytalk.Models.User;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.AppDataControlService;
import com.dataflow.deliverytalk.util.retrofit.CloudFunctionsRetrofit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutPopupActivity extends AppCompatActivity {

    private final String databaseUrl = "https://deliverytalk-31595.firebaseio.com";
    private Button yesButton;
    private Button noButton;

    private FirebaseAuth auth;

    // 더블 클릭 방지
    private boolean doubleClick = false;

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
        auth = FirebaseAuth.getInstance();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doubleClick) {
                    doubleClick = true;
                    User u = new User();
                    u.setUid(user.getUid());
                    Call<Void> res = CloudFunctionsRetrofit.getInstance().getService().deleteUser(u);
                    res.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Log.d("success", "success");
                            final String phoneNumber = user.getPhoneNumber();
                            final AppDataControlService appData = new AppDataControlService(getSharedPreferences("appData", MODE_PRIVATE));
                            FirebaseDatabase.getInstance(databaseUrl).getReference("Parcels").child(user.getPhoneNumber()).removeValue();
                            FirebaseDatabase.getInstance().getReference("Tokens").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String token = dataSnapshot.child(phoneNumber).getValue().toString();
                                    Log.d("logout_innerif_token", dataSnapshot.getValue().toString());
                                    FirebaseDatabase.getInstance().getReference("Messages").child(token).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("logout_messageDelete", "success");
                                                        FirebaseDatabase.getInstance().getReference("Tokens").child(phoneNumber).removeValue();
                                                        appData.setPhoneNumber(null);
                                                        appData.changeFlag("isLogin", false);
                                                        ActivityCompat.finishAffinity(LogoutPopupActivity.this);
                                                        System.exit(0);
                                                    } else {
                                                        Log.d("logout", "failed");
                                                    }
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.e("Error", t.getMessage().toLowerCase());
                        }
                    });
                }
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
