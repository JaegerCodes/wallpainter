package com.americancolors.endreverse.ar.arutils;

import android.content.Context;

import org.jetbrains.annotations.Nullable;

public class ARGlobals {
    public static String caffeModelContent = "";
    public static String caffeModelPath = "";
    public static volatile long globalTimeMillisStart;
    public static Integer lowerExposureIndex = 0;
    public static boolean notProcessingARFrame = true;
    public static boolean processingARFrame = false;
    public static String prototxtContent = "";
    public static volatile String prototxtPath = "";
    public static Integer upperExposureIndex = 0;
    public static ARUtils.VisualizerState visualizerState = ARUtils.VisualizerState.LIVE;

}
