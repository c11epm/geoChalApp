package se.umu.cs.c11epm.geochalapp.view;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.umu.cs.c11epm.geochalapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengedOtherItemFragment extends Fragment {


    public ChallengedOtherItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_challenged_other_item, container, false);



        return v;
    }


}
