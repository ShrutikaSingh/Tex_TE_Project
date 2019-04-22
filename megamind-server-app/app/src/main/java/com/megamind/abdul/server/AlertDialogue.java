package com.megamind.abdul.server;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class AlertDialogue extends AppCompatActivity {

    String actionName;
    String jsonObjString;
    String mac_addr;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_layout);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_alertbox);
        toolbar = (Toolbar) findViewById(R.id.alert_toolbar);

        actionName = getIntent().getExtras().getString("action_name");
        jsonObjString = getIntent().getExtras().getString("json");
        mac_addr = getIntent().getExtras().getString("mac_addr");
        final int action = getIntent().getExtras().getInt("action");

        toolbar.setTitle(actionName);
        setSupportActionBar(toolbar);

        try {
            JSONObject jsonObject = new JSONObject(jsonObjString);

            for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
                String key = iter.next();
                if (!key.equals("action_number")) {
                    TextView tv = new TextView(this);
                    tv.setPadding(10, 0, 0, 0);
                    tv.setText(key);
                    linearLayout.addView(tv);
                    EditText et = new EditText(this);
                    et.setText(jsonObject.getString(key));
                    linearLayout.addView(et);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR :", "JSON parsing error!!");
            finish();
        }

        Button send = (Button) findViewById(R.id.alert_btn_yes);
        Button cancel = (Button) findViewById(R.id.alert_btn_no);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.linear_layout_alertbox);
                JSONObject jsonObject = new JSONObject();

                try {
                    for (int i = 0; i < ll.getChildCount(); i = i + 2) {

                        TextView tv = (TextView) ll.getChildAt(i);
                        EditText et = (EditText) ll.getChildAt(i + 1);

                        String title = tv.getText().toString();
                        String text = et.getText().toString();

                        jsonObject.put(title, text);
                    }
                } catch (JSONException e) {
                    Toast.makeText(AlertDialogue.this, "FAILED -> JSON Exception", Toast.LENGTH_SHORT).show();
                    finish();
                }

                //Setting data to be sent
                MyVolley.getInstance(AlertDialogue.this).sendAction(action, mac_addr, jsonObject.toString());
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
