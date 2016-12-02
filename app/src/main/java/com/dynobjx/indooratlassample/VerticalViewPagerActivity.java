package com.dynobjx.indooratlassample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalViewPagerActivity extends AppCompatActivity {

    @BindView(R.id.vertical_pager)
    VerticalViewPager verticalPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_view_pager);
        ButterKnife.bind(this);
    }
}
