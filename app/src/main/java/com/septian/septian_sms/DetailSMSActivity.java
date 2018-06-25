package com.septian.septian_sms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailSMSActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtPhone, txtMessage, txtDate;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sms);

        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        txtDate = (TextView) findViewById(R.id.txtDate);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            txtPhone.setText(extras.getString("txtPhone"));
            txtMessage.setText(extras.getString("txtMessage"));
            txtDate.setText(extras.getString("txtDate"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
