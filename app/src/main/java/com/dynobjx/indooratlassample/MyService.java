package com.dynobjx.indooratlassample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.indooratlas.android.sdk.IALocationService;

public class MyService extends IALocationService {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
