package com.dynobjx.indooratlassample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherActivity extends AppCompatActivity {

    @BindView(R.id.iv_map)
    ImageView ivMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        ButterKnife.bind(this);

        PicassoSingleton picassoSingleton = PicassoSingleton.getInstance();

        picassoSingleton.getPicasso().load("https://idaweb.blob.core.windows.net/imageblobcontainer/8c50303d-8805-4790-9ee2-fc05e1fa4d12")
                .into(ivMap, new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("success");
                    }

                    @Override
                    public void onError() {
                        System.out.println("error");
                    }
                });

    }
}
