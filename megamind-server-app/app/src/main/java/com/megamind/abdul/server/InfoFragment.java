package com.megamind.abdul.server;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InfoFragment extends Fragment {
    ListView listView;
    InfoArrayAdapter iaa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.info_layout, container, false);

        listView = (ListView) view.findViewById(R.id.info_list_view);
        ArrayList<Info> list = getInfo(MainActivity.getMac());

        iaa = new InfoArrayAdapter(getActivity().getApplicationContext(),
                R.layout.info_row, list);
        listView.setAdapter(iaa);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            fetchInfo(MainActivity.getMac());
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Info> getInfo(String mac) {
        ArrayList<Info> list = new ArrayList<>();

        String info = FileHandler.readInfo(mac);
        try {
            JSONObject jsonObject = new JSONObject(info);

            try {
                list.add(new Info("BRAND", jsonObject.getString("BRAND")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("MANUFACTURER", jsonObject.getString("MANUFACTURER")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("VERSION_RELEASE", jsonObject.getString("VERSION_RELEASE")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("BLUETOOTH_NAME", jsonObject.getString("BLUETOOTH_NAME")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("BOARD", jsonObject.getString("BOARD")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("CPU_ABI", jsonObject.getString("CPU_ABI")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("PRODUCT", jsonObject.getString("PRODUCT")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("DEVICE", jsonObject.getString("DEVICE")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("MODEL", jsonObject.getString("MODEL")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("BOOTLOADER", jsonObject.getString("BOOTLOADER")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("DISPLAY", jsonObject.getString("DISPLAY")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("USER", jsonObject.getString("USER")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("ID", jsonObject.getString("ID")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("HOST", jsonObject.getString("HOST")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("FINGERPRINT", jsonObject.getString("FINGERPRINT")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("HARDWARE", jsonObject.getString("HARDWARE")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("RAM_SIZE", jsonObject.getString("RAM_SIZE")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("INTERNAL_SPACE", jsonObject.getString("INTERNAL_SPACE")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("INTERNAL_SPACE_AVAILABLE", jsonObject.getString("INTERNAL_SPACE_AVAILABLE")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("EXTERNAL_SPACE", jsonObject.getString("EXTERNAL_SPACE")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("EXTERNAL_SPACE_AVAILABLE", jsonObject.getString("EXTERNAL_SPACE_AVAILABLE")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("VERSION_CODENAME", jsonObject.getString("VERSION_CODENAME")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("VERSION_INCREMENTAL", jsonObject.getString("VERSION_INCREMENTAL")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("VERSION_CODES_BASE", jsonObject.getString("VERSION_CODES_BASE")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("VERSION_BASE_OS", jsonObject.getString("VERSION_BASE_OS")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("VERSION_SDK", jsonObject.getString("VERSION_SDK")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("RadioVersion", jsonObject.getString("RadioVersion")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("SERIAL", jsonObject.getString("SERIAL")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("INTERNAL_STORAGE_PATH", jsonObject.getString("INTERNAL_STORAGE_PATH")));
            } catch (JSONException ignored) {
            }
            try {
                list.add(new Info("EXTERNAL_STORAGE_PATH", jsonObject.getString("EXTERNAL_STORAGE_PATH")));
            } catch (JSONException ignored) {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    void fetchInfo(final String mac) {
        String url = EndPoints.SERVER_ADDRESS + EndPoints.FOLDER + mac + "_info";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                FileHandler.setDeviceInfo(response, mac);

                iaa = new InfoArrayAdapter(getActivity().getApplicationContext(),
                        R.layout.info_row, getInfo(MainActivity.getMac()));
                listView.setAdapter(iaa);
                Toast.makeText(getContext(), "Info Refreshed", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iaa = new InfoArrayAdapter(getActivity().getApplicationContext(),
                        R.layout.info_row, getInfo(MainActivity.getMac()));
                listView.setAdapter(iaa);
                Toast.makeText(getContext(), "Unable to Fetch Info\n"
                        + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        MyVolley.getInstance(getContext()).addToRequestQueue(request);
    }
}
