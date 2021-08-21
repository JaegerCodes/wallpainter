package com.americancolors.endreverse.ar.segmentor;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.americancolors.endreverse.ar.arutils.ARObservables;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Segmentor2ImplFinal implements Segmentor {
    public int BorderSize;
    public final Point anchor = new Point(-1.0d, -1.0d);
    public Mat cacheImage;
    public Mat cacheMask;
    public List<SeedPointAndColor> cachePoints;
    public Bitmap cacheResult;
    public Mat cannyEdges;
    public Mat ellipseStructElement;
    {
        Imgproc.getStructuringElement(2, new Size(ARObservables.currentARDebugParams.getEdKernel(), ARObservables.currentARDebugParams.getEdKernel()), this.anchor);
    }
    public float imageSimilarityThreshold;
    public boolean isDarkWallShiftEnabled;
    public boolean isLightWallShiftEnabled;
    public Scalar lower;
    public int mCurrentNValue;
    public boolean mEnableYShift;
    public double pointSimilarityThreshold;
    public final Scalar scalar_0 = new Scalar(0.0d);
    public final Scalar scalar_255 = new Scalar(255.0d);
    public Scalar upper;

    @SuppressLint({"CheckResult"})
    public Segmentor2ImplFinal(Boolean enableYShift, Boolean isDarkWallShiftEnabled, Boolean isLightWallShiftEnabled, int i, float f, double d) {
        this.ellipseStructElement = Imgproc.getStructuringElement(2, new Size(ARObservables.currentARDebugParams.getEdKernel(), ARObservables.currentARDebugParams.getEdKernel()), this.anchor);
        this.lower = new Scalar(20.0d, 20.0d, 20.0d);
        this.upper = this.lower;

        this.BorderSize = 1;
        this.imageSimilarityThreshold = f;
        this.pointSimilarityThreshold = d;
        this.mEnableYShift = enableYShift;
        this.isDarkWallShiftEnabled = isDarkWallShiftEnabled;
        this.isLightWallShiftEnabled = isLightWallShiftEnabled;
        this.mCurrentNValue = i;
        Core.setNumThreads(6);
    }

    private Mat detectEdges(Mat mat, List<LineEndPoints> list) {
        Mat mat2 = new Mat(mat.size(), CvType.CV_8UC1);
        double cannyMin = ARObservables.currentARDebugParams.getCannyMin();
        double cannyMax = ARObservables.currentARDebugParams.getCannyMax();
        Imgproc.medianBlur(mat, mat, ARObservables.currentARDebugParams.getBlurKernel());
        if (ARObservables.currentARDebugParams.isOTSUFlag()) {
            Mat a2 = Mat.zeros(mat.size(), mat.type());
            if (ARObservables.currentARDebugParams.getImageExposure() != 0.0f) {
                Imgproc.Laplacian(mat, a2, mat.type(), (int) ARObservables.currentARDebugParams.getImageExposure());
            }
            Core.subtract(mat, a2, mat2);
            Imgproc.Canny(mat2.clone(), mat2, cannyMin, cannyMax, 3, true);
        } else {
            Imgproc.Canny(mat, mat2, cannyMin, cannyMax);
        }
        Imgproc.dilate(mat2, mat2, this.ellipseStructElement, this.anchor, 1);
        if (ARObservables.currentARDebugParams.isApplyHoughExtend()) {
            mat2 = mat2.clone();
        }
        if (ARObservables.currentARDebugParams.showPoints()) {
            Core.bitwise_not(mat2, mat2);
        }
        if (list != null) {
            for (LineEndPoints lineEndPoints : list) {
                Imgproc.line(mat2, lineEndPoints.startPoint, lineEndPoints.endPoint, new Scalar(255.0d), 1);
            }
        }
        return mat2;
    }

    private Mat getSegementedMask(Mat mat, Mat mat2, List<SeedPointAndColor> list, List<LineEndPoints> list2) {
        int i;
        ArrayList<SeedPointAndColor> arrayList = new ArrayList<>(list);
        int i2 = mat.rows();
        int b = mat.cols();
        ArrayList<Mat> arrayList2 = new ArrayList<>();
        Core.split(mat2, arrayList2);
        Mat detectEdges = detectEdges(arrayList2.get(0), list2);
        Mat clone = detectEdges.clone();
        if (ARObservables.currentARDebugParams.isCannyFlag()) {
            this.cannyEdges = detectEdges.clone();
        }
        int i3 = this.BorderSize;
        Core.copyMakeBorder(clone, clone, i3, i3, i3, i3, 0, this.scalar_0);
        int i4 = 0;
        while (i4 < arrayList.size()) {
            Point dVar = (arrayList.get(i4)).tapPoint;
            double d = dVar.x;
            i = i4;
            if (d >= 0.0d) {
                double d2 = dVar.y;
                if (!(d2 < 0.0d) && !(d >= ((double) b)) && !(d2 >= ((double) i2))) {
                    double d3 = clone.get((int) d2, (int) d)[0];
                    if (d3 == 0.0d || d3 == 255.0d) {
                        getMask(mat, clone, dVar, i + 1);
                    } else {
                        (arrayList.get(((int) d3) - 1)).validInArea = false;
                        Mat a2 = Mat.zeros(clone.size(), clone.type());
                        Core.compare(clone, new Scalar(d3), a2, 0);
                        clone.setTo(new Scalar((i + 1)), a2);
                    }
                }
            }
            i4 = i + 1;
        }
        Mat mat3 = new Mat(clone, new Range(1, clone.rows() - 1), new Range(1, clone.cols() - 1));
        Mat b2 = Mat.zeros(i2, b, CvType.CV_8UC1);
        Core.subtract(mat3, detectEdges, b2);
        Imgproc.morphologyEx(b2, b2, 3, this.ellipseStructElement, this.anchor, 3);
        return b2;
    }

    private boolean isSimilarImage(Mat mat) {
        Mat mat2 = this.cacheImage;
        if (mat2 == null || mat2.size() != mat.size()) {
            return false;
        }
        Mat mat3 = new Mat(mat.size(), CvType.CV_8UC4);
        Core.absdiff(this.cacheImage, mat, mat3);
        double[] dArr = Core.mean(mat3).val;
        return ((dArr[0] + dArr[1]) + dArr[2]) / 3.0d <= ((double) this.imageSimilarityThreshold);
    }

    private boolean isSimilarTapPoints(List<SeedPointAndColor> list) {
        if (list == null || this.cachePoints == null || list.size() != this.cachePoints.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            SeedPointAndColor seedPointAndColor = list.get(i);
            SeedPointAndColor seedPointAndColor2 = this.cachePoints.get(i);
            if (!(Math.sqrt(Math.pow(seedPointAndColor.tapPoint.x - seedPointAndColor2.tapPoint.x, 2.0d) + Math.pow(seedPointAndColor.tapPoint.y - seedPointAndColor2.tapPoint.y, 2.0d)) <= this.pointSimilarityThreshold && seedPointAndColor.r == seedPointAndColor2.r && seedPointAndColor.g == seedPointAndColor2.g && seedPointAndColor.b == seedPointAndColor2.b && seedPointAndColor.meanY == seedPointAndColor2.meanY)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void invalidateCache() {
        this.cacheImage = null;
        this.cachePoints = null;
        this.cacheResult = null;
    }

    @Override
    public Bitmap predictAndColorMultiTapSingleMask(Bitmap bitmap, List<SeedPointAndColor> list) {
        return predictAndColorMultiTapSingleMask(bitmap, list, null);
    }

    @Override
    public Bitmap predictAndColorMultiTapSingleMask(Bitmap bitmap, List<SeedPointAndColor> list, List<LineEndPoints> list2) {
        Mat mat;
        Bitmap bitmap2;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Mat mat2 = new Mat(height, width, CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, mat2);
        Imgproc.cvtColor(mat2, mat2, 3);
        Mat mat3 = new Mat(height, width, CvType.CV_8UC3);
        Imgproc.cvtColor(mat2, mat3, 36);
        new Mat();
        if (!isSimilarImage(mat2) || !isSimilarTapPoints(list)) {
            this.cacheImage = mat2;
            this.cachePoints = list;
            try {
                mat = getSegementedMask(mat2, mat3, list, list2);
            } catch (IndexOutOfBoundsException unused) {
                mat = this.cacheMask;
            }
            this.cacheMask = mat;
        } else {
            mat = this.cacheMask;
        }
        Mat reColor = reColor(mat2, mat3, mat, list);
        if (ARObservables.currentARDebugParams.isCannyFlag()) {
            bitmap2 = Bitmap.createBitmap(this.cannyEdges.cols(), this.cannyEdges.rows(), Bitmap.Config.ARGB_8888);
            Mat mat4 = new Mat();
            Imgproc.cvtColor(this.cannyEdges, mat4, 9);
            Utils.matToBitmap(mat4, bitmap2);
        } else {
            Bitmap createBitmap = Bitmap.createBitmap(reColor.cols(), reColor.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(reColor, createBitmap);
            bitmap2 = createBitmap;
        }
        if (!ARObservables.currentARDebugParams.isCannyFlag()) {
            this.cacheResult = bitmap2;
        }
        return bitmap2;
    }

    public Mat reColor(Mat mat, Mat mat2, Mat mat3, List<SeedPointAndColor> list) {
        Mat mat4 = new Mat(mat.height(), mat.width(), CvType.CV_8UC4);
        mat2.channels();
        ArrayList<Mat> arrayList = new ArrayList<>();
        Core.split(mat2, arrayList);
        int i = 0;
        Mat mat5 = arrayList.get(0);
        Mat mat6 = arrayList.get(1);
        Mat mat7 = arrayList.get(2);
        int i2 = 0;
        while (i2 < list.size()) {
            new Scalar(0.0d);
            Mat mat8 = new Mat(mat3.size(), 4);
            Mat mat9 = new Mat(mat3.size(), 4);
            int i3 = i2 + 1;
            Core.compare(mat3, new Scalar(i3), mat8, i);
            Core.bitwise_not(mat8, mat9);
            Scalar c = Core.mean(mat5, mat8);
            list.get(i2).meanY = c.val[i];
            list.get(i2).diffY = (int) (c.val[i] - ((double) list.get(i2).y));
            Scalar gVar = new Scalar((-list.get(i2).diffY));
            Scalar gVar2 = new Scalar(list.get(i2).cb);
            Scalar gVar3 = new Scalar(list.get(i2).cr);
            Core.add(mat5, gVar, mat5, mat8);
            Core.bitwise_and(mat6, mat9, mat6);
            Core.bitwise_and(mat7, mat9, mat7);
            Core.add(mat6, gVar2, mat6, mat8);
            Core.add(mat7, gVar3, mat7, mat8);
            i2 = i3;
            i = 0;
        }
        arrayList.set(i, mat5);
        arrayList.set(1, mat6);
        arrayList.set(2, mat7);
        Core.merge(arrayList, mat2);
        mat2.convertTo(mat2, CvType.CV_8UC3);
        Imgproc.cvtColor(mat2, mat2, 38);
        Imgproc.cvtColor(mat2, mat4, 2);
        return mat4;
    }

    public void getMask(Mat mat, Mat mat2, Point dVar, int i) {
        Imgproc.floodFill(mat, mat2, dVar, this.scalar_255, Imgproc.boundingRect(mat2), this.lower, this.upper, (i << 8) | 8 | 131072);
        //return mat2;
    }

    /*private Mat hdrConversion(Mat mat) {
        MergeMertens a2 = Photo.createMergeMertens();
        Imgproc.cvtColor(mat, new Mat(), 1);
        Mat mat2 = new Mat();
        Mat mat3 = new Mat();
        Imgproc.cvtColor(mat, mat2, 1);
        Imgproc.cvtColor(mat, mat3, 1);
        mat.convertTo(mat2, -1, (1.0f - ARObservables.currentARDebugParams.getImageContrast()), 1.0d);
        mat.convertTo(mat3, -1, (ARObservables.currentARDebugParams.getImageContrast() + 1.0f), 1.0d);
        ArrayList<Mat> arrayList = new ArrayList<>();
        arrayList.add(mat2);
        arrayList.add(mat);
        arrayList.add(mat3);
        a2.process(arrayList, mat);
        return mat;
    }

    private int getAggregateY(Point dVar, int[] iArr, int i, int i2) {
        int i3 = ((int) dVar.x) - 1;
        int i4 = ((int) dVar.y) - 1;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (i5 < 3) {
            int i8 = i6;
            for (int i9 = 0; i9 < 3; i9++) {
                int i10 = i3 + i5;
                int i11 = i4 + i9;
                if (i10 > 0 && i11 > 0 && i10 < i && i10 < i2) {
                    i8 += iArr[(i10 * i) + i2];
                    i7++;
                }
            }
            i5++;
            i6 = i8;
        }
        return i6 / i7;
    }

    private int clamp(int i) {
        if (i < 0) {
            return 0;
        }
        return Math.min(i, 255);
    }

    public void a(ARDebugParams aRDebugParams) {
        this.ellipseStructElement = Imgproc.getStructuringElement(0, new Size(ARObservables.currentARDebugParams.getEdKernel(), ARObservables.currentARDebugParams.getEdKernel()), this.anchor);
        this.lower = new Scalar(aRDebugParams.getHoughExtendLength(), aRDebugParams.getHoughExtendLength(), aRDebugParams.getHoughExtendLength());
        this.upper = new Scalar(aRDebugParams.getHoughExtendLength(), aRDebugParams.getHoughExtendLength(), aRDebugParams.getHoughExtendLength());
    }

    public void b(ARDebugParams aRDebugParams) {
        this.ellipseStructElement = Imgproc.getStructuringElement(0, new Size(ARObservables.currentARDebugParams.getEdKernel(), ARObservables.currentARDebugParams.getEdKernel()), this.anchor);
        this.lower = new Scalar(aRDebugParams.getHoughExtendLength(), aRDebugParams.getHoughExtendLength(), aRDebugParams.getHoughExtendLength());
        this.upper = new Scalar(aRDebugParams.getHoughExtendLength(), aRDebugParams.getHoughExtendLength(), aRDebugParams.getHoughExtendLength());
    }*/

    @Override
    public void setIsDarkWallShiftFlag(boolean z) {
        this.isDarkWallShiftEnabled = z;
    }

    @Override
    public void setIsLightWallShiftFlag(boolean z) {
        this.isLightWallShiftEnabled = z;
    }
}
