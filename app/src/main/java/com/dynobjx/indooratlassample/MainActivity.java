package com.dynobjx.indooratlassample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.indooratlas.android.sdk.IALocationService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_basic)
    Button btnBasic;
    @BindView(R.id.btn_floor_plan)
    Button btnFloorPlan;
    @BindView(R.id.btn_with_google_map)
    Button btnWithGoogleMap;
    @BindView(R.id.btn_settings)
    Button btnSettings;
    @BindView(R.id.btn_image)
    Button btnImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnBasic.setOnClickListener(this);
        btnFloorPlan.setOnClickListener(this);
        btnWithGoogleMap.setOnClickListener(this);
        btnImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Class<?> clz = null;
        switch (view.getId()) {
            case R.id.btn_basic:
                clz = BasicActivity.class;
                break;
            case R.id.btn_floor_plan:
                clz = FloorPlanActivity.class;
                break;
            case R.id.btn_with_google_map:
                clz = GoogleMapActivity.class;
                break;
            case R.id.btn_settings:
                clz = SettingsActivity.class;
                break;
            case R.id.btn_image:
                clz = OtherActivity.class;
                break;
        }
        startActivity(new Intent(this, clz));
    }
}
