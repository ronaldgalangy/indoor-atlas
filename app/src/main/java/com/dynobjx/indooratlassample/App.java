package com.dynobjx.indooratlassample;

import android.app.Application;
import android.os.Bundle;

import com.indooratlas.android.sdk.IALocation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by root on 12/2/16.
 */

public class App extends Application {

    public static final String RADIUS_KEY = "radius";

    @Override
    public void onCreate() {
        super.onCreate();
        /*WayPoints wayPoints = WayPoints.getInstance(this);

        ArrayList<CheckPoint> checkPoints = new ArrayList<>();
        IALocation iaLocation = new IALocation.Builder()
                .withFloorLevel(30)
                .withLatitude(14.58783225)
                .withLongitude(121.06150665).build();

        IALocation iaLocation2 = new IALocation.Builder()
                .withFloorLevel(30)
                .withLatitude(14.58782753)
                .withLongitude(121.06135282).build();

        checkPoints.add(new CheckPoint("ayannah", iaLocation));
        checkPoints.add(new CheckPoint("elevator", iaLocation2));

        wayPoints.setCheckPoints(checkPoints);*/

        WayPoints wayPoints = WayPoints.getInstance(this);

        ArrayList<CheckPoint> checkPoints = new ArrayList<>();
        IALocation ayannah = new IALocation.Builder()
                .withFloorLevel(30)
                .withLatitude(14.58776608)  //14.58776608, 121.06155812
                .withLongitude(121.06155812).build();

        IALocation elevator = new IALocation.Builder()
                .withFloorLevel(30)
                .withLatitude(14.58787944)
                .withLongitude(121.06134903).build();

        IALocation dyno = new IALocation.Builder()
                .withFloorLevel(30)
                .withLatitude(14.58787551)
                .withLongitude(121.06114524).build();

        IALocation oldDyno = new IALocation.Builder()
                .withFloorLevel(30)
                .withLatitude(14.58776947)
                .withLongitude(121.06135454).build();

        checkPoints.add(new CheckPoint("ayannah", ayannah));
        checkPoints.add(new CheckPoint("elevator", elevator));
        checkPoints.add(new CheckPoint("3010", dyno));
        checkPoints.add(new CheckPoint("3006", oldDyno));

        wayPoints.setCheckPoints(checkPoints);

        /** initialize okhttp client */
        File cacheDir = getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = getCacheDir();
        }
        final Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        System.out.println(">>> " + request.url());
                        return chain.proceed(request);
                    }
                })
                .cache(cache)
                .build();

        /** initialize picasso */
        final PicassoSingleton picassoSingleton = PicassoSingleton.getInstance();
        picassoSingleton.initPicasso(this, okHttpClient);
    }
}
