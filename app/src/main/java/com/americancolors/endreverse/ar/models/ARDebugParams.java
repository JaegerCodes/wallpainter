package com.americancolors.endreverse.ar.models;

import com.americancolors.endreverse.ar.arutils.ARObservables;
import com.americancolors.endreverse.ar.segmentor.Segmentor3ImplFinal;

public class ARDebugParams {
    public boolean applyHoughExtend;
    public int blurKernel;
    public ARObservables.CameraFocusMode cameraFocusMode;
    public boolean cannyFlag;
    public int cannyMax;
    public int cannyMin;
    public int cvImageWidth;
    public int delayValue;
    public int edKernel;
    public boolean enableLightCorrection;
    public int exposureCompensationIndex;
    public int frameImageHeight;
    public int frameImageWidth;
    public boolean gaussBlurflag;
    public int houghExtendLength;
    public float imageContrast;
    public float imageExposure;
    public float imageHighlights;
    public float imageShadows;
    public boolean otsuFlag;
    public float otsuMultiplier;
    public boolean showPoints;
    public String timeTakenPerFrame;

    public ARDebugParams() {
        this.timeTakenPerFrame = "";
        this.delayValue = 60;
        this.enableLightCorrection = true;
        this.cannyFlag = false;
        this.exposureCompensationIndex = 0;
        this.otsuFlag = true;
        this.applyHoughExtend = true;
        this.houghExtendLength = 10;
        this.otsuMultiplier = 0.3f;
        this.delayValue = 40;
        this.cvImageWidth = Segmentor3ImplFinal.IMAGE_RESIZE_FRAME_WIDTH;
        this.cannyMin = 20;
        this.cannyMax = 25;
        this.blurKernel = 3;
        this.edKernel = 11;
        this.cameraFocusMode = ARObservables.CameraFocusMode.FOCUS_ON_START;
    }

    public int getBlurKernel() {
        return this.blurKernel;
    }

    public ARObservables.CameraFocusMode getCameraFocusMode() {
        return this.cameraFocusMode;
    }

    public int getCannyMax() {
        return this.cannyMax;
    }

    public int getCannyMin() {
        return this.cannyMin;
    }

    public int getCvImageWidth() {
        return this.cvImageWidth;
    }

    public int getDelayValue() {
        return this.delayValue;
    }

    public int getEdKernel() {
        return this.edKernel;
    }

    public int getExposureCompensationIndex() {
        return this.exposureCompensationIndex;
    }

    public int getFrameImageHeight() {
        return this.frameImageHeight;
    }

    public int getFrameImageWidth() {
        return this.frameImageWidth;
    }

    public int getHoughExtendLength() {
        return this.houghExtendLength;
    }

    public float getImageContrast() {
        return this.imageContrast;
    }

    public float getImageExposure() {
        return this.imageExposure;
    }

    public float getImageHighlights() {
        return this.imageHighlights;
    }

    public float getImageShadows() {
        return this.imageShadows;
    }

    public float getOtsuMultiplier() {
        return this.otsuMultiplier;
    }

    public String getTimeTakenPerFrame() {
        return this.timeTakenPerFrame;
    }

    public boolean isApplyHoughExtend() {
        return this.applyHoughExtend;
    }

    public boolean isCannyFlag() {
        return this.cannyFlag;
    }

    public boolean isEnableLightCorrection() {
        return this.enableLightCorrection;
    }

    public boolean isGaussBlurflag() {
        return this.gaussBlurflag;
    }

    public boolean isOTSUFlag() {
        return this.otsuFlag;
    }

    public void setApplyHoughExtend(boolean z) {
        this.applyHoughExtend = z;
    }

    public void setBlurKernel(int i) {
        this.blurKernel = i;
    }

    public void setCameraFocusMode(ARObservables.CameraFocusMode cameraFocusMode2) {
        this.cameraFocusMode = cameraFocusMode2;
    }

    public void setCannyFlag(boolean z) {
        this.cannyFlag = z;
    }

    public void setCannyMax(int i) {
        this.cannyMax = i;
    }

    public void setCannyMin(int i) {
        this.cannyMin = i;
    }

    public void setCvImageWidth(int i) {
        this.cvImageWidth = i;
    }

    public void setDelayValue(int i) {
        if (i <= 0 || i >= 1001) {
            i = 25;
        }
        this.delayValue = i;
    }

    public void setEdKernel(int i) {
        this.edKernel = i;
    }

    public void setEnableLightCorrection(boolean z) {
        this.enableLightCorrection = z;
    }

    public void setExposureCompensationIndex(int i) {
        this.exposureCompensationIndex = i;
    }

    public void setFrameImageHeight(int i) {
        this.frameImageHeight = i;
    }

    public void setFrameImageWidth(int i) {
        if (i > 300 && i < 5000) {
            this.frameImageWidth = i;
        }
    }

    public void setGaussBlurflag(boolean z) {
        this.gaussBlurflag = z;
    }

    public void setHoughExtendLength(int i) {
        this.houghExtendLength = i;
    }

    public void setImageContrast(float f) {
        this.imageContrast = f;
    }

    public void setImageExposure(float f) {
        this.imageExposure = f;
    }

    public void setImageHighlights(float f) {
        this.imageHighlights = f;
    }

    public void setImageShadows(float f) {
        this.imageShadows = f;
    }

    public void setOTSUFlag(boolean z) {
        this.otsuFlag = z;
    }

    public void setOtsuMultiplier(float f) {
        if (f > 0.1f && f < 1.0f) {
            this.otsuMultiplier = f;
        }
    }

    public void setShowPoints(boolean z) {
        this.showPoints = z;
    }

    public void setTimeTakenPerFrame(String str) {
        this.timeTakenPerFrame = str;
    }

    public boolean showPoints() {
        return this.showPoints;
    }
}
