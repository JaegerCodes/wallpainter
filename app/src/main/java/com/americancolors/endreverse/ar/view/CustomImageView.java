package com.americancolors.endreverse.ar.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import com.americancolors.endreverse.ar.models.MaskTapeSeedInfo;

import java.util.ArrayList;
import java.util.List;

public class CustomImageView extends AppCompatImageView {
    public static final int STROKE_WIDTH = 3;
    public int mImageViewLeft;
    public int mImageViewTop;
    public List<View> mMaskTapeEndPoint = new ArrayList<>();
    public List<View> mMaskTapeStartPoint = new ArrayList<>();
    public float mMaskingNodeWidth;
    public Paint mPaint = new Paint();
    public List<MaskTapeSeedInfo> reMappedMaskedPointData1 = new ArrayList<>();
    public List<MaskTapeSeedInfo> reMappedMaskedPointData2 = new ArrayList<>();

    public CustomImageView(Context context) {
        super(context);
        init();
    }

    public CustomImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CustomImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        this.mPaint.setAlpha(255);
        this.mPaint.setStrokeWidth(TypedValue.applyDimension(1, 3.0f, getResources().getDisplayMetrics()));
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(-1);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setPathEffect(new DashPathEffect(new float[]{10.0f, 10.0f, 10.0f, 10.0f}, 0.0f));
    }

    private void remapViewsToPoints(Canvas canvas) {
        List<View> list;
        this.reMappedMaskedPointData1.clear();
        this.reMappedMaskedPointData2.clear();
        List<View> list2 = this.mMaskTapeStartPoint;
        if (list2 != null && !list2.isEmpty() && (list = this.mMaskTapeEndPoint) != null && !list.isEmpty()) {
            int i = 0;
            while (i < this.mMaskTapeStartPoint.size() && i < this.mMaskTapeEndPoint.size()) {
                View view = this.mMaskTapeStartPoint.get(i);
                View view2 = this.mMaskTapeEndPoint.get(i);
                if (!(view == null || view2 == null)) {
                    float x = (view.getX() - ((float) this.mImageViewLeft)) + (this.mMaskingNodeWidth / 2.0f);
                    float y = (view.getY() - ((float) this.mImageViewTop)) + (this.mMaskingNodeWidth / 2.0f);
                    float x2 = (view2.getX() - ((float) this.mImageViewLeft)) + (this.mMaskingNodeWidth / 2.0f);
                    float y2 = (view2.getY() - ((float) this.mImageViewTop)) + (this.mMaskingNodeWidth / 2.0f);
                    if (canvas != null) {
                        canvas.drawLine(x, y, x2, y2, this.mPaint);
                    }
                    this.reMappedMaskedPointData1.add(new MaskTapeSeedInfo(x, y));
                    this.reMappedMaskedPointData2.add(new MaskTapeSeedInfo(x2, y2));
                }
                i++;
            }
        }
    }

    public List<MaskTapeSeedInfo> getReMappedMaskedPointData1() {
        return this.reMappedMaskedPointData1;
    }

    public List<MaskTapeSeedInfo> getReMappedMaskedPointData2() {
        return this.reMappedMaskedPointData2;
    }
    public void onDraw(Canvas paramCanvas)
    {
        if ((getDrawable() instanceof BitmapDrawable))
        {
            Bitmap localBitmap = ((BitmapDrawable)getDrawable()).getBitmap();
            if ((localBitmap != null) && (localBitmap.isRecycled())) {
                return;
            }
        }
        super.onDraw(paramCanvas);
        remapViewsToPoints(paramCanvas);
    }

    public void updateDrawLine(int i, int i2, List<View> list, List<View> list2, float f) {
        this.mMaskTapeStartPoint = list;
        this.mMaskTapeEndPoint = list2;
        this.mImageViewTop = i2;
        this.mImageViewLeft = i;
        this.mMaskingNodeWidth = f;
        remapViewsToPoints((Canvas) null);
    }
}

