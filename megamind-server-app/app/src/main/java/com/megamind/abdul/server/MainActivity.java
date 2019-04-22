package com.megamind.abdul.server;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    static Toolbar toolbar;
    static String mac = null;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Menu drawerMenu;

    static String getMac() {
        return mac;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerMenu = mNavigationView.getMenu();

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();


        /**
         * This is required to handle the onItemClick on Navigation Drawer Items.
         */

        NavigationView.OnNavigationItemSelectedListener item_click_listener
                = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                Toast.makeText(getApplicationContext(), item.toString(), Toast.LENGTH_LONG).show();
                mac = item.toString();

                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                mDrawerLayout.closeDrawers();
                return true;

            }
        };

        /**
         * Attaching the above listener to NavigationView
         */

        mNavigationView.setNavigationItemSelectedListener(item_click_listener);

        /**
         * Setup Drawer Toggle of the Toolbar
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if (SharedPrefManager.getInstance(this).isPresent("devices")) {
            String savedDevices = SharedPrefManager.getInstance(this).getValue("devices");
            setMenu(savedDevices);
        }

        /**
         * Setting up the mac
         */
        if (Objects.equals(drawerMenu.getItem(0).toString(), "null"))
            mac = null;
        else
            mac = drawerMenu.getItem(0).toString();

        fetchRegDevices();

    }

    public void setMenu(String JSONResponse) {

        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("sys_devices");

            if (jsonArray.length() > 0) {
                drawerMenu.clear();
            } else
                return;

            for (int i = 0; i < jsonArray.length(); i++) {
                String s = jsonArray.getJSONObject(i).getString("mac_addr");
                drawerMenu.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void fetchRegDevices() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                EndPoints.URL_FETCH_DEVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            return;
                        }

                        FileHandler.saveRegisteredDevices(response);
                        Toast.makeText(getApplicationContext(),
                                "Devices refreshed", Toast.LENGTH_SHORT).show();
                        Log.e("Devices refreshed", response);
                        setMenu(response);
                        SharedPrefManager.getInstance(getApplication()).saveValue("devices", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Log.d("Volley error:", error.getMessage());
                            Toast.makeText(getApplicationContext(),
                                    "Unable to Connect!", Toast.LENGTH_SHORT).show();
                        } catch (Exception ignored) {
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("server_initials", Build.SERIAL);
                return map;
            }
        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        if (id == R.id.about) {
            startActivity(new Intent(MainActivity.this, About.class));
        }
        if (id == R.id.disclaimer) {
            startActivity(new Intent(MainActivity.this, DisclaimerActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


}