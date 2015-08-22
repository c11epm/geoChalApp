package se.umu.cs.c11epm.geochalapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.network.GetUserTask;

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
        TextView t = (TextView) v.findViewById(R.id.textview);


        Log.d("CALL", "Before send!");
        GetUserTask task = new GetUserTask(getActivity(), "SOME UNIQUE KEY");
        task.execute("emil");
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

        return v;
    }
}
