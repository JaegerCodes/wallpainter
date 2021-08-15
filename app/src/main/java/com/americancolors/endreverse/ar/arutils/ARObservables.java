package com.americancolors.endreverse.ar.arutils;

import com.americancolors.endreverse.ar.models.ARDebugParams;

public class ARObservables {
    public static volatile ARDebugParams currentARDebugParams = new ARDebugParams();

    public enum CameraFocusMode {
        FOCUS_ON_START,
        FOCUS_AT_INTERVAL,
        FOCUS_ON_TAP,
        FOCUS_AUTO
    }
}
