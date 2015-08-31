package se.umu.cs.c11epm.geochalapp.view;


import android.app.ActionBar;
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
import se.umu.cs.c11epm.geochalapp.model.UserInfo;
import se.umu.cs.c11epm.geochalapp.model.network.HttpPostRequestTask;

public class LoginFragment extends Fragment {

    private enum buttonType {
        CREATE, LOGIN;
    }

    private MainActivity activity;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        activity = (MainActivity) getActivity();

        ActionBar ab = activity.getActionBar();

        if(ab != null) {
            //Set title to login_welcome
            ab.setTitle(R.string.login_welcome);
        }

        final EditText username = (EditText) v.findViewById(R.id.username);
        final EditText password = (EditText) v.findViewById(R.id.password);
        Button mCreateUserButton = (Button) v.findViewById(R.id.createUserButton);
        Button mLoginButton = (Button) v.findViewById(R.id.loginButton);

        setupButtonListener(username, password, mCreateUserButton, buttonType.CREATE);
        setupButtonListener(username, password, mLoginButton, buttonType.LOGIN);

        return v;
    }

    private void setupButtonListener(final EditText username, final EditText password, Button mCreateUserButton, final buttonType type) {
        mCreateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String request = type == buttonType.LOGIN ? "/login" : "/user";

                final String uname = username.getText().toString();
                String pword = password.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", uname);
                    jsonObject.put("password", pword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new HttpPostRequestTask(){
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {
                        Log.d("MISTER", request);
                        Log.d("HELLO", jsonObject.toString());
                        try {
                            if(jsonObject.get("status").equals(200)) {
                                //OK, then return to sign in (Main)
                                Log.d("IF", "STATUS 200");
                                if(type.equals(buttonType.LOGIN)) {
                                    Log.d("IF", "LOGIN");
                                    activity.setUser(new UserInfo(uname, jsonObject.get("token").toString()));
                                    activity.changeView(MainActivity.views.MAIN);
                                } else {
                                    Log.d("ELSE", "CREATE");
                                    Toast.makeText(activity, R.string.create_user_success, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("ELSE", "NOT 200");
                                int errorString = type == buttonType.LOGIN ? R.string.error_login : R.string.error_create;
                                Toast.makeText(activity, errorString, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }.execute(request, jsonObject.toString());
            }
        });
    }


}
