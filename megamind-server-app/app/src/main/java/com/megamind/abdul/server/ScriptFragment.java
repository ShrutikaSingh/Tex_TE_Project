package com.megamind.abdul.server;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class ScriptFragment extends Fragment {

    String mac_addr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.scripts_layout, container, false);

        final ListView listview = (ListView) view.findViewById(R.id.scripts_list_view);

        ArrayList<String> actionsName = new ArrayList<>();
        try {
            actionsName = ActionManager.
                    getActionsName(getActivity().getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
            actionsName.add("null");
        }

        final ArrayAdapter<String> saa = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.scripts_row, actionsName);

        mac_addr = MainActivity.getMac();
        listview.setOnItemClickListener(new MyOnClickListener(getContext(), mac_addr));
        listview.setAdapter(saa);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_refresh);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getActivity().getApplicationContext(),
                item.toString(), Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }
}


class MyOnClickListener implements AdapterView.OnItemClickListener {

    private Context mCtx;
    private String mac_addr;

    public MyOnClickListener(Context applicationContext, String mac_addr) {
        this.mCtx = applicationContext;
        this.mac_addr = mac_addr;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String name = (String) ((TextView) view).getText();

        if (mac_addr == null)
            return;

        try {
            ActionManager.sendAction(name, mCtx, mac_addr);
        } catch (IOException | JSONException e) {
            Toast.makeText(mCtx, "FAILED", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}