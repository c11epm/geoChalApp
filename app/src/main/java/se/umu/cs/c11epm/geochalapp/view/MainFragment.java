package se.umu.cs.c11epm.geochalapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.UserInfo;

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
        activity.startGPS();
        try {
            userInfo = activity.getUserInfo();

            ActionBar ab = activity.getSupportActionBar();

            ab.setTitle("GeoChal - " + userInfo.getUsername());
        } catch (NullPointerException e) {
            System.err.println("Hello, could not set title in action bar.");
        }

        Button challengeMeList = (Button) v.findViewById(R.id.mainChallengedMeButton);
        Button challengeOtherList = (Button) v.findViewById(R.id.mainChallengedOtherButton);
        Button createChallenge = (Button) v.findViewById(R.id.mainCreateChallengeButton);
        Button logoutButton = (Button) v.findViewById(R.id.mainLogout);

        challengeMeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeView(MainActivity.views.CHALLENGELIST, MainActivity.list.OTHER);
            }
        });

        challengeOtherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeView(MainActivity.views.CHALLENGELIST, MainActivity.list.ME);
            }
        });

        createChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeView(MainActivity.views.CREATECHALLENGE, MainActivity.list.NONE);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.logout();
                activity.changeView(MainActivity.views.LOGIN, MainActivity.list.NONE);
            }
        });

        return v;
    }
}
