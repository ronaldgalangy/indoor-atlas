package com.dynobjx.indooratlassample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BasicActivity extends AppCompatActivity implements IALocationListener {

    @BindView(R.id.tv_location_coordinates) TextView tvLocationCoordinates;
    private IALocationManager iaLocationManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.bind(this);
        iaLocationManager = IALocationManager.create(this);
        final String text = getString(R.string.indooratlas_floor_plan_id);
        final IARegion region = IARegion.floorPlan(text);
        iaLocationManager.setLocation(IALocation.from(region));
    }

    @Override
    protected void onResume() {
        final IALocationRequest iaLocationRequest = IALocationRequest.create();
        iaLocationRequest.setFastestInterval(-1);
        iaLocationRequest.setSmallestDisplacement(-1);
        iaLocationManager.requestLocationUpdates(iaLocationRequest, this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        iaLocationManager.removeLocationUpdates(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        iaLocationManager.destroy();
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(IALocation location) {
        tvLocationCoordinates.setText(String.format(Locale.US, "%f,%f, accuracy: %.2f", location.getLatitude(),
                location.getLongitude(), location.getAccuracy()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
}
