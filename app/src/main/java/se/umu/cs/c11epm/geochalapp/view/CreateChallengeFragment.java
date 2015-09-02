package se.umu.cs.c11epm.geochalapp.view;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.network.HttpPostRequestTask;

public class CreateChallengeFragment extends Fragment {
    private MainActivity activity;

    public CreateChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_challenge, container, false);

        activity = (MainActivity) getActivity();
        final EditText challengeUser = (EditText) v.findViewById(R.id.challengeUser);

        Button createChallenge = (Button) v.findViewById(R.id.mainCreateChallengeButton);
        createChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location loc = activity.getPosition();
                if (loc == null) {
                    Toast.makeText(activity, R.string.location_error, Toast.LENGTH_SHORT).show();
                    return;
                }


                Log.d("ON CLICK", challengeUser.getText().toString());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("creatorUser", activity.getUserInfo().getUsername());
                    jsonObject.put("challengedUser", challengeUser.getText().toString());
                    jsonObject.put("longitude", loc.getLongitude());
                    jsonObject.put("latitude", loc.getLatitude());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new HttpPostRequestTask(){
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {
                        try {
                            //if(jsonObject.get("status") == 200) {
                                Toast.makeText(activity, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                            //}
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute("/challenge", jsonObject.toString());
            }
        });

        return v;
    }


}
