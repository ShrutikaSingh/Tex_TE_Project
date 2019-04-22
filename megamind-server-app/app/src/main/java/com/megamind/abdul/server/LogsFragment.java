package com.megamind.abdul.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class LogsFragment extends Fragment {

    static LogsFragment.LogUpdateReceiver receiver;
    LogsArrayAdapter laa;
    ListView listview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.logs_layout, container, false);

        listview = (ListView) view.findViewById(R.id.logs_list_view);
        ArrayList<MyLog> list = getAllLogs(MainActivity.getMac());

        laa = new LogsArrayAdapter(getActivity().getApplicationContext(),
                R.layout.logs_row, list);
        listview.setAdapter(laa);

        //Registering Log Update Receiver
        receiver = new LogsFragment.LogUpdateReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter("log_update"));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null)
            getActivity().unregisterReceiver(receiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            fetchAllLogs(MainActivity.getMac());
        }
        return super.onOptionsItemSelected(item);
    }

    void fetchAllLogs(final String mac) {
        String url = EndPoints.SERVER_ADDRESS + EndPoints.FOLDER + mac + "_logs";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                FileHandler.saveLogs(response, mac);

                laa = new LogsArrayAdapter(getActivity().getApplicationContext(),
                        R.layout.logs_row, getAllLogs(MainActivity.getMac()));
                listview.setAdapter(laa);
                Toast.makeText(getContext(), "Logs Refreshed", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                laa = new LogsArrayAdapter(getActivity().getApplicationContext(),
                        R.layout.logs_row, getAllLogs(MainActivity.getMac()));
                listview.setAdapter(laa);
                Toast.makeText(getContext(), "Unable to Fetch Logs\n"
                        + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        MyVolley.getInstance(getContext()).addToRequestQueue(request);
    }

    ArrayList<MyLog> getAllLogs(String mac) {
        ArrayList<MyLog> myLogs = new ArrayList<>();

        String logs = "[" + FileHandler.readLogs(mac) + "]";

        try {
            JSONArray jsonArray = new JSONArray(logs);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                String title = row.getString("action_name");
                String time = row.getString("time");
                StringBuilder description = new StringBuilder("");

                for (Iterator<String> iter = row.keys(); iter.hasNext(); ) {
                    String key = iter.next();
                    if (!(key.equals("action_name") || key.equals("time")))
                        description.append(key).append(" : ").append(row.get(key)).append("\n");
                }

                myLogs.add(new MyLog(title, description.toString().trim(), time));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return myLogs;
    }

    public class LogUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<MyLog> list = getAllLogs(MainActivity.getMac());
            laa = new LogsArrayAdapter(getActivity().getApplicationContext(),
                    R.layout.logs_row, list);
            listview.setAdapter(laa);

        }
    }

}