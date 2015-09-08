package se.umu.cs.c11epm.geochalapp.view;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.Challenge;
import se.umu.cs.c11epm.geochalapp.model.Haversine;
import se.umu.cs.c11epm.geochalapp.model.Position;
import se.umu.cs.c11epm.geochalapp.model.network.HttpGetRequestTask;

public class ChallengeListFragment extends Fragment {

    private MainActivity activity;
    private ListView challengeList;
    private MainActivity.list type;

    private List<Challenge> challenges;

    public ChallengeListFragment() {
        // Required empty public constructor
    }

    public void setType(MainActivity.list type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_challenge_list, container, false);

        activity = (MainActivity) getActivity();
        challengeList = (ListView) v.findViewById(R.id.challengeList);
        fillList();




        if(type.equals(MainActivity.list.ME)) {
            challengeList.setAdapter(new ChallengeMeAdapter(activity, R.layout.challenge_list_item, challenges));
            challengeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Challenge c = (Challenge) challengeList.getAdapter().getItem(position);

                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ChallengedMeItemFragment cmf = new ChallengedMeItemFragment();
                    cmf.setChallenge(c);

                    ft.replace(R.id.mainActivity, cmf);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });
        } else {
            challengeList.setAdapter(new ChallengeOtherAdapter(activity, R.layout.challenge_list_item, challenges));
            challengeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Challenge c = (Challenge) challengeList.getAdapter().getItem(position);

                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ChallengedOtherItemFragment cof = new ChallengedOtherItemFragment();
                    cof.setChallenge(c);

                    ft.replace(R.id.mainActivity, cof);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });
        }


        return v;
    }

    private void fillList() {
        String path = type.equals(MainActivity.list.ME) ?
                "/challenge/challenged/" + activity.getUserInfo().getUsername() :
                "/challenge/creator/" + activity.getUserInfo().getUsername();

        challenges = new LinkedList<>();

        new HttpGetRequestTask() {
            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    JSONArray arr = jsonObject.getJSONArray("challenges");

                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            JSONObject json = (JSONObject) arr.get(i);
                            Challenge challenge = new Challenge();

                            challenge.setChallengedUser((String) json.get("challengedUser"));
                            challenge.setCreatorUser((String) json.get("creatorUser"));
                            challenge.setID((String) json.get("id"));
                            challenge.setLongitude((Double) json.get("longitude"));
                            challenge.setLatitude((Double) json.get("latitude"));
                            long k = (int) json.get("finished");
                            challenge.setFinished(k);


                            challenges.add(challenge);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("ERROR IN JSON PARSE::::", e.getMessage());
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("CHALLENGE:JSON", e.getMessage());
                }

                ListAdapter adapter = challengeList.getAdapter();
                ((ArrayAdapter<Challenge>)adapter).notifyDataSetChanged();

            }
        }.execute(path);
    }

    private class ChallengeMeAdapter extends ArrayAdapter<Challenge> {

        public ChallengeMeAdapter(Context context, int resource, List<Challenge> challenges) {
            super(context, resource, challenges);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.challenge_list_item, null);
            }
            Challenge c = getItem(position);

            TextView challengedBy = (TextView) convertView.findViewById(R.id.challenge_item_user);
            TextView distance = (TextView) convertView.findViewById(R.id.challenge_item_distance);
            ImageView icon = (ImageView) convertView.findViewById(R.id.challenge_item_icon);

            challengedBy.setText(getString(R.string.challenge_item_challenged) + " " + c.getCreatorUser());
            icon.setImageResource(c.isFinished() ? R.drawable.done : R.drawable.notdone);
            if(activity.getGPS().gotPosition()) {
                Position myPos = new Position();
                Location loc = activity.getPosition();
                myPos.setLatitude(loc.getLatitude());
                myPos.setLongitude(loc.getLongitude());

                double d = Haversine.haversine(c.getPosition(), myPos);

                String length;
                if(d < 1) {
                    d *= 1000;
                    length = ((int) d) + " m";
                } else {
                    d = (int) (d * 100);
                    d = d / 100;
                    length = d + " km";
                }
                distance.setText(getString(R.string.challenge_item_distance) + " " + length);
            } else {
                distance.setText(getString(R.string.location_error));
            }


            return convertView;
        }
    }

    private class ChallengeOtherAdapter extends ArrayAdapter<Challenge> {

        public ChallengeOtherAdapter(Context context, int resource, List<Challenge> challenges) {
            super(context, resource, challenges);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.challenge_list_item, null);
            }
            Challenge c = getItem(position);

            TextView challengedBy = (TextView) convertView.findViewById(R.id.challenge_item_user);
            TextView distance = (TextView) convertView.findViewById(R.id.challenge_item_distance);
            ImageView icon = (ImageView) convertView.findViewById(R.id.challenge_item_icon);

            challengedBy.setText(getString(R.string.challenge_item_challenged_other) + " " + c.getChallengedUser());
            icon.setImageResource(c.isFinished() ? R.drawable.done : R.drawable.notdone);

            if(activity.getGPS().gotPosition()) {
                Position myPos = new Position();
                Location loc = activity.getPosition();
                myPos.setLatitude(loc.getLatitude());
                myPos.setLongitude(loc.getLongitude());

                double d = Haversine.haversine(c.getPosition(), myPos);

                String length;
                if(d < 1) {
                    d *= 1000;
                    length = ((int) d) + " m";
                } else {
                    d = (int) (d * 100);
                    d = d / 100;
                    length = d + " km";
                }
                distance.setText(getString(R.string.challenge_item_distance) + " " + length);
            } else {
                distance.setText(getString(R.string.location_error));
            }

            return convertView;
        }
    }
}
