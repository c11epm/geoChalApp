package se.umu.cs.c11epm.geochalapp.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.umu.cs.c11epm.geochalapp.R;

/**
 * AboutFragment
 *
 * Simple fragment that only presents the about information of the app.
 *
 * Created by emil on 2015-09-11.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }


}
