package se.umu.cs.c11epm.geochalapp.model.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by emil on 2015-08-23.
 * Make sure to implement the onPostExecution when this class is created to be able to access
 * the GUI object in order to be able to update the GUI properly.
 */
public class HttpPostRequestTask extends BaseTask {

    public HttpPostRequestTask() {
    }

    /**
     * USAGE: To send HTTP POST request with a URI and a body and read a JSON response.
     * @param params [0] holds the URI to connect to. [1] holds the JSON body to send.
     * @return JSON response
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        String urlString = null;
        String baseURL = "http://geochal-1007.appspot.com";
        URL url = null;
        urlString = baseURL + params[0];
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        int statusCode = 0;
        JSONObject json = null;
        try {
            byte[] bytes = params[1].getBytes("UTF-8");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            //connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Length", "" + bytes.length);

            Log.d("HTTPPOST", "SEND TO: " + urlString + "\nDATA: " + params[1]);

            OutputStream os = new DataOutputStream(connection.getOutputStream());
            os.write(bytes);
            os.close();

            statusCode = connection.getResponseCode();
            Log.d("RESPONSE CODE", statusCode + "");
            InputStream is;

            //Success (HttpStatus == 200)
            if(statusCode == 200) {
                is = new BufferedInputStream(connection.getInputStream());
                json = parseData(getResponse(is));
            }
            else if (statusCode == 400) {
                is = new BufferedInputStream(connection.getErrorStream());
                String resp = getResponse(is);
                if(resp.length() == 0) {
                    json = new JSONObject();
                } else {
                    json = new JSONObject(resp);
                }

            } else {
                is = new BufferedInputStream(connection.getErrorStream());
                Log.d("HTTPPOST:::::", "Got some server error. " + statusCode + " " + getResponse(is));
            }

            if(is != null) {
                is.close();
            }


            if (json != null && !json.has("status")) {
                json.put("status", statusCode);
            }


        } catch (JSONException e) {
            //Status
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
