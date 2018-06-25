package com.septian.septian_sms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etPhone, etMessage;
    Button btnInbox, btnSent, btnKirim;

    String phoneNumber, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            requestPermission();
        }

        etPhone = (EditText) findViewById(R.id.editTextPhone);
        etMessage = (EditText) findViewById(R.id.editTextMessage);
        btnInbox = (Button) findViewById(R.id.btnInbox);
        btnSent = (Button) findViewById(R.id.btnSent);
        btnKirim = (Button) findViewById(R.id.btnKirim);

        btnInbox.setOnClickListener(this);
        btnSent.setOnClickListener(this);
        btnKirim.setOnClickListener(this);
    }

    public void requestPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int permissionReceiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int permissionReadSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        List<String> lPermissions = new ArrayList<>();
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED)
            lPermissions.add(Manifest.permission.SEND_SMS);
        if (permissionReceiveSMS != PackageManager.PERMISSION_GRANTED)
            lPermissions.add(Manifest.permission.RECEIVE_SMS);
        if (permissionReadSMS != PackageManager.PERMISSION_GRANTED)
            lPermissions.add(Manifest.permission.READ_SMS);

        if (!lPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, lPermissions.toArray(new String[lPermissions.size()]),1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1 : {

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnInbox:
                Intent i1 = new Intent(this, InboxActivity.class);
                startActivity(i1);
                break;

            case R.id.btnSent:
                Intent i2 = new Intent(this, SentActivity.class);
                startActivity(i2);
                break;

            case R.id.btnKirim:
                phoneNumber = etPhone.getText().toString();
                message = etMessage.getText().toString();

                if (phoneNumber == null || phoneNumber.isEmpty() || !Patterns.PHONE.matcher(phoneNumber).matches()
                        || message == null || message.isEmpty()) {
                    Toast.makeText(this, "No Telpon dan Pesan tidak boleh Kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendSMS();
                etMessage.setText("");
                break;
        }
    }

    private void sendSMS() {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case MainActivity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS Terkirim", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "Tidak ada Layanan!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio Off!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case MainActivity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS Terkirim!", Toast.LENGTH_SHORT).show();
                        break;
                    case MainActivity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS TIDAK Terkirim!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
