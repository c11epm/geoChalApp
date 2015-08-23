package se.umu.cs.c11epm.geochalapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.network.HttpPostRequestTask;
import se.umu.cs.c11epm.geochalapp.model.network.HttpGetRequestTask;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_main, container, false);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("GeoChal OLOL");
        } catch (NullPointerException e) {
            System.err.println("Hello, could not set title in action bar.");
        }
        final TextView t = (TextView) v.findViewById(R.id.textview);


        Log.d("CALL", "Before send!");
        HttpGetRequestTask task = new HttpGetRequestTask(getActivity(), "SOME UNIQUE KEY");
        JSONObject j = new JSONObject();
        try {
            j.put("username", "emil");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        task.execute("/user/emil");
        try {
            JSONObject json = task.get(15000, TimeUnit.MILLISECONDS);

            t.setText(json.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        final EditText username = (EditText) v.findViewById(R.id.username);
        final EditText password = (EditText) v.findViewById(R.id.password);
        Button btn = (Button) v.findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                String pword = password.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", uname);
                    jsonObject.put("password", pword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpPostRequestTask task = new HttpPostRequestTask(getActivity(), "CREATE NEW USER");
                try {
                    task.execute("/user", jsonObject.toString());
                    JSONObject json = task.get(15000, TimeUnit.MILLISECONDS);
                    t.setText(json.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }

            }
        });



        return v;
    }
}
