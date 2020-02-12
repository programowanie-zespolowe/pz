package sample.WebService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.OffsetDateTime;

public class WebServiceConnection {

    private static WebServiceConnection instance;

    private final String LoginUrl = "http://54.37.136.172:90/admin/login";

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

        Login(loginStruct.name, loginStruct.password);
    }

    private String MakeRequest(String stringUrl, String JSON) throws IOException {
        URL address = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection = (HttpURLConnection) address.openConnection();
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

    public boolean Login(String login, String password)
    {
        loginStruct = new LoginStruct(login, password);
        try {
            String JSON = JSONConverter.ConvertTOJson(loginStruct);
            String response = MakeRequest(LoginUrl, JSON);
            tokenStruct = JSONConverter.ConvertToObject(response, TokenStruct.class);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
