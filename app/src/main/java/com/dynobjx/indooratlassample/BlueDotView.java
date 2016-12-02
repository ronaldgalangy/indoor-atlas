package com.dynobjx.indooratlassample;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Extends great ImageView library by Dave Morrissey. See more:
 * https://github.com/davemorrissey/subsampling-scale-image-view.
 */
public class BlueDotView extends SubsamplingScaleImageView {

    private float radius = 1.0f;
    private PointF dotCenter = null;
    private Context context;
    private int markerColor;

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setDotCenter(PointF dotCenter) {
        this.dotCenter = dotCenter;
    }

    public void setMarkerColor(int markerColor) {
        this.markerColor = markerColor;
    }

    public BlueDotView(Context context) {
        this(context, null);
        this.context = context;
    }

    public BlueDotView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        initialise();
    }

    private void initialise() {
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println();
        super.onDraw(canvas);

        if (!isReady()) {
            return;
        }

        if (dotCenter != null) {
            PointF vPoint = sourceToViewCoord(dotCenter);
            float scaledRadius = getScale() * radius;
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(this.markerColor);
            canvas.drawCircle(vPoint.x, vPoint.y, scaledRadius, paint);
        }
    }
}
