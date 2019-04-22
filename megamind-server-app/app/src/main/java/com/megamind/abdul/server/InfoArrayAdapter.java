package com.megamind.abdul.server;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class InfoArrayAdapter extends ArrayAdapter<Info> {
    public InfoArrayAdapter(Context context, int resource, List<Info> myInfos) {
        super(context, resource, myInfos);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Info info = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.info_row, parent, false);
        }

        assert info != null;

        TextView title = (TextView) convertView.findViewById(R.id.info_row_title);
        title.setText(info.title);

        TextView desc = (TextView) convertView.findViewById(R.id.info_row_description);
        desc.setText(info.description);

        return convertView;
    }
}

class Info {
    String title;
    String description;

    Info(String title, String description) {
        this.title = title;
        this.description = description;
    }
}