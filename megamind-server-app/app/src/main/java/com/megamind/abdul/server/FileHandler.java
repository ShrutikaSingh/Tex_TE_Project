package com.megamind.abdul.server;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class FileHandler {
    public static void saveRegisteredDevices(String JSONResponse) {
        File file = new File(Environment.getExternalStorageDirectory(), "Server");
        file.mkdirs();
        file = new File(Environment.getExternalStorageDirectory(), "Server/Devices.txt");


        try {
            OutputStream fo = new FileOutputStream(file);
            fo.write(JSONResponse.getBytes());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveLogs(String JSONResponse, String mac) {

        File file = new File(Environment.getExternalStorageDirectory(),
                "Server/" + mac + "_logs" + ".json");
        try {
            OutputStream fo = new FileOutputStream(file);
            fo.write(JSONResponse.getBytes());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateLogs(String JSONResponse, String mac) {

        File file = new File(Environment.getExternalStorageDirectory(),
                "Server/" + mac + "_logs" + ".json");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                result.append(line);

            file.delete();
            OutputStream fo = new FileOutputStream(file);
            fo.write((JSONResponse + "," + result.toString()).getBytes());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readLogs(String mac) {
        StringBuilder myLogs = new StringBuilder();

        File file = new File(Environment.getExternalStorageDirectory(),
                "Server/" + mac + "_logs" + ".json");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
                myLogs.append(line);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return myLogs.toString();
    }

    public static void setDeviceInfo(String JSONResponseString, String mac) {

        File file = new File(Environment.getExternalStorageDirectory(),
                "Server/" + mac + "_info" + ".json");

        try {
            OutputStream fo = new FileOutputStream(file);
            fo.write(JSONResponseString.getBytes());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readInfo(String mac) {
        StringBuilder myInfo = new StringBuilder();

        File file = new File(Environment.getExternalStorageDirectory(),
                "Server/" + mac + "_info" + ".json");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
                myInfo.append(line);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return myInfo.toString();
    }
}