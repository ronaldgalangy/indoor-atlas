package com.dynobjx.indooratlassample;

import android.content.Context;

import com.indooratlas.android.sdk.IALocation;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 12/2/16.
 */
public class WayPoints {
    private static WayPoints ourInstance = new WayPoints();
    private Context context;

    private List<CheckPoint> checkPoints = new ArrayList<>();

    public static WayPoints getInstance(Context context) {
        ourInstance.context = context;
        return ourInstance;
    }

    private WayPoints() {
    }

    public List<CheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(List<CheckPoint> checkPoints) {
        this.checkPoints = checkPoints;
    }

    public CheckPoint checkIfWayPointExists(IALocation iaLocation) {

        for (CheckPoint checkPoint : this.checkPoints) {
            final IALocation location = checkPoint.getIaLocation();
            final float distanceTo = iaLocation.toLocation().distanceTo(location.toLocation());
            final float radius = PrefsHelper.getFloat(this.context, App.RADIUS_KEY);
            if (distanceTo < radius) {
                return checkPoint;
            }
            /*DecimalFormat df= new DecimalFormat("000.00000000");
            String formattedLatitude = df.format(iaLocation.getLatitude());
            String formattedLongtitude = df.format(iaLocation.getLatitude());
            try {
                double finalLatitude = (Double)df.parse(formattedLatitude);
                double finalLongtitude = (Double)df.parse(formattedLongtitude);

                System.out.println("finalLatitude : " + finalLatitude);
                System.out.println("finalLongtitude : " + finalLongtitude);

                if (location.getLatitude() == finalLatitude &&
                        location.getLongitude() == finalLongtitude) {
                    return location;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
        }
        return null;
    }
}
