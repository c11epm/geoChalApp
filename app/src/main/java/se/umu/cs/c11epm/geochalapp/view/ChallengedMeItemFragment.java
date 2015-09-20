package se.umu.cs.c11epm.geochalapp.view;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.awt.font.TextAttribute;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.Challenge;
import se.umu.cs.c11epm.geochalapp.model.Haversine;
import se.umu.cs.c11epm.geochalapp.model.Position;
import se.umu.cs.c11epm.geochalapp.model.network.HttpPostRequestTask;

/**
 * ChallengedMeItemFragment
 *
 * Presents a challenge where the logged in user is challenged.
 * Contains the buttons to finish the challenge, show challenge position on map
 * and force update the position from GPS.
 *
 * A simple {@link Fragment} subclass.
 */
public class ChallengedMeItemFragment extends Fragment {

    private MainActivity activity;
    private Challenge challenge;
    public ChallengedMeItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_challenged_me_item, container, false);

        activity = (MainActivity) getActivity();

        TextView challengedBy = (TextView) v.findViewById(R.id.challenge_item_challenged_by);
        TextView chalLong = (TextView) v.findViewById(R.id.challenge_long);
        TextView chalLat = (TextView) v.findViewById(R.id.challenge_lat);
        final TextView challengeDistance = (TextView) v.findViewById(R.id.challenge_me_distance);
        final TextView myLong = (TextView) v.findViewById(R.id.my_long);
        final TextView myLat = (TextView) v.findViewById(R.id.my_lat);

        Button finish = (Button) v.findViewById(R.id.finish_challenge);
        Button update = (Button) v.findViewById(R.id.update_my_pos);
        Button show = (Button) v.findViewById(R.id.show_on_map);

        if(challenge.isFinished()) {
            finish.setEnabled(false);
        }

        challengedBy.setText(getString(R.string.challenge_item_challenged) + " " + challenge.getCreatorUser());
        chalLong.setText(getString(R.string.challenge_item_long) + " " + challenge.getLongitude());
        chalLat.setText(getString(R.string.challenge_item_lat) + " " + challenge.getLatitude());

        if(activity.getGPS().gotPosition()) {
            myLong.setText(getString(R.string.challenge_me_long) + " " + activity.getPosition().getLongitude() + "");
            myLat.setText(getString(R.string.challenge_me_lat) + " " + activity.getPosition().getLatitude() + "");

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
            challengeDistance.setText(getString(R.string.no_position));
        }

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.getGPS().gotPosition()) {
                    activity.showMap(challenge.getPosition(), activity.getPosition());
                } else {
                    Toast.makeText(activity, R.string.location_error, Toast.LENGTH_SHORT).show();
                }

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "/challenge/" + challenge.getID();
                if(!activity.getGPS().gotPosition()) {
                    Toast.makeText(activity, getString(R.string.location_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject json = new JSONObject();
                try {
                    json.put("longitude", activity.getPosition().getLongitude());
                    json.put("latitude", activity.getPosition().getLatitude());

                    new HttpPostRequestTask() {
                        @Override
                        protected void onPostExecute(JSONObject jsonObject) {
                            try {
                                Toast.makeText(activity, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(path, json.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.getGPS().gotPosition()) {
                    myLong.setText(getString(R.string.challenge_me_long) + " " + activity.getPosition().getLongitude() + "");
                    myLat.setText(getString(R.string.challenge_me_lat) + " " + activity.getPosition().getLatitude() + "");

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
                    Toast.makeText(activity, getString(R.string.location_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }


    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Challenge getChallenge() {
        return challenge;
    }
}
