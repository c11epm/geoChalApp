package se.umu.cs.c11epm.geochalapp.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.Challenge;
import se.umu.cs.c11epm.geochalapp.model.network.HttpGetRequestTask;

public class ChallengeListFragment extends Fragment {

    private MainActivity activity;
    private ListView challengeList;
    private MainActivity.list type;

    private List<Challenge> challenges;

    public ChallengeListFragment() {
        // Required empty public constructor
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
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
        challengeList.setAdapter(new ChallengeMeAdapter(activity, R.layout.challenge_list_item, challenges));

        /*if(type.equals(MainActivity.list.ME)) {
            //TODO set item clicker
        } else {
            //TODO set list item clicker to change fragment view.
        }*/

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
                ArrayAdapter<Challenge> adapter = (ArrayAdapter<Challenge>)challengeList.getAdapter();
                adapter.notifyDataSetChanged();
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
            ImageButton icon = (ImageButton) convertView.findViewById(R.id.challenge_item_imagebutton);

            challengedBy.setText(getString(R.string.challenge_item_challenged) + " " + c.getCreatorUser());
            icon.setImageResource(R.drawable.notdone);
            distance.setText(getString(R.string.challenge_item_distance) + " ");

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
            ImageButton icon = (ImageButton) convertView.findViewById(R.id.challenge_item_imagebutton);

            challengedBy.setText(getString(R.string.challenge_item_challenged_other) + " " + c.getChallengedUser());
            //icon.setImageResource(R.drawable.notdone);
            distance.setText(getString(R.string.challenge_item_position) +
                    " lat:" + c.getLatitude() + " lon:" + c.getLongitude());

            return convertView;

        }
    }

}
