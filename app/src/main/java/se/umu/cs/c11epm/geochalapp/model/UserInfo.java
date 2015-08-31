package se.umu.cs.c11epm.geochalapp.model;

/**
 * Created by emil on 2015-08-31.
 */
public class UserInfo {
    private String username;
    private String token;

    public UserInfo(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
