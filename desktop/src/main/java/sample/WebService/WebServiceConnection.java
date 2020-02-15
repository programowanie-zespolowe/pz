package sample.WebService;

import sample.Structs.Building;
import sample.Structs.BuildingLevel;
import sample.Structs.LoginStruct;
import sample.Structs.TokenStruct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;

public class WebServiceConnection {

    private static WebServiceConnection instance;

    private final String LoginUrl = "http://54.37.136.172:90/admin/login";
    private final String BuildingsUrl = "http://54.37.136.172:90/admin/GetData/Buildings";
    private final String BuildingsLevelUrl = "http://54.37.136.172:90/admin/GetData/Buildings/";

    private TokenStruct tokenStruct;
    private LoginStruct loginStruct;

    public static WebServiceConnection GetInstance()
    {
        if(instance == null)
            instance = new WebServiceConnection();

        return instance;
    }

    private WebServiceConnection()
    {

    }

    private void CheckToken()
    {
        if(tokenStruct != null && tokenStruct.getExpiration().compareTo(OffsetDateTime.now()) <= 0)
            return;
        if(loginStruct == null)
            return;

        Login(loginStruct.getName(), loginStruct.getPassword());
    }

    private String MakePOSTRequest(String stringUrl, String JSON) throws IOException {
        URL address = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(JSON.getBytes());
        os.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String content = br.readLine();
        connection.disconnect();

        return content;
    }

    private String MakeGETRequest(String stringUrl) throws IOException {
        CheckToken();

        URL address = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + tokenStruct.getToken());
        connection.setDoOutput(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String content = br.readLine();
        connection.disconnect();

        return content;
    }


    public boolean Login(String login, String password)
    {
        loginStruct = new LoginStruct(login, password);
        try {
            String JSON = JSONConverter.ConvertTOJson(loginStruct);
            String response = MakePOSTRequest(LoginUrl, JSON);
            tokenStruct = JSONConverter.ConvertToObject(response, TokenStruct.class);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public Building[] BuildingList()
    {
        try {
            String response = MakeGETRequest(BuildingsUrl);
            return JSONConverter.ConvertToObject(response, Building[].class);
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public BuildingLevel[] BuildingLevelList(int buildingId)
    {
        try {
            String response = MakeGETRequest(BuildingsLevelUrl + Integer.toString(buildingId));
            return JSONConverter.ConvertToObject(response, BuildingLevel[].class);
        }
        catch (IOException e)
        {
            return null;
        }
    }

}
