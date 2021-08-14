package com.americancolors.endreverse.ar.models;

import android.graphics.Bitmap;

import com.americancolors.endreverse.ar.segmentor.LineEndPoints;

import java.util.ArrayList;
import java.util.List;

public class MyIdeaVisualizer {
    public List<RecColor> colorList = new ArrayList();
    public Bitmap coloredImage;
    public List<LineEndPoints> maskingTapeLineList = new ArrayList();
    public Bitmap originalImages;
    public List<SeedPointWithColor> seedPointWithColorList = new ArrayList();
    public String version;

    public List<RecColor> getColorList() {
        return this.colorList;
    }

    public Bitmap getColoredImage() {
        return this.coloredImage;
    }

    public List<LineEndPoints> getMaskingTapeLineList() {
        return this.maskingTapeLineList;
    }

    public Bitmap getOriginalImages() {
        return this.originalImages;
    }

    public List<SeedPointWithColor> getSeedPointList() {
        return this.seedPointWithColorList;
    }

    public String getVersion() {
        return this.version;
    }

    public void setColorList(List<RecColor> list) {
        this.colorList = list;
    }

    public void setColoredImage(Bitmap bitmap) {
        this.coloredImage = bitmap;
    }

    public void setMaskingTapeLineList(List<LineEndPoints> list) {
        this.maskingTapeLineList = list;
    }

    public void setOriginalImages(Bitmap bitmap) {
        this.originalImages = bitmap;
    }

    public void setSeedPointList(List<SeedPointWithColor> list) {
        this.seedPointWithColorList = list;
    }

    public void setVersion(String str) {
        this.version = str;
    }
}
