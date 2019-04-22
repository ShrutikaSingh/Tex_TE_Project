package com.megamind.abdul.server;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

class ActionManager {
    private static AssetManager assetManager;

    static ArrayList<String> getActionsName(Context context) throws IOException {
        ArrayList<String> actions = new ArrayList<>();
        assetManager = context.getAssets();

        String[] files = assetManager.list("");

        for (String file_name : files) {
            if (file_name.endsWith(".json"))
                actions.add(file_name.replaceFirst("[.][^.]+$", ""));
        }

        return actions;
    }

    static void sendAction(String actionName, Context context, String mac_addr)
            throws IOException, JSONException {
        int action;
        assetManager = context.getAssets();
        InputStream is = assetManager.open(actionName + ".json");

        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder data_json_string = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            data_json_string.append(line).append('\n');
        }

        JSONObject object = new JSONObject(data_json_string.toString());
        action = object.getInt("action_number");

        //ALERT DIALOGUE BOX !!
        Intent in = new Intent(context, AlertDialogue.class);
        in.putExtra("action", action);
        in.putExtra("action_name", actionName);
        in.putExtra("mac_addr", mac_addr);
        in.putExtra("json", object.toString());
        context.startActivity(in);

    }
}
