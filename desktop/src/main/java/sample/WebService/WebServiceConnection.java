package sample.WebService;

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import sample.Structs.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebServiceConnection {

    private static WebServiceConnection instance;

    private final String LoginUrl = "http://54.37.136.172:90/admin/login";
    private final String BuildingsUrl = "http://54.37.136.172:90/admin/GetData/Buildings";
    private final String BuildingsLevelUrl = "http://54.37.136.172:90/admin/GetData/Buildings/{0}";
    private final String AddBuildingsLevelUrl = "http://54.37.136.172:90/admin/AddData/BuildingImage/{0}/{1}/{2}/{3}";
    private final String AddPointUrl = "http://54.37.136.172:90/admin/AddData/BuildingsImage/{0}/Points";
    private final String GroupsUrl = "http://54.37.136.172:90/admin/GetData/Buildings/{0}/Groups";
    private final String PointDetailUrl = "http://54.37.136.172:90/admin/GetData/Buildings/Points/{0}/PointsDetails";
    private final String PointsUrl = "http://54.37.136.172:90/admin/GetData/Buildings/{0}/Points";
    private final String AddPointType = "http://54.37.136.172:90/Admin/AddData/PointType";

//    private final String LoginUrl = "http://localhost:6000/admin/login";
//    private final String BuildingsUrl = "http://localhost:6000/admin/GetData/Buildings";
//    private final String BuildingsLevelUrl = "http://localhost:6000/admin/GetData/Buildings/{0}";
//    private final String AddBuildingsLevelUrl = "http://localhost:6000/admin/AddData/BuildingImage/{0}";
//    private final String AddPointUrl = "http://localhost:6000/admin/AddData/BuildingsImage/{0}/Points";
//    private final String GroupsUrl = "http://localhost:6000/admin/GetData/Buildings/{0}/Groups";
//    private final String PointDetailUrl = "http://localhost:6000/admin/GetData/Buildings/Points/{0}/PointsDetails";
//    private final String PointsUrl = "http://localhost:6000/admin/GetData/Buildings/{0}/Points";

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

    private String MakePOSTRequest(String stringUrl, String JSON, boolean authorized) throws IOException {
        if(authorized)
            CheckToken();
        URL address = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        if(authorized)
            connection.setRequestProperty("Authorization", "Bearer " + tokenStruct.getToken());
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(JSON.getBytes());
        os.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String content = br.readLine();
        connection.disconnect();

        return content;
    }
    public String UploadFile(String stringUrl, String JSON, byte[] fileContent) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // build httpentity object and assign the file that need to be uploaded
        HttpEntity postData = MultipartEntityBuilder.create()
                .addBinaryBody("pathImage", fileContent)
                .addPart("JSON", new StringBody(JSON, ContentType.APPLICATION_JSON))
                .build();

        // build http request and assign httpentity object to it that we build above
        HttpUriRequest postRequest = RequestBuilder
                .post(stringUrl)
                .addHeader("Authorization", "Bearer " + tokenStruct.getToken())
                .setEntity(postData).build();

        System.out.println("Executing request " + postRequest.getRequestLine());
        HttpResponse response = httpclient.execute(postRequest);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        //Throw runtime exception if status code isn't 200
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
        }

        //Create the StringBuffer object and store the response into it.
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = br.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
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
            String response = MakePOSTRequest(LoginUrl, JSON, false);
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
            String response = MakeGETRequest(MessageFormat.format(BuildingsLevelUrl, buildingId));
            return JSONConverter.ConvertToObject(response, BuildingLevel[].class);
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public int AddBuildingLevel(BuildingLevel buildingLevel, int buildingId, String filePath)
    {
        try{
            File file = new File(filePath);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .addBinaryBody("ImageRead", file, ContentType.create("application/octet-stream"), "filename")
                    .setMode(HttpMultipartMode.STRICT)
                    .build();

            String url = MessageFormat.format(AddBuildingsLevelUrl, buildingId, buildingLevel.getBuildingLevel(), buildingLevel.getScale(), buildingLevel.getNorthPointAngle());
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
//            httpPost.addHeader("Content-type", "multipart/form-data");
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();

            printPost(httpPost, entity);

            return 1;
        }
        catch (Exception e)
        {
            return 0;
        }
    }


    public Point AddPoint(Point point, int buildingLevelId)
    {
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost uploadFile = new HttpPost(MessageFormat.format(AddPointUrl, buildingLevelId));
            uploadFile.addHeader("Content-type", "multipart/form-data");
            uploadFile.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addTextBody("PointType", Integer.toString(point.getIdPointType()));
            builder.addTextBody("X", Double.toString(point.getX()));
            builder.addTextBody("Y", Double.toString(point.getY()));

            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(uploadFile);


            return JSONConverter.ConvertToObject(response.toString(), Point.class);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static void printPost(HttpPost httppost, HttpEntity entity) {
        try {
            Header[] headers = httppost.getAllHeaders();
            String content = EntityUtils.toString(entity);

            System.out.println(httppost.toString());
            for (Header header : headers) {
                System.out.println(header.getName() + ": " + header.getValue());
            }
            System.out.println();
            System.out.println(content);
        }
        catch (Exception e)
        {

        }
    }

    public Group[] Groups(int buildingId)
    {
        try {
            String response = MakeGETRequest(MessageFormat.format(GroupsUrl, buildingId));
            return JSONConverter.ConvertToObject(response, Group[].class);
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public Point[] Points(int buildingId)
    {
        try {
            String response = MakeGETRequest(MessageFormat.format(PointsUrl, buildingId));
            return JSONConverter.ConvertToObject(response, Point[].class);
        }
        catch (IOException e)
        {
            return null;
        }
    }
    public List<PointDetail> PointDetail(int pointId)
    {
        try {
            String response = MakeGETRequest(MessageFormat.format(PointDetailUrl, pointId));
            PointDetail[] details = JSONConverter.ConvertToObject(response, PointDetail[].class);
            return new ArrayList<>(Arrays.asList(details));
        }
        catch (IOException e)
        {
            return null;
        }
    }

}
