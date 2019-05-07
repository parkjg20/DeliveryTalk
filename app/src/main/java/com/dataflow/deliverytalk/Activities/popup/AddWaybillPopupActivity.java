package com.dataflow.deliverytalk.Activities.popup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.SelectCarrierActivity;
import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.Models.Person;
import com.dataflow.deliverytalk.Models.Progress;
import com.dataflow.deliverytalk.Models.State;
import com.dataflow.deliverytalk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddWaybillPopupActivity extends AppCompatActivity {


    private ConstraintLayout layout;

    private ParcelModel parcelModel;
    private EditText waybill;
    private TextView setCarrier;
    private EditText nickname;

    private Button submitButton;
    String nicknameText;
    String waybillText;
    String carrierCode;
    private boolean defaultAlarm;

    DatabaseReference ref;
    SharedPreferences appData;

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
        submitButton = findViewById(R.id.addwaybill_submitButton);

        defaultAlarm = appData.getBoolean("pushFlag", true);

        ref = FirebaseDatabase.getInstance("https://deliverytalk-31595.firebaseio.com").getReference("Parcels").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

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
                if(waybill.getText().length() > 9 && nickname.getText().length()>0){
                    submitButton.setEnabled(true);
                    submitButton.setBackgroundColor(Color.parseColor("#0DCCB5"));
                }else{
                    submitButton.setEnabled(false);
                    submitButton.setBackgroundColor(Color.parseColor("#DEDEDE"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(waybill.getText().length() > 9 && nickname.getText().length()>0){
                    submitButton.setEnabled(true);
                    submitButton.setBackgroundColor(Color.parseColor("#0DCCB5"));
                }else{
                    submitButton.setEnabled(false);
                    submitButton.setBackgroundColor(Color.parseColor("#DEDEDE"));
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setCarrier.getText().equals("택배사를 선택해주세요.")){
                    setCarrier.setError("택배사를 선택해주세요");
                    return;
                }
                nicknameText = nickname.getText().toString();
                waybillText = waybill.getText().toString();
                Log.d("variables", "id"+carrierCode+", waybill"+waybillText);
                new GetDataSync().execute();

            }
        });

    }

    private void addWayBillToDatabase(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss");
        String time = sdf.format(new Date(System.currentTimeMillis()));
        Log.d("time", time);

        ref.child(time).setValue(parcelModel)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("result", "success");
                    Log.d("ref", ref.getKey());
                    finish();
                } else {
                    Log.d("result", "failure");
                    Intent intent = new Intent(AddWaybillPopupActivity.this, EventDialogPopup.class);
                    intent.putExtra("title", "[error 1]");
                    intent.putExtra("content","운송장 등록에 실패했습니다.");
                }
            }
        });

    }


    public class GetDataSync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getData();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //결과 저장하는 메소드
            addWayBillToDatabase();
        }
    }

    private void getData() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://apis.tracker.delivery/carriers/"+carrierCode+"/tracks/"+waybillText);
        try {
            Log.d("length", json.length()+"");
            if(json.length() == 1){
                Log.d("message", json.get("message").toString());

                // 존재하지 않는 운송장 팝업 출력

                return;
            }

            //받아온 데이터 저장
            parcelModel = new ParcelModel();
            parcelModel.setTitle(nicknameText);
            parcelModel.setWaybill(waybillText);
            parcelModel.setFrom(new Person(json.getJSONObject("from").getString("name"), json.getJSONObject("from").getString("time")));
            parcelModel.setTo(new Person(json.getJSONObject("to").getString("name"), json.getJSONObject("from").getString("time")));
            parcelModel.setState(new State(json.getJSONObject("state").getString("id"), json.getJSONObject("state").getString("text")));
            List<Progress> list = new ArrayList<>();
            for(int i = 0; i < json.getJSONArray("progresses").length(); i++){
                Progress progress = new Progress();
                JSONObject jary = (JSONObject) json.getJSONArray("progresses").get(i);
                progress.setTime(jary.getString("time"));
                progress.setDescription(jary.getString("description"));
                progress.setLocation(jary.getString("location"));
                progress.setStatus(new State(jary.getJSONObject("status").getString("id"),jary.getJSONObject("status").getString("text")));
                list.add(progress);
            }
            parcelModel.setProgresses(list);
            parcelModel.setCarrier(
                    new Carrier(
                            json.getJSONObject("carrier").getString("id"),
                            json.getJSONObject("carrier").getString("name"),
                            json.getJSONObject("carrier").getString("tel")
                    )
            );
            Log.d("alarm", defaultAlarm+"");
            parcelModel.setAlarm(defaultAlarm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
