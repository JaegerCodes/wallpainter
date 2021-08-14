package com.americancolors.endreverse.ar.segmentor;

import android.graphics.Bitmap;

import java.util.List;

public interface Segmentor {
    void invalidateCache();

    Bitmap predictAndColorMultiTapSingleMask(Bitmap bitmap, List<SeedPointAndColor> list);

    Bitmap predictAndColorMultiTapSingleMask(Bitmap bitmap, List<SeedPointAndColor> list, List<LineEndPoints> list2);

    void setIsDarkWallShiftFlag(boolean z);

    void setIsLightWallShiftFlag(boolean z);
}

