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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by emil on 2015-08-23.
 */
public class HttpGetRequestTask extends BaseTask {

    private String action;
    private Context context;

    public static String CREATE_USER_RESPONSE = "HTTP_RESPONSE";

    public HttpGetRequestTask(Context context, String action) {
        this.context = context;
        this.action = action;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        Intent intent = new Intent(action);
        intent.putExtra(CREATE_USER_RESPONSE, s.toString());
        context.sendBroadcast(intent);
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

            InputStream is = new BufferedInputStream(connection.getInputStream());

            statusCode = connection.getResponseCode();
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


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
