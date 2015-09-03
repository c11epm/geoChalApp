package se.umu.cs.c11epm.geochalapp.model;

/**
 * Created by emil on 2015-09-03.
 */
public class Haversine {

    public static final double R = 6372.8; // In kilometers
    public static double haversine(Position challenge, Position user) {
        double dLat = Math.toRadians(challenge.getLatitude() - user.getLatitude());
        double dLon = Math.toRadians(challenge.getLongitude() - user.getLongitude());

        double a = Math.pow(Math.sin(dLat / 2),2) +
                Math.pow(Math.sin(dLon / 2),2) *
                        Math.cos(Math.toRadians(user.getLatitude())) *
                        Math.cos(Math.toRadians(challenge.getLatitude()));
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
