package se.umu.cs.c11epm.geochalapp.view;


import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.UserInfo;
import se.umu.cs.c11epm.geochalapp.model.network.HttpGetRequestTask;
import se.umu.cs.c11epm.geochalapp.model.network.HttpPostRequestTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {

    private MainActivity activity;
    private UserInfo user;

    public UserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_info_view, container, false);

        activity = (MainActivity) getActivity();
        user = activity.getUserInfo();

        final TextView username = (TextView) v.findViewById(R.id.user_view_name);
        final TextView points = (TextView) v.findViewById(R.id.user_view_points);
        final TextView friends = (TextView) v.findViewById(R.id.user_view_friends);
        Button addFriend = (Button) v.findViewById(R.id.add_friend);

        new HttpGetRequestTask() {
            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    if(jsonObject.get("status").equals(200)) {
                        username.setText(getString(R.string.username) + ": " + jsonObject.get("username").toString());
                        points.setText(getString(R.string.points) + ": " + jsonObject.get("points").toString());

                        String friendsString = "";

                        JSONArray arr = jsonObject.getJSONArray("friends");
                        for(int i = 0; i < arr.length(); i++) {
                            friendsString += (String) arr.get(i) + ", ";
                        }
                        friends.setText(getString(R.string.friends) + ": " + friendsString);
                    }
                } catch (JSONException e) {
                    Toast.makeText(activity, R.string.error_server, Toast.LENGTH_SHORT).show();
                }

            }
        }.execute("/user/" + user.getUsername());

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create alert dialog to enter the username to add in.
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Add username to friends list");

                // Set up the input
                final EditText input = new EditText(activity);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = input.getText().toString();

                        JSONObject json = new JSONObject();
                        try {
                            json.put("user", activity.getUserInfo().getUsername());
                            json.put("add", username);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Send HTTP add friend request
                        new HttpPostRequestTask() {
                            @Override
                            protected void onPostExecute(JSONObject jsonObject) {
                                try {
                                    Toast.makeText(activity, jsonObject.get("message").toString(),Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.execute("/user/add", json.toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return v;
    }

}
