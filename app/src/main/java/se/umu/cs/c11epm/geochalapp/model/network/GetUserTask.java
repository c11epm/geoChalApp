package se.umu.cs.c11epm.geochalapp.model.network;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by emil on 2015-08-22.
 */
public class GetUserTask extends BaseTask {
    private Context context;
    private String action;

    public static String USER_RESPONSE = "USER_RESPONSE";

    public GetUserTask(Context context, String action) {
        this.context = context;
        this.action = action;
    }


    @Override
    protected JSONObject doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL("http://geochal-1007.appspot.com/user/" + params[0]);
        } catch (MalformedURLException e) {
            return new JSONObject();
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

    @Override
    protected void onPostExecute(JSONObject s) {
        Intent intent = new Intent(action);
        intent.putExtra(USER_RESPONSE, s.toString());

        context.sendBroadcast(intent);
    }
}
