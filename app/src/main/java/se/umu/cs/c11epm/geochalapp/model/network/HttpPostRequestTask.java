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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by emil on 2015-08-23.
 * Params[0] = json with new user content.
 */
public class HttpPostRequestTask extends BaseTask {

    private String action;
    private Context context;

    public static String CREATE_USER_RESPONSE = "CREATE_USER_RESPONSE";

    public HttpPostRequestTask(Context context, String action) {
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
     *
     * @param params [0] holds the URI to connect to. [1] holds the JSON body to send.
     * @return JSON response
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        URL url = null;
        String baseURL = "http://geochal-1007.appspot.com/";
        try {
            url = new URL(baseURL + params[0]);
        } catch (MalformedURLException e) {
            return new JSONObject();
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
