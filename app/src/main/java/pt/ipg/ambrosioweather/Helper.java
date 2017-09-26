package pt.ipg.ambrosioweather;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by RT on 16/01/2017.
 */

public class Helper {

    //Get JSON code from a URL
    public Weather GetJSONFromURL(URL[] myURL) {
        //array due to async task requirements
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();

        try {
            conn = (HttpURLConnection) myURL[0].openConnection();
            conn.connect();

            InputStream stream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String jsonString = buffer.toString();
        return GetWeatherFromJSONString(jsonString);
    }

    //Parse JSON, create campaigns and set their properties
    public Weather GetWeatherFromJSONString(String imjson) {

        Weather w = new Weather();
        try {
            JSONObject jsonObject = new JSONObject(imjson);
            //Cidade
            w.setCity(jsonObject.getJSONObject("current_observation").getJSONObject("display_location").optString("city"));
            //Temperatura
            w.setTemperature(jsonObject.getJSONObject("current_observation").optDouble("temp_c"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return w;
    }

    //What you want Ambrosio to answer
    public String WhatToSay(Context context, String temperature) {

        Double t = Double.parseDouble(temperature);
        String[] falas;

        if (t > 5.0) {
            falas = context.getResources().getStringArray(R.array.cold_answers);
        } else if (t > 15.0) {
            falas = context.getResources().getStringArray(R.array.medium_answers);
        } else {
            falas = context.getResources().getStringArray(R.array.hot_answers);
        }

        Random rand = new Random();
        int choice = rand.nextInt(3);
        String to_say = falas[choice];
        return to_say;
    }

}