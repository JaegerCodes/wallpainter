package com.americancolors.endreverse.ar.segmentor;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class SeedPointAndColor implements Cloneable {

    public int b;
    public int cb;
    public int cr;
    public int diffY;
    public boolean firstCheck;
    public int g;
    public double meanY;
    public boolean outside;
    public int r;
    public Point tapPoint;
    public boolean validInArea;
    public int y;

    public SeedPointAndColor() {
    }

    public SeedPointAndColor(Point dVar, int[] iArr) {
        this.tapPoint = dVar;
        this.r = iArr[0];
        this.g = iArr[1];
        this.b = iArr[2];
        Mat mat = new Mat(1, 1, CvType.CV_32SC3);
        mat.put(0, 0, iArr);
        mat.convertTo(mat, CvType.CV_8UC3);
        Mat clone = mat.clone();
        Imgproc.cvtColor(mat, clone, 37);
        clone.convertTo(clone, CvType.CV_32SC3);
        this.y = (int) clone.get(0, 0)[0];
        this.cb = (int) clone.get(0, 0)[1];
        this.cr = (int) clone.get(0, 0)[2];
        this.validInArea = true;
        this.firstCheck = false;
        this.outside = false;
    }

    public SeedPointAndColor(Point dVar, int[] iArr, int i) {
        this.tapPoint = dVar;
        this.r = iArr[0];
        this.g = iArr[1];
        this.b = iArr[2];
        Mat mat = new Mat(1, 1, CvType.CV_32SC3);
        mat.put(0, 0, iArr);
        mat.convertTo(mat, CvType.CV_8UC3);
        Mat clone = mat.clone();
        Imgproc.cvtColor(mat, clone, 37);
        clone.convertTo(clone, CvType.CV_32SC3);
        this.y = (int) clone.get(0, 0)[0];
        this.cb = (int) clone.get(0, 0)[1];
        this.cr = (int) clone.get(0, 0)[2];
        this.meanY = (double) i;
        this.validInArea = true;
        this.firstCheck = false;
    }
}
