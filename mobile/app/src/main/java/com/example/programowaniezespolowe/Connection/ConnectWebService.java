package com.example.programowaniezespolowe.Connection;

import com.example.programowaniezespolowe.Data.Device;
import com.example.programowaniezespolowe.Data.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectWebService {
    private static ConnectWebService instance;
    private final static String TOKEN = "http://54.37.136.172:91/api/token";
    private final static String ALL_BUILDINGS = "http://54.37.136.172:91/GetData/Buildings";
    private final static String SELECTED_BUILDING = "http://54.37.136.172:91/GetData/Buildings/";
    private final static String BUILDING_POINT = "http://54.37.136.172:91/GetData/Buildings/Points/";

    private Token token;
    private Device device;

    public static ConnectWebService GetInstance()
    {
        if(instance == null)
            instance = new ConnectWebService();

        return instance;
    }

    public ConnectWebService() {
    }

    private JSONArray GetRequest(String url) throws IOException, JSONException {
        if(token == null){
            getToken(device.getName(), device.getMacId());
        }

        URL address = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token.getToken());
        connection.setDoOutput(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JSONArray content = new JSONArray(br.readLine());
        connection.disconnect();

        return content;
    }

    private String PostRequest(String url, String json) throws IOException {
        URL address = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(json.getBytes());
        os.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String content = br.readLine();
        connection.disconnect();

        return content;

    }

    private void getToken(String deviceName, String deviceMac){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("NameDevice", deviceName);
            jsonObject.put("MacId", deviceMac);
            device = new Device(deviceName, deviceMac);
            String response = PostRequest(TOKEN, jsonObject.toString());
            token.setToken(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getSelectedBuilding(int buildingId){
        try {
            JSONArray response = GetRequest(SELECTED_BUILDING + buildingId);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getBuildingPoints(int buildingId){
        try {
            JSONArray response = GetRequest(BUILDING_POINT + buildingId);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
