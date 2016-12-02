package com.dynobjx.indooratlassample;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;

public class PicassoSingleton {

    private Picasso picasso;
    private static PicassoSingleton PICASSASO_SINGLETON = new PicassoSingleton();
    private String CONTENT_NOT_FOUND = "Content Not Found";
    private String NOT_FOUND = "Not Found";

    private PicassoSingleton() {

    }

    public static PicassoSingleton getInstance() {
        return PICASSASO_SINGLETON;
    }

    public void initPicasso(final Context context, final OkHttpClient okHttpClient) {
        if (picasso == null) {
            picasso = new Picasso.Builder(context)
                    .executor(Executors.newSingleThreadExecutor())
                    .downloader(new OkHttp3Downloader(okHttpClient)).build();
        }
    }

    public Picasso getPicasso() {
        return picasso;
    }
}


