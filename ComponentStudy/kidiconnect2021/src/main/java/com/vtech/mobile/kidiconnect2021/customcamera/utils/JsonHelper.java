package com.vtech.mobile.kidiconnect2021.customcamera.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonHelper {
    private static final String TAG = "JsonHelper";

    public static String readJsonStr(String jsonPath) throws Exception {
        // read file
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonPath)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new Exception("read json file error: " + e.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }
    }

    public static void writerJsonStr(String jsonStr, String targetPath) throws Exception {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(targetPath);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            throw new Exception("write json file error");
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e1) {
                    Log.e(TAG, e1.getMessage());
                }
            }

        }
    }
}
