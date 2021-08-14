package com.americancolors.endreverse.ar.arutils;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class TransparentOutlineProvider extends ViewOutlineProvider {
    public float mAlpha;

    public TransparentOutlineProvider(float f) {
        this.mAlpha = f;
    }

    public void getOutline(View view, Outline outline) {
        ViewOutlineProvider.BACKGROUND.getOutline(view, outline);
        outline.setAlpha(this.mAlpha);
    }
}
