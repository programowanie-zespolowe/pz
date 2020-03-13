package com.example.programowaniezespolowe.Connection;

import com.example.programowaniezespolowe.Data.Device;
import com.example.programowaniezespolowe.Data.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

public class ConnectWebService {
    private static ConnectWebService instance;
    private final static String TOKEN = "http://54.37.136.172:91/api/token";
    private final static String SELECTED_BUILDING =  "http://54.37.136.172:91/GetData/Buildings/All/{0}";
    private final String Groups = "http://54.37.136.172:91/GetData/Buildings/{0}/Groups";
    private final String PointDetail = "http://54.37.136.172:91/GetData/Buildings/Points/{0}/PointsDetails";
    private final String BUILDING_POINT = "http://54.37.136.172:91/GetData/Buildings/{0}/Points";

    private Token token;
    private static Device device;

    public void setDevice(Device device) {
        this.device = device;
    }

    public static ConnectWebService GetInstance()
    {
        if(instance == null)
            instance = new ConnectWebService();

        return instance;
    }

    public ConnectWebService() {
        token = new Token();
    }

    private JSONArray GetRequest(String url) throws IOException, JSONException {
        if(token.getToken() == null){
            getToken(device.getName(), device.getMacId());
            //getToken("Test", "00:00:00:00:00:00");
        }

        URL address = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token.getToken());
        connection.setDoInput(true);
        connection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JSONArray content = new JSONArray(br.readLine());
        connection.disconnect();

        return content;
    }

    private JSONObject PostRequest(String url, String json) throws IOException, JSONException {
        URL address = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(json.getBytes());
        os.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JSONObject content = new JSONObject( br.readLine());
        connection.disconnect();

        return content;

    }

    private void getToken(String deviceName, String deviceMac){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("NameDevice", deviceName);
            jsonObject.put("MacId", deviceMac);
            device = new Device(deviceName, deviceMac);
            JSONObject response = PostRequest(TOKEN, jsonObject.toString());
            token.setToken(response.getString("token"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getSelectedBuilding(int buildingId){
        try {
           JSONArray response = GetRequest(MessageFormat.format(SELECTED_BUILDING, buildingId));
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
            JSONArray response = GetRequest(MessageFormat.format( BUILDING_POINT, buildingId));
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    public JSONArray getGroups(int buildingId){
        try {
            JSONArray response = GetRequest( MessageFormat.format(Groups, buildingId));
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getPointDetail(int buildingId){
        try {
            JSONArray response = GetRequest(MessageFormat.format( PointDetail, buildingId));
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
