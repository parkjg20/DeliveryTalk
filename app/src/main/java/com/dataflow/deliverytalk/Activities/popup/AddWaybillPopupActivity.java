package com.dataflow.deliverytalk.Activities.popup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.SelectCarrierActivity;
import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.Models.Location;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.Models.Person;
import com.dataflow.deliverytalk.Models.Progress;
import com.dataflow.deliverytalk.Models.State;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.retrofit.ParcelTrackingRetrofit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWaybillPopupActivity extends AppCompatActivity {


    private ConstraintLayout layout;

    private EditText waybill;
    private TextView setCarrier;
    private EditText nickname;

    private Button cancelButton;
    private Button submitButton;
    private String carrierCode;
    private boolean defaultAlarm;

    private DatabaseReference parcels;
    private FirebaseFirestore db;
    private FirebaseFirestoreSettings settings;
    SharedPreferences appData;

    private ParcelModel parcelModel = new ParcelModel();

    // 더블 클릭 방지
    private boolean doubleClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_waybill_popup);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        waybill = findViewById(R.id.addwaybill_waybill);
        nickname = findViewById(R.id.addwaybill_productNickname);
        setCarrier = findViewById(R.id.addwaybill_carrier);
        cancelButton = findViewById(R.id.addwaybill_cancelButton);
        submitButton = findViewById(R.id.addwaybill_submitButton);

        defaultAlarm = appData.getBoolean("pushFlag", true);

        parcels = FirebaseDatabase.getInstance().getReference("Parcels").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        db = FirebaseFirestore.getInstance(FirebaseApp.getInstance("[DEFAULT]"));
        settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        setListeners();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            String temp = data.getStringExtra("carrierName");
            carrierCode = data.getStringExtra("code");
            setCarrier.setText(temp);
        }
    }

    private void setListeners(){

        waybill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern ps = Pattern.compile("[0-9]*$");
                Editable temp = waybill.getText();
                if(!ps.matcher(temp.toString()).matches()){
                    temp.delete(temp.length() - 1, temp.length());
                    waybill.setText(temp);
                    waybill.setError("숫자만 입력해주세요");
                    waybill.setSelection(temp.length());
                    return;
                }
                if(waybill.getText().length() > 9 ){
                    submitButton.setEnabled(true);
                    submitButton.setBackgroundColor(Color.parseColor("#0DCCB5"));
                }else{
                    submitButton.setEnabled(false);
                    submitButton.setBackgroundColor(Color.parseColor("#707070"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        setCarrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(AddWaybillPopupActivity.this, SelectCarrierActivity.class);
                startActivityForResult(intent, 0);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doubleClick){
                    doubleClick = true;
                    submitButton.setEnabled(false);
                    if(setCarrier.getText().equals("택배사를 선택해주세요.")){
                        setCarrier.setError("택배사를 선택해주세요");
                        doubleClick = false;
                        return;
                    }

                    parcelTracking(carrierCode, waybill.getText().toString());
                }
            }
        });

    }

    private void addWayBillToDatabase(@NonNull ParcelModel parcelModel){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(System.currentTimeMillis()));

        parcels.child(time).setValue(parcelModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                }else{
                    Intent intent = new Intent(AddWaybillPopupActivity.this, EventDialogPopup.class);
                    intent.putExtra("title", "[error 1]");
                    intent.putExtra("content","운송장 등록에 실패했습니다.");
                    startActivity(intent);
                }
                submitButton.setEnabled(true);
                doubleClick = false;
            }
        });


    }

    private void parcelTracking(String carrierId, final String waybill) {
        if(carrierCode.isEmpty()||waybill.length() < 12){
            Intent intent = new Intent(AddWaybillPopupActivity.this, EventDialogPopup.class);
            intent.putExtra("title", "[error 1]");
            intent.putExtra("content", "입력 값이 올바르지 않습니다.");
            startActivity(intent);
            doubleClick = false;
            return;
        }
        Log.d("before_parcel_track", carrierId+", "+waybill);
        Call<JsonObject> res = ParcelTrackingRetrofit.getInstance().getService().parcelTrack(carrierId, waybill);
        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 404){
                    try {
                        Log.e("add_waybill", response.code() + ", " + response.errorBody().string());
                    }catch(Exception e){
                        Log.e("add_waybill_exception", e.getStackTrace().toString());
                    }
                    Intent intent = new Intent(AddWaybillPopupActivity.this, EventDialogPopup.class);
                    intent.putExtra("title","[error 1]");
                    intent.putExtra("content", "운송장 정보를 찾을 수 없습니다.\n운송장 번호가 잘못되었거나\n아직 등록되지 않은 운송장입니다.");
                    startActivity(intent);
                    doubleClick = false;
                    return;
                }
                JsonObject json = response.body();
                String name = "[상품명 없음]";
                if(!nickname.getText().toString().isEmpty()){
                    name = nickname.getText().toString();
                }
                parcelModel.setTitle(name);
                parcelModel.setWaybill(waybill);
                parcelModel.setFrom(new Person(json.getAsJsonObject("from").getAsJsonPrimitive("name").getAsString(), json.getAsJsonObject("from").getAsJsonPrimitive("time").getAsString()));
                String time = json.getAsJsonObject("from").getAsJsonPrimitive("time").getAsString();
                if(time == null||time.isEmpty()){
                    time = "0000-00-00T00:00:00+00:00";
                }
                parcelModel.setTo(new Person(json.getAsJsonObject("to").getAsJsonPrimitive("name").getAsString(), time));
                parcelModel.setState(new State(json.getAsJsonObject("state").getAsJsonPrimitive("id").getAsString(), json.getAsJsonObject("state").getAsJsonPrimitive("text").getAsString()));
                List<Progress> list = new ArrayList<>();
                for(int i = 0; i < json.getAsJsonArray("progresses").size(); i++){
                    Progress progress = new Progress();
                    JsonObject jary = (JsonObject) json.getAsJsonArray("progresses").get(i);
                    progress.setTime(jary.getAsJsonPrimitive("time").getAsString());
                    progress.setDescription(jary.getAsJsonPrimitive("description").getAsString());
                    progress.setLocation(new Location(jary.getAsJsonObject("location").getAsJsonPrimitive("name").getAsString()));
                    progress.setStatus(new State(jary.getAsJsonObject("status").getAsJsonPrimitive("id").getAsString(),jary.getAsJsonObject("status").getAsJsonPrimitive("text").getAsString()));
                    list.add(progress);
                }
                parcelModel.setProgresses(list);
                parcelModel.setAlarm(defaultAlarm);

                db.collection("Carriers")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot t : task.getResult()){
                                        if(t.getId().equals(carrierCode)){
                                            parcelModel.setCarrier(new Carrier(
                                                    t.getId(),
                                                    t.getData().get("name").toString(),
                                                    t.getData().get("tel").toString(),
                                                    t.getData().get("logo").toString(),
                                                    t.getData().get("homepage").toString()
                                                )
                                            );
                                            addWayBillToDatabase(parcelModel);
                                        }
                                    }
                                }
                            }
                        });

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("failed", "url = "+ call.request().url().toString());
            }
        });

    }

}
