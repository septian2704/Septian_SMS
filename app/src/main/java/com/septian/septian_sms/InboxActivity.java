package com.septian.septian_sms;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity {

    ListView lvInbox;
    ArrayList<Sms> lSMS;
    CustomSmsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        lvInbox = (ListView) findViewById(R.id.lvInbox);

        lSMS = getAllSms();

        adapter = new CustomSmsAdapter(lSMS, this);
        lvInbox.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sms dataModel = lSMS.get(i);

                Intent intent = new Intent(InboxActivity.this, DetailSMSActivity.class);
                intent.putExtra("txtPhone", dataModel.getAddress());
                intent.putExtra("txtMessage", dataModel.getMsg());
                intent.putExtra("txtDate", adapter.millisToDate(Long.parseLong(dataModel.getTime())));
                InboxActivity.this.startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    public ArrayList<Sms> getAllSms() {
        ArrayList<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/inbox");
        ContentResolver cr = this.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        this.startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        return lstSms;
    }
}
