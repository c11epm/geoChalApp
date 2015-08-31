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
import se.umu.cs.c11epm.geochalapp.model.UserInfo;
import se.umu.cs.c11epm.geochalapp.model.network.HttpPostRequestTask;
import se.umu.cs.c11epm.geochalapp.model.network.HttpGetRequestTask;

public class MainFragment extends Fragment {

    private UserInfo userInfo;
    private MainActivity activity;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_main, container, false);
        activity = (MainActivity) getActivity();
        try {
            activity.getSupportActionBar().setTitle("GeoChal OLOL");
        } catch (NullPointerException e) {
            System.err.println("Hello, could not set title in action bar.");
        }
        TextView t = (TextView) v.findViewById(R.id.mainText);
        userInfo = activity.getUserInfo();
        t.setText("Hello misterinos!" + userInfo.getUsername() + " " + userInfo.getToken());
        return v;
    }
}
