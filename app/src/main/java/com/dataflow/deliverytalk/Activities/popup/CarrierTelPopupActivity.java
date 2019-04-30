package com.dataflow.deliverytalk.Activities.popup;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dataflow.deliverytalk.R;

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

public class CarrierTelPopupActivity extends AppCompatActivity {

    private Button noButton;
    private Button yesButton;

    private String tel;
    private String carrierCode;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_carrier_tel_popup);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        carrierCode = getIntent().getStringExtra("carrierCode");

        if(carrierCode.isEmpty() || carrierCode.length()  < 1){
            Toast.makeText(this, "올바르지 않은 요청입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        noButton = findViewById(R.id.telPopup_no);
        yesButton = findViewById(R.id.telPopup_yes);
        title = findViewById(R.id.telPopup_title);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:"+tel));
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        new GetDataSync().execute();

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
            title.setText(tel);
        }
    }

    private void getData() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://apis.tracker.delivery/carriers/"+carrierCode);
        try {
            String response = json.getString("tel");
            Log.e("AAAAAAAAA %s", response);
            tel = response;

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
