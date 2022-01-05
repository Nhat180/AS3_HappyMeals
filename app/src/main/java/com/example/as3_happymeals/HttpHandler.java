package com.example.as3_happymeals;

import com.example.as3_happymeals.model.Site;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {

    public static String getRequest(String urlStr){
        StringBuilder builder = new StringBuilder();
        try {
            // Step 1: Connect to the web server
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)
                    url.openConnection();

            // Step 2: Read the input from the server
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            String line="";
            while ((line = reader.readLine())!=null){
                builder.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();

    }

    public static String postRequest(String urlStr, Site site){
        String status="";
        try {
            //Step 1 - prepare the connection
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept", "application/json");

            //Step 2 - prepare the JSON object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", site.getLeaderUid());
            jsonObject.put("name", site.getName());
            jsonObject.put("leaderName", site.getLeaderName());
            jsonObject.put("latitude", site.getLatitude());
            jsonObject.put("longitude", site.getLongitude());
            jsonObject.put("totalPeople", site.getTotalPeople());
            jsonObject.put("totalComment", site.getTotalComment());

            //Step 3 - Writing data to the web server
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());
            os.flush();
            os.close();
            status = conn.getResponseCode() + ": " + conn.getResponseMessage();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static String putRequest(String urlStr, Site site) {
        String status = "";
        try {
            //Step 1 - prepare the connection
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            //Step 2 - prepare the JSON object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", site.getName());
            jsonObject.put("leaderName", site.getLeaderName());
            jsonObject.put("latitude", site.getLatitude());
            jsonObject.put("longitude", site.getLongitude());
            jsonObject.put("totalPeople", site.getTotalPeople());
            jsonObject.put("totalComment", site.getTotalComment());

            //Step 3 - Writing data to webservice
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());
            os.flush();
            os.close();
            status = conn.getResponseCode() + ": " + conn.getResponseMessage();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static String deleteRequest (String urlStr) {
        URL url = null;
        String status = "";
        try {
            url = new URL(urlStr);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded" );
            httpCon.setRequestMethod("DELETE");
            httpCon.connect();
            status = httpCon.getResponseCode() + ": " + httpCon.getResponseMessage();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }


}
