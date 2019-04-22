package com.megamind.abdul.server;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyVolley {

    private static MyVolley mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private MyVolley(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized MyVolley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyVolley(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void sendAction(final int action, final String mac_addr, final String data_string) {
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_SINGLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response :", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error"))
                                Toast.makeText(mCtx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(mCtx, "SUCCESS", Toast.LENGTH_SHORT).show();
                        } catch (JSONException ignored) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mCtx, "FAILED", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", String.valueOf(action));
                map.put("server_initials", getServerInitials());
                map.put("mac_addr", mac_addr);
                map.put("data_string", data_string);
                return map;
            }
        };
        addToRequestQueue(request);
    }

    private String getServerInitials() {
        return Build.SERIAL;
    }

}