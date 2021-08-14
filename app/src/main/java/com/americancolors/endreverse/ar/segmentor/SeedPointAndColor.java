package com.americancolors.endreverse.ar.segmentor;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class SeedPointAndColor implements Cloneable {

    /* renamed from: b */
    public int f7760b;

    /* renamed from: cb */
    public int f7761cb;

    /* renamed from: cr */
    public int f7762cr;
    public int diffY;
    public boolean firstCheck;

    /* renamed from: g */
    public int f7763g;
    public double meanY;
    public boolean outside;

    /* renamed from: r */
    public int f7764r;
    public Point tapPoint;
    public boolean validInArea;

    /* renamed from: y */
    public int f7765y;

    public SeedPointAndColor() {
    }

    public SeedPointAndColor(Point dVar, int[] iArr) {
        this.tapPoint = dVar;
        this.f7764r = iArr[0];
        this.f7763g = iArr[1];
        this.f7760b = iArr[2];
        Mat mat = new Mat(1, 1, CvType.CV_16S);
        mat.get(0, 0, iArr);
        mat.assignTo(mat, CvType.CV_8U);
        Mat clone = mat.clone();
        Imgproc.cvtColor(mat, clone, 37);
        clone.assignTo(clone, CvType.CV_16S);
        this.f7765y = (int) clone.get(0, 0)[0];
        this.f7761cb = (int) clone.get(0, 0)[1];
        this.f7762cr = (int) clone.get(0, 0)[2];
        this.validInArea = true;
        this.firstCheck = false;
        this.outside = false;
    }

    public SeedPointAndColor(Point dVar, int[] iArr, int i) {
        this.tapPoint = dVar;
        this.f7764r = iArr[0];
        this.f7763g = iArr[1];
        this.f7760b = iArr[2];
        Mat mat = new Mat(1, 1, CvType.CV_16S);
        mat.get(0, 0, iArr);
        mat.assignTo(mat, CvType.CV_8U);
        Mat clone = mat.clone();
        Imgproc.cvtColor(mat, clone, 37);
        clone.assignTo(clone, CvType.CV_16S);
        this.f7765y = (int) clone.get(0, 0)[0];
        this.f7761cb = (int) clone.get(0, 0)[1];
        this.f7762cr = (int) clone.get(0, 0)[2];
        this.meanY = (double) i;
        this.validInArea = true;
        this.firstCheck = false;
    }
}
