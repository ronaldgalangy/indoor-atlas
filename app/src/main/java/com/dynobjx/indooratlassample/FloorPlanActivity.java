package com.dynobjx.indooratlassample;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FloorPlanActivity extends AppCompatActivity implements IARegion.Listener {

    @BindView(R.id.imageView)
    BlueDotView mImageView;

    // blue dot radius in meters
    private static final float dotRadius = 1.0f;
    @BindView(R.id.tv_log)
    TextView tvLog;
    private IALocationManager iaLocationManager;
    private IAResourceManager iaResourceManager;

    private IATask<IAFloorPlan> mPendingAsyncResult;
    private IAFloorPlan mFloorPlan;

    private long mDownloadId;
    private DownloadManager mDownloadManager;

    private final String TAG = FloorPlanActivity.class.getSimpleName();

    private IALocationListenerSupport iaLocationListenerSupport = new IALocationListenerSupport() {
        @Override
        public void onLocationChanged(IALocation location) {
            if (mFloorPlan != null && location.getRegion().getId().equals(getString(R.string.indooratlas_floor_plan_id)) &&
                    mImageView != null && mImageView.isReady()) {
                IALatLng latLng = new IALatLng(location.getLatitude(), location.getLongitude());
                PointF point = mFloorPlan.coordinateToPoint(latLng);
                mImageView.setDotCenter(point);
                final WayPoints wayPoints = WayPoints.getInstance(FloorPlanActivity.this);
                final CheckPoint checkPoint = wayPoints.checkIfWayPointExists(location);
                if (checkPoint != null) {
                    log("You are in " + checkPoint.getName());
                    mImageView.setMarkerColor(ContextCompat.getColor(FloorPlanActivity.this,
                            R.color.colorAccent));
                } else {
                    mImageView.setMarkerColor(ContextCompat.getColor(FloorPlanActivity.this,
                            R.color.colorPrimaryDark));
                    log("path : " + location.getLatitude() + ", " + location.getLongitude());
                }
                mImageView.postInvalidate();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);
        ButterKnife.bind(this);
        findViewById(android.R.id.content).setKeepScreenOn(true);

        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        iaLocationManager = IALocationManager.create(this);
        iaResourceManager = IAResourceManager.create(this);


        final String floorPlanId = getString(R.string.indooratlas_floor_plan_id);
        ensurePermissions();
        IALocationRequest iaLocationRequest = IALocationRequest.create();
        //iaLocationRequest.setFastestInterval(2000);
        //iaLocationRequest.setSmallestDisplacement(1f);
        iaLocationManager.requestLocationUpdates(iaLocationRequest, iaLocationListenerSupport);
        iaLocationManager.registerRegionListener(this);
        //mImageView.setRadius(mFloorPlan.getMetersToPixels() * dotRadius);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.floorplan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_radius:
                InputRadiusDialog inputRadiusDialog = InputRadiusDialog.newInstance();
                inputRadiusDialog.show(getSupportFragmentManager(), "input-radius");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        super.onResume();
    }

    private void ensurePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        //iaLocationManager.removeLocationUpdates(iaLocationListenerSupport);
        //iaLocationManager.unregisterRegionListener(this);
        unregisterReceiver(onComplete);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        iaLocationManager.destroy();
        super.onDestroy();
    }

    /**
     * Region change listener.
     */

    @Override
    public void onEnterRegion(IARegion iaRegion) {
        if (iaRegion.getType() == IARegion.TYPE_FLOOR_PLAN && iaRegion.getId().equals(getString(R.string.indooratlas_floor_plan_id))) {
            fetchFloorPlan(iaRegion.getId());
            Log.d(TAG, iaRegion.getId() + " onEnterRegion");
        }
    }

    @Override
    public void onExitRegion(IARegion iaRegion) {

    }

    /**
     * Fetch floor plan here.
     */

    private void fetchFloorPlan(final String regionId) {
        cancelPendingNetworkCalls();
        final IATask<IAFloorPlan> asyncResult = iaResourceManager.fetchFloorPlanWithId(regionId);
        mPendingAsyncResult = asyncResult;
        if (mPendingAsyncResult != null) {
            mPendingAsyncResult.setCallback(new IAResultCallback<IAFloorPlan>() {
                @Override
                public void onResult(IAResult<IAFloorPlan> result) {
                    Log.d(TAG, "floor level : " + result.getResult().getFloorLevel());
                    Log.d(TAG, "floor plan url : " + result.getResult().getUrl());
                    setTitle(result.getResult().getName());
                    /*if (result.isSuccess() && result.getResult() != null) {
                        final PicassoSingleton picassoSingleton = PicassoSingleton.getInstance();
                        picassoSingleton.getPicasso()
                                .load(result.getResult().getUrl())
                                .into(mImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(TAG, "onSuccess: ");

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, "onError: ");
                                    }
                                });
                    }*/
                    if (result.isSuccess() && result.getResult() != null) {
                        mFloorPlan = result.getResult();
                        String fileName = mFloorPlan.getId() + ".img";
                        String filePath = Environment.getExternalStorageDirectory() + "/"
                                + Environment.DIRECTORY_DOWNLOADS + "/" + fileName;
                        File file = new File(filePath);
                        if (!file.exists()) {
                            DownloadManager.Request request =
                                    new DownloadManager.Request(Uri.parse(mFloorPlan.getUrl()));
                            request.setDescription("IndoorAtlas floor plan");
                            request.setTitle("Floor plan");
                            // requires android 3.2 or later to compile
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.
                                        Request.VISIBILITY_HIDDEN);
                            }
                            request.setDestinationInExternalPublicDir(Environment.
                                    DIRECTORY_DOWNLOADS, fileName);

                            mDownloadId = mDownloadManager.enqueue(request);
                        } else {
                            showFloorPlanImage(filePath);
                        }
                    } else {
                        // do something with error
                        if (!asyncResult.isCancelled()) {
                            Toast.makeText(FloorPlanActivity.this,
                                    (result.getError() != null
                                            ? "error loading floor plan: " + result.getError()
                                            : "access to floor plan denied"), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }, Looper.getMainLooper()); // deliver callbacks in main thread
        }
    }

    private void cancelPendingNetworkCalls() {
        if (mPendingAsyncResult != null && !mPendingAsyncResult.isCancelled()) {
            mPendingAsyncResult.cancel();
        }
    }

    private void showFloorPlanImage(String filePath) {
        mImageView.setRadius(mFloorPlan.getMetersToPixels() * dotRadius);
        mImageView.setImage(ImageSource.uri(filePath));
    }

    /**
     * Methods for fetching floor plan data and bitmap image.
     * Method {@link #fetchFloorPlan(String id)} fetches floor plan data including URL to bitmap
     */

     /*  Broadcast receiver for floor plan image download */
    private BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
            if (id != mDownloadId) {
                //Log.w(TAG, "Ignore unrelated download");
                return;
            }
            //Log.w(TAG, "Image download completed");
            Bundle extras = intent.getExtras();
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = mDownloadManager.query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // process download
                    String filePath = c.getString(c.getColumnIndex(
                            DownloadManager.COLUMN_LOCAL_FILENAME));
                    showFloorPlanImage(filePath);
                }
            }
            c.close();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case 1:

                if (grantResults.length == 0
                        || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "error",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }

    }

    private void log(String txt) {
        tvLog.setText(tvLog.getText() + "\n" + txt);
    }
}
