package se.umu.cs.c11epm.geochalapp.view;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.Challenge;
import se.umu.cs.c11epm.geochalapp.model.Haversine;
import se.umu.cs.c11epm.geochalapp.model.Position;
import se.umu.cs.c11epm.geochalapp.model.network.HttpGetRequestTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengedOtherItemFragment extends Fragment {

    private MainActivity activity;
    private Challenge challenge;

    public ChallengedOtherItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_challenged_other_item, container, false);

        activity = (MainActivity) getActivity();

        final TextView challengedUser = (TextView) v.findViewById(R.id.challenge_other_username);
        TextView challengeToken = (TextView) v.findViewById(R.id.challenge_other_token);
        TextView challengeLat = (TextView) v.findViewById(R.id.challenge_other_lat);
        TextView challengeLong = (TextView) v.findViewById(R.id.challenge_other_long);
        TextView challengeDistance = (TextView) v.findViewById(R.id.challenge_other_distance);

        Button updateChallenge = (Button) v.findViewById(R.id.challenge_other_update);

        final ImageView challengeStatus = (ImageView) v.findViewById(R.id.challenge_status);

        challengedUser.setText(getString(R.string.challenge_item_challenged_other) + " " + challenge.getChallengedUser());
        challengeToken.setText(getString(R.string.challenge_item_id) + " " + challenge.getID());
        challengeLat.setText(getString(R.string.challenge_item_lat) + " " + challenge.getLatitude());
        challengeLong.setText(getString(R.string.challenge_item_long) + " " + challenge.getLongitude());

        if(activity.getGPS().gotPosition()) {
            Position myPos = new Position();
            Location loc = activity.getPosition();
            myPos.setLatitude(loc.getLatitude());
            myPos.setLongitude(loc.getLongitude());

            double d = Haversine.haversine(challenge.getPosition(), myPos);

            String length;
            if(d < 1) {
                d *= 1000;
                length = ((int) d) + " m";
            } else {
                d = (int) (d * 100);
                d = d / 100;
                length = d + " km";
            }
            challengeDistance.setText(getString(R.string.challenge_item_distance) + " " + length);
        } else {
            challengeDistance.setText(getString(R.string.location_error));
        }

        updateStatusIcon(challengeStatus);

        updateChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpGetRequestTask() {
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {
                        try {
                            if(jsonObject.get("status").equals(200)) {
                                challenge = new Challenge();
                                challenge.setCreatorUser((String) jsonObject.get("creatorUser"));
                                challenge.setChallengedUser((String) jsonObject.get("challengedUser"));
                                challenge.setID((String) jsonObject.get("id"));
                                challenge.setLongitude((Double) jsonObject.get("longitude"));
                                challenge.setLatitude((Double) jsonObject.get("latitude"));
                                challenge.setFinished((long) (Integer) jsonObject.get("finished"));
                                updateStatusIcon(challengeStatus);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute("/challenge/" + challenge.getID());


            }
        });

        return v;
    }

    private void updateStatusIcon(ImageView v) {
        v.setImageResource(challenge.isFinished() ? R.drawable.done : R.drawable.notdone);
    }


    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
