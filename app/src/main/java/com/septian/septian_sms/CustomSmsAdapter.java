package com.septian.septian_sms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomSmsAdapter extends ArrayAdapter<Sms> implements View.OnClickListener{

    private ArrayList<Sms> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtPhone;
        TextView txtMessage;
        TextView txtDate;
    }

    public CustomSmsAdapter(ArrayList<Sms> data, Context context) {
        super(context, R.layout.item_sms, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Sms dataModel=(Sms)object;

        Intent intent = new Intent(mContext, DetailSMSActivity.class);
        intent.putExtra("txtPhone", dataModel.getAddress());
        intent.putExtra("txtMessage", dataModel.getMsg());
        intent.putExtra("txtDate", millisToDate(Long.parseLong(dataModel.getTime())));
        mContext.startActivity(intent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sms dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_sms, parent, false);
            viewHolder.txtPhone = (TextView) convertView.findViewById(R.id.txtPhone);
            viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtPhone.setText(dataModel.getAddress());
        if (dataModel.getMsg().length() >= 30)
            viewHolder.txtMessage.setText(dataModel.getMsg().substring(0, 30) + "...");
        else
            viewHolder.txtMessage.setText(dataModel.getMsg());
        viewHolder.txtDate.setText(millisToDate(Long.parseLong(dataModel.getTime())));
        return convertView;
    }

    public static String millisToDate(long currentTime) {
        String finalDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        //android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date());
        Date date = calendar.getTime();
        finalDate = android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", date).toString();
        return finalDate;
    }
}
