package se.umu.cs.c11epm.geochalapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.umu.cs.c11epm.geochalapp.R;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("GeoChal");
        } catch (NullPointerException e) {
            System.err.println("Hello, could not set title in action bar.");
        }
        return v;
    }



}
