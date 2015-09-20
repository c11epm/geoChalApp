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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import se.umu.cs.c11epm.geochalapp.view.MainActivity;

/**
 * HttpGetRequestTask
 * Implements the HTTP GET request in a ASyncTask to use to contact web server.
 * Used to contact the GeoChal web service.
 *
 * The doPostExecute method needs to be implemented where the class is used to get access to
 * GUI object and other model object to set the collected data.
 * Created by emil on 2015-08-23.
 */
public class HttpGetRequestTask extends BaseTask {

    public HttpGetRequestTask() {
    }

    /**
     * USAGE:   params[0] URI ex. /user/id
     * @param params containing the URI
     * @return JSON response
     */
    @Override
    protected JSONObject doInBackground(String... params) {

        String baseURL ="http://geochal-1007.appspot.com";

        URL url = null;
        try {
            url = new URL(baseURL + params[0]);
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL not formed correctly");
        }
        int statusCode = 0;
        JSONObject json = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            //connection.setRequestProperty("Accept", "application/json");
            Log.d("SENT TO:::", baseURL+params[0]);
            InputStream is;

            statusCode = connection.getResponseCode();

            //Success (HttpStatus == 200)
            if(statusCode == 200) {
                is = new BufferedInputStream(connection.getInputStream());
                json = parseData(getResponse(is));

            }
            else {
                is = new BufferedInputStream(connection.getErrorStream());
                json = new JSONObject(getResponse(is));
            }

            if(is != null) {
                is.close();
            }

            if (!json.has("status")) {
                json.put("status", statusCode);
            }
            Log.d("RESPONSE::::", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
