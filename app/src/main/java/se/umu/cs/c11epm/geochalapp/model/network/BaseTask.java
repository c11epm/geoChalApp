package se.umu.cs.c11epm.geochalapp.model.network;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by emil on 2015-08-22.
 */
public abstract class BaseTask extends AsyncTask<String, Void, JSONObject> {

    protected String getResponse(InputStream is) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();

        String line = "";

        while((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }

        is.close();

        return builder.toString();

    }

    protected JSONObject parseData(String jsonString) throws JSONException {
        return new JSONObject(jsonString);
    }
}
