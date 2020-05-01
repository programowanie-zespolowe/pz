package com.example.programowaniezespolowe.Connection;

import com.example.programowaniezespolowe.Data.Device;
import com.example.programowaniezespolowe.Data.PointPath;
import com.example.programowaniezespolowe.Data.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Date;

public class ConnectWebService {
    private static ConnectWebService instance;
    private final String TOKEN = "http://54.37.136.172:91/api/token";
    private final String SELECTED_BUILDING =  "http://54.37.136.172:91/GetData/Buildings/Selected/{0}";
    private final String GROUPS = "http://54.37.136.172:91/GetData/Buildings/{0}/Groups";
    private final String POINT_DETAIL = "http://54.37.136.172:91/GetData/Buildings/Groups/{0}/PointsDetails";
    private final String BUILDING_POINT = "http://54.37.136.172:91/GetData/Buildings/{0}/Points";
    private final String NEXT_POINT = "http://54.37.136.172:91/GetData/Buildings/{0}/{1}/{2}/{3}";

    private final String OUTDOOR_GAME = "http://54.37.136.172:91/GetData/Buildings/OutdoorGame/{0}";
    private final String OUTDOOR_GAME_POINT =  "http://54.37.136.172:91/GetData/Buildings/OutdoorGame/Game/{0}/{1}";
    private final String OUTDOOR_GAME_HINT =  "http://54.37.136.172:91/GetData/Buildings/OutdoorGame/Hint/{0}/{1}";
    private final String OUTDOOR_TIME_GAME = "http://54.37.136.172:91/GetData/Buildings/OutdoorGame/RecordTime/{0}/{1}/{2}/{3}";

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
    private JSONObject GetRequest2(String url) throws IOException, JSONException {
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
        JSONObject content = new JSONObject(br.readLine());
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
    private String PostRequest(String url) throws IOException, JSONException {
        URL address = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.setRequestMethod("POST");
        //connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + token.getToken());
        connection.setDoOutput(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String  content = br.readLine();
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

    public String getOutdoorTime(int idGame, String name, String mac, boolean start){
        String newUrl = MessageFormat.format(OUTDOOR_TIME_GAME, idGame, name, mac, start);
        try {
            String jsonObject = PostRequest(newUrl);
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
            JSONArray response = GetRequest( MessageFormat.format(GROUPS, buildingId));
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
            JSONArray response = GetRequest(MessageFormat.format( POINT_DETAIL, buildingId));
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    //    Admin/GetData/Buildings/{idBuilding}/{idPrevPoint}/{idActualPoint}/{idDestPoint}
    public JSONObject getNextPoint(int idBuilding){
        PointPath p = PointPath.getInstance();
        try {
            JSONObject response = GetRequest2(MessageFormat.format(NEXT_POINT, idBuilding, p.getPreviousPoint(), p.getCurrentPoint(), p.getTargetPoint()));
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getOutdoorGame(int idbuilding){
        try {
            JSONArray response = GetRequest(MessageFormat.format( OUTDOOR_GAME, idbuilding));
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getFirstGamePoint(int idGame, int idNextPoint){
        try {
            JSONArray response = GetRequest(MessageFormat.format(OUTDOOR_GAME_POINT, idGame, idNextPoint));
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getHintGameP(int idGame, int idHintPoint){
        try {
            JSONArray response = GetRequest(MessageFormat.format(OUTDOOR_GAME_HINT, idGame, idHintPoint));
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
