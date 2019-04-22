package com.megamind.abdul.server;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class LogsArrayAdapter extends ArrayAdapter<MyLog> {

    public LogsArrayAdapter(Context context, int resource, List<MyLog> myLogs) {
        super(context, resource, myLogs);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyLog l = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.logs_row, parent, false);
        }

        assert l != null;

        TextView title = (TextView) convertView.findViewById(R.id.logs_row_title);
        title.setText(l.title);

        TextView desc = (TextView) convertView.findViewById(R.id.logs_row_description);
        desc.setText(l.description);

        TextView time = (TextView) convertView.findViewById(R.id.logs_row_time);
        time.setText(l.time);

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {


    }
}

class MyLog {
    String title;
    String description;
    String time;

    MyLog(String title, String description, String time) {
        this.title = title;
        this.description = description;
        this.time = time;
    }
}
