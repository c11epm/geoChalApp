package se.umu.cs.c11epm.geochalapp.model.network;

import android.content.Context;
import android.content.Intent;

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
 * Params[0] = json with new user content.
 */
public class HttpPostRequestTask extends BaseTask {

    public HttpPostRequestTask() {
    }

    /**
     *
     * @param params [0] holds the URI to connect to. [1] holds the JSON body to send.
     * @return JSON response
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        String urlString = null;
        String baseURL = "http://geochal-1007.appspot.com";
        URL url = null;
        try {
            url = new URL(baseURL + params[0]);
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

            OutputStream os = new DataOutputStream(connection.getOutputStream());
            os.write(bytes);
            os.close();

            statusCode = connection.getResponseCode();
            InputStream is = new BufferedInputStream(connection.getInputStream());

            //Success (HttpStatus == 200)
            if(statusCode == 200) {
                json = parseData(getResponse(is));
            }
            else {
                json = new JSONObject(getResponse(is));
            }

            if(is != null) {
                is.close();
            }

            if (!json.has("status")) {
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
