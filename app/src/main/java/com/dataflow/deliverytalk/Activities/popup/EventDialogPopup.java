package com.dataflow.deliverytalk.Activities.popup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dataflow.deliverytalk.R;

public class EventDialogPopup extends AppCompatActivity {

    private TextView title;
    private TextView content;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dialog_popup);

        title = findViewById(R.id.eventPopup_title);
        content = findViewById(R.id.eventPopup_content);
        button = findViewById(R.id.eventPopup_close);

        title.setText(getIntent().getStringExtra("title"));
        content.setText(getIntent().getStringExtra("content"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
