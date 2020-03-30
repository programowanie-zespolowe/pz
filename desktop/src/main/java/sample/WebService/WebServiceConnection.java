package sample.WebService;

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.methods.*;
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
    private final String AddBuilding = "http://54.37.136.172:90/admin/AddData/Buildings/{0}/{1}";
    private final String EditBuildingUrl = "http://54.37.136.172:90/admin/EditData/Buildings/{0}/{1}/{2}";
    private final String RemoveBuildingUrl = "http://54.37.136.172:90/admin/DeleteData/Buildings/{0}";

    private final String BuildingsLevelUrl = "http://54.37.136.172:90/admin/GetData/Buildings/{0}";
    private final String AddBuildingsLevelUrl = "http://54.37.136.172:90/admin/AddData/BuildingImage/{0}/{1}/{2}";
    private final String RemoveBuildingsLevelUrl = "http://54.37.136.172:90/admin/DeleteData/BuildingImages/{0}";
    private final String EditLevelUrl = "http://54.37.136.172:90/admin/EditData/Buildings/{0}/BuildingImages/{1}/{2}/{3}";

    private final String PointsUrl = "http://54.37.136.172:90/admin/GetData/Buildings/{0}/Points";
    private final String AddPointUrl = "http://54.37.136.172:90/admin/AddData/BuildingsImage/{0}/{1}/{2}/{3}/{4}/{5}/Points";
    private final String EditPointUrl = "http://54.37.136.172:90/admin/EditData/Buildings/{0}/BuildingImages/{1}/Points/{2}/{3}/{4}/{5}/{6}/{7}";
    private final String RemovePointUrl = "http://54.37.136.172:90/admin/DeleteData/Point/{0}";

    private final String GroupsUrl = "http://54.37.136.172:90/admin/GetData/Buildings/{0}/Groups";
    private final String AddGroupUrl = "http://54.37.136.172:90/admin/AddData/Groups/{0}/{1}";
    private final String RemoveGroupUrl = "http://54.37.136.172:90/admin/DeleteData/Groups/{0}";
    private final String EditGroupUrl = "http://54.37.136.172:90/admin/EditData/Groups/{0}/{1}/{2}";

    private final String PointDetailUrl = "http://54.37.136.172:90/admin/GetData/Buildings/Points/{0}/PointsDetails";
    private final String AddPointDetailUrl = "http://54.37.136.172:90/admin/AddData/Points/{0}/{1}/{2}/PointDetails";
    private final String RemovePointDetailUrl = "http://54.37.136.172:90/admin/DeleteData/PointsDetails/{0}";
    private final String EditPointDetailUrl = "http://54.37.136.172:90/admin/EditData/Points/{0}/PointsDetail/{1}/{2}/{3}";


    private final String PointsConnectionsUrl = "http://54.37.136.172:90/admin/GetData/Buildings/{0}/PointsConnection";
    private final String AddPointsConnectionUrl = "http://54.37.136.172:90/admin/AddData/Points/{0}/{1}/PointsConnection";
    private final String RemovePointConnectionUrl = "http://54.37.136.172:90/admin/DeleteData/PointsConnection/{0}";

    private final String AddPointType = "http://54.37.136.172:90/Admin/AddData/PointType";

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

    public Integer AddBuildingLevel(BuildingLevel buildingLevel, int buildingId, String filePath)
    {
        try{
            File file = new File(filePath);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .addBinaryBody("ImageRead", file, ContentType.create("application/octet-stream"), "filename")
                    .setMode(HttpMultipartMode.STRICT)
                    .build();

            String url = MessageFormat.format(AddBuildingsLevelUrl, buildingId, buildingLevel.getBuildingLevel(), buildingLevel.getNorthPointAngle());
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();

            BufferedReader br = new BufferedReader(new InputStreamReader(result.getContent()));
            String content = br.readLine();
            return Integer.parseInt(content);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Integer AddBuilding(String buildingName, double scale, String filePath)
    {
        try{
            File file = new File(filePath);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .addBinaryBody("ImageRead", file, ContentType.create("application/octet-stream"), "filename")
                    .setMode(HttpMultipartMode.STRICT)
                    .build();

            String url = MessageFormat.format(AddBuilding, buildingName.replace(" ", "_"), (int)scale);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();

            BufferedReader br = new BufferedReader(new InputStreamReader(result.getContent()));
            String content = br.readLine();
            return Integer.parseInt(content);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private boolean DeleteRequest(String address) throws IOException
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(address);
        httpDelete.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
        HttpResponse response = httpclient.execute(httpDelete);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

        //Throw runtime exception if status code isn't 200
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
        }
        return true;
    }

    public boolean RemoveBuildingLevel(int idBuilding, int idImage)
    {
        try{
            DeleteRequest(MessageFormat.format(RemoveBuildingsLevelUrl, idImage));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Point AddPoint(Point point, int buildingLevelId)
    {
        try{
//            File file = new File(filePath);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpEntity entity = MultipartEntityBuilder
                    .create()
//                    .addBinaryBody("ImageRead", file, ContentType.create("application/octet-stream"), "filename")
                    .setMode(HttpMultipartMode.STRICT)
                    .build();

            String url = MessageFormat.format(AddPointUrl, buildingLevelId, point.getX(), point.getY(), point.getIdPointType(), point.getDirection(), point.isOnOffDirection());
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();

            BufferedReader br = new BufferedReader(new InputStreamReader(result.getContent()));
            String content = br.readLine();
            Integer id = Integer.parseInt(content);
            if(id == null)
                return null;
            point.setIdPoint(id);
            return point;
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
    public PointsConnection[] PointsConnections(int buildingId)
    {
        try {
            String response = MakeGETRequest(MessageFormat.format(PointsConnectionsUrl, buildingId));
            return JSONConverter.ConvertToObject(response, PointsConnection[].class);
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

    public PointsConnection AddPointConnection(Point selectedPoint_1, Point selectedPoint_2) {
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .setMode(HttpMultipartMode.STRICT)
                    .build();

            String url = MessageFormat.format(AddPointsConnectionUrl, selectedPoint_1.getIdPoint(), selectedPoint_2.getIdPoint());
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();

            BufferedReader br = new BufferedReader(new InputStreamReader(result.getContent()));
            String content = br.readLine();
            Integer id = Integer.parseInt(content);
            if(id == null)
                return null;
            PointsConnection pointsConnection = new PointsConnection();
            pointsConnection.setIdPointConnection(id);
            pointsConnection.setIdPointStart(selectedPoint_1.getIdPoint());
            pointsConnection.setIdPointEnd(selectedPoint_2.getIdPoint());
            return pointsConnection;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Group AddGroup(Group group, String filePath, int idBuilding)
    {
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .setMode(HttpMultipartMode.STRICT)
                    .build();

            String url = MessageFormat.format(AddGroupUrl, group.getNameGroup().replace(" ", "_"), idBuilding);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();

            BufferedReader br = new BufferedReader(new InputStreamReader(result.getContent()));
            String content = br.readLine();
            group.setIdGroup(Integer.parseInt(content));
            return group;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public boolean EditGroup(Group group, String filePath, int idBuilding)
    {
        try{
            File file = new File(filePath);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            MultipartEntityBuilder builder = MultipartEntityBuilder
                    .create();
            HttpEntity entity = null;
            if(filePath.length() > 2)
                entity = builder
                        .addBinaryBody("ImageRead", file, ContentType.create("application/octet-stream"), "filename")
                        .setMode(HttpMultipartMode.STRICT)
                        .build();
            else
                entity = builder
                        .setMode(HttpMultipartMode.STRICT)
                        .build();

            String url = MessageFormat.format(EditGroupUrl, group.getIdGroup(), group.getNameGroup().replace(" ", "_"), idBuilding);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean RemoveGroup(Group group) {
        try{
            DeleteRequest(MessageFormat.format(RemoveGroupUrl, group.getIdGroup()));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public PointDetail AddPointDetail(PointDetail pointDetail)
    {
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .setMode(HttpMultipartMode.STRICT)
                    .build();

            String url = MessageFormat.format(AddPointDetailUrl, pointDetail.getIdPoint(),
                    pointDetail.getNamePoint().replace(" ", "_"),
                    pointDetail.getIdGroup());
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();

            BufferedReader br = new BufferedReader(new InputStreamReader(result.getContent()));
            String content = br.readLine();
            pointDetail.setIdPointDetails(Integer.parseInt(content));
            return pointDetail;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public boolean RemovePointDetail(PointDetail pointDetail) {
        try{
            DeleteRequest(MessageFormat.format(RemovePointDetailUrl, pointDetail.getIdPointDetails()));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public boolean RemovePoint(Point point) {
        try{
            DeleteRequest(MessageFormat.format(RemovePointUrl, point.getIdPoint()));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean EditPointDetails(PointDetail pointDetail, String filePath)
    {
        try{
            File file = new File(filePath);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            MultipartEntityBuilder builder = MultipartEntityBuilder
                    .create();
            HttpEntity entity = null;
            if(filePath.length() > 2)
                entity = builder
                    .addBinaryBody("ImageRead", file, ContentType.create("application/octet-stream"), "filename")
                    .setMode(HttpMultipartMode.STRICT)
                    .build();
            else
                entity = builder
                        .setMode(HttpMultipartMode.STRICT)
                        .build();

            String url = MessageFormat.format(EditPointDetailUrl, pointDetail.getIdPoint(),
                    pointDetail.getIdPointDetails(),
                    pointDetail.getNamePoint().replace(" ", "_"),
                    pointDetail.getIdGroup());
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean EditPoint(Point point, int idBuilding, int buildingLevelId) {
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .setMode(HttpMultipartMode.STRICT)
                    .build();

            String url = MessageFormat.format(EditPointUrl, idBuilding, buildingLevelId, point.getIdPoint(), (int)point.getX(), (int)point.getY(), point.getIdPointType(), point.getDirection(), point.isOnOffDirection());
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();

            BufferedReader br = new BufferedReader(new InputStreamReader(result.getContent()));
            String content = br.readLine();
            Integer id = Integer.parseInt(content);
            if(id == null)
                return false;
            point.setIdPoint(id);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean RemovePointConnection(PointsConnection connection) {
        try{
            DeleteRequest(MessageFormat.format(RemovePointConnectionUrl, connection.getIdPointConnection()));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean EditBuilding(Building building, String filePath) {
        try{
            File file = new File(filePath);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            MultipartEntityBuilder builder = MultipartEntityBuilder
                    .create();
            HttpEntity entity = null;
            if(filePath.length() > 2)
                entity = builder
                        .addBinaryBody("ImageRead", file, ContentType.create("application/octet-stream"), "filename")
                        .setMode(HttpMultipartMode.STRICT)
                        .build();
            else
                entity = builder
                        .setMode(HttpMultipartMode.STRICT)
                        .build();

            String url = MessageFormat.format(EditBuildingUrl, building.getIdBuilding(),
                    building.getNameBuilding().replace(" ", "_"),
                    building.getScale());
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean DeleteBuilding(int idBuilding) {
        try{
            DeleteRequest(MessageFormat.format(RemoveBuildingUrl, idBuilding));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean EditLevel(BuildingLevel level) {
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            MultipartEntityBuilder builder = MultipartEntityBuilder
                    .create();
            HttpEntity entity = builder
                        .setMode(HttpMultipartMode.STRICT)
                        .build();

            String url = MessageFormat.format(EditLevelUrl, level.getIdBuilding(),
                    level.getIdImage(),
                    level.getBuildingLevel(),
                    level.getNorthPointAngle());
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + tokenStruct.getToken());
            String name = entity.getContentType().getName();
            String value = entity.getContentType().getValue();
            httpPost.addHeader(name, value);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }
}
