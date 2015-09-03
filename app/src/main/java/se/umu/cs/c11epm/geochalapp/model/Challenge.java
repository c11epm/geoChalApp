package se.umu.cs.c11epm.geochalapp.model;

import java.util.UUID;

/**
 * Created by emil on 2015-07-15.
 */
public class Challenge {
    //TODO add expire time and created time

    private String creatorUser;
    private String challengedUser;
    private String id;

    //GPS coordinates

    private double longitude;
    private double latitude;

    public Challenge(String creatorUser, String challengedUser, String id, double latitude, double longitude) {
        this.creatorUser = creatorUser;
        this.challengedUser = challengedUser;
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Challenge() {}

    public Position getPosition() {
        Position pos = new Position();
        pos.setLatitude(latitude);
        pos.setLongitude(longitude);
        return pos;
    }

    public void setPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(String creatorUser) {
        this.creatorUser = creatorUser;
    }

    public String getChallengedUser() {
        return challengedUser;
    }

    public void setChallengedUser(String challengedUser) {
        this.challengedUser = challengedUser;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
