package com.dataflow.deliverytalk.Activities.popup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.SelectCarrierActivity;
import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.Models.Person;
import com.dataflow.deliverytalk.Models.Progress;
import com.dataflow.deliverytalk.Models.State;
import com.dataflow.deliverytalk.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AddWaybillPopupActivity extends AppCompatActivity {


    private ConstraintLayout layout;

    private EditText waybill;
    private TextView setCarrier;
    private EditText nickname;

    private Button submitButton;
    String nicknameText;
    String waybillText;
    String carrierCode;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_waybill_popup);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        waybill = findViewById(R.id.addwaybill_waybill);
        nickname = findViewById(R.id.addwaybill_productNickname);
        setCarrier = findViewById(R.id.addwaybill_carrier);

        ref = FirebaseDatabase.getInstance().getReference("parcels");

        setListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String temp = data.getStringExtra("carrierName");
        carrierCode = data.getStringExtra("code");
        setCarrier.setText(temp);
    }

    private void setListeners(){
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
                nicknameText = nickname.getText().toString();
                waybillText = waybill.getText().toString();

            }
        });

    }

    private void addWayBillToDatabase(){

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
        JSONObject json = readJsonFromUrl("https://apis.tracker.delivery/carriers/"+carrierCode+"/tracks"+waybill);
        try {
            if(json.get("message").toString() != null){
                json.get("message").toString();
            }

            //받아온 데이터 저장
            ParcelModel parcelModel = new ParcelModel();
            parcelModel.setFrom(new Person(json.getJSONObject("from").getString("name"), json.getJSONObject("from").getString("time")));
            parcelModel.setTo(new Person(json.getJSONObject("to").getString("name"), json.getJSONObject("from").getString("time")));
            parcelModel.setState(new State(json.getJSONObject("state").getString("id"), json.getJSONObject("state").getString("text")));
            List<Progress> list = new ArrayList<>();
            for(int i = 0; i < json.getJSONArray("progresses").length(); i++){
                Progress progress = (Progress)json.getJSONArray("progresses").get(i);
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
