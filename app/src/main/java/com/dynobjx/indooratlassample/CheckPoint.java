package com.dynobjx.indooratlassample;

import com.indooratlas.android.sdk.IALocation;

/**
 * Created by root on 12/2/16.
 */

public class CheckPoint {
    private String name;
    private IALocation iaLocation;

    public CheckPoint(String name, IALocation iaLocation) {
        this.name = name;
        this.iaLocation = iaLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IALocation getIaLocation() {
        return iaLocation;
    }

    public void setIaLocation(IALocation iaLocation) {
        this.iaLocation = iaLocation;
    }
}
