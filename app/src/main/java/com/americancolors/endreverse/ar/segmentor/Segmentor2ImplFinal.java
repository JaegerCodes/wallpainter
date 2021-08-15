package com.americancolors.endreverse.ar.segmentor;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.americancolors.endreverse.ar.arutils.ARObservables;
import com.americancolors.endreverse.ar.models.ARDebugParams;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.MergeMertens;
import org.opencv.photo.Photo;

import java.util.ArrayList;
import java.util.List;

public class Segmentor2ImplFinal implements Segmentor {
    public static final int ANCHOR_POS_X = -1;
    public static final int ANCHOR_POS_Y = -1;
    public static final int BORDER_SIZE = 1;
    public static final int SCALAR_0_VECT = 0;
    public static final int SCALAR_255_VECT = 255;
    public static final int SCALE_ALL_VALUE = 1;
    public static final int SCALE_VECT_0 = 20;
    public static final int SCALE_VECT_1 = 20;
    public static final int SCALE_VECT_2 = 20;
    public static final int SIZE_HEIGHT = 3;
    public static final int SIZE_WIDTH = 3;
    public int BorderSize;
    public String TAG = "SegmentationModel";
    public final Point anchor = new Point(-1.0d, -1.0d);
    public Mat cacheImage;
    public Mat cacheMask;
    public List<SeedPointAndColor> cachePoints;
    public Bitmap cacheResult;
    public Mat cannyEdges;
    public Mat dilationKernel = new Mat(new Size(3.0d, 3.0d), CvType.CV_8UC1, Scalar.all(1.0d));
    public Mat ellipseStructElement = Imgproc.getStructuringElement(2, new Size((double) ARObservables.currentARDebugParams.getEdKernel(), (double) ARObservables.currentARDebugParams.getEdKernel()), this.anchor);
    public Mat erosionKernel = new Mat(new Size(3.0d, 3.0d), CvType.CV_8UC1, Scalar.all(1.0d));
    public float imageSimilarityThreshold;
    public boolean isDarkWallShiftEnabled;
    public boolean isLightWallShiftEnabled;
    public Scalar lower;
    public int mCurrentNValue;
    public boolean mEnableYShift;
    public double pointSimilarityThreshold;
    public final Scalar scalar_0;
    public final Scalar scalar_255;
    public Scalar upper;

    @SuppressLint({"CheckResult"})
    public Segmentor2ImplFinal(Boolean bool, Boolean bool2, Boolean bool3, int i, float f, double d) {
        //ARObservables.arDebugParamsObservable.d(new C2671b(this));
        this.ellipseStructElement = Imgproc.getStructuringElement(2, new Size((double) ARObservables.currentARDebugParams.getEdKernel(), (double) ARObservables.currentARDebugParams.getEdKernel()), this.anchor);
        //ARObservables.arDebugParamsObservable.d(new C2670a(this));
        this.lower = new Scalar(20.0d, 20.0d, 20.0d);
        this.upper = this.lower;
        this.scalar_0 = new Scalar(0.0d);
        this.scalar_255 = new Scalar(255.0d);
        this.BorderSize = 1;
        this.imageSimilarityThreshold = f;
        this.pointSimilarityThreshold = d;
        this.mEnableYShift = bool.booleanValue();
        this.isDarkWallShiftEnabled = bool2.booleanValue();
        this.isLightWallShiftEnabled = bool3.booleanValue();
        this.mCurrentNValue = i;
        Core.setNumThreads(6); // Core.a(6)
    }

    private int clamp(int i) {
        if (i < 0) {
            return 0;
        }
        if (i > 255) {
            return 255;
        }
        return i;
    }

    private Mat detectEdges(Mat mat, List<LineEndPoints> list) {
        Mat mat2 = new Mat(mat.size(), CvType.CV_8UC1);
        double cannyMin = (double) ARObservables.currentARDebugParams.getCannyMin();
        double cannyMax = (double) ARObservables.currentARDebugParams.getCannyMax();
        Imgproc.medianBlur(mat, mat, ARObservables.currentARDebugParams.getBlurKernel());
        if (ARObservables.currentARDebugParams.isOTSUFlag()) {
            Mat a = Mat.zeros(mat.size(), mat.type());
            if (ARObservables.currentARDebugParams.getImageExposure() != 0.0f) {
                Imgproc.Laplacian(mat, a, mat.type(), (int) ARObservables.currentARDebugParams.getImageExposure());
            }
            Core.subtract(mat, a, mat2);
            Imgproc.Canny(mat2.clone(), mat2, cannyMin, cannyMax, 3, true);
        } else {
            Imgproc.Canny(mat, mat2, cannyMin, cannyMax);
        }
        Imgproc.dilate(mat2, mat2, this.ellipseStructElement, this.anchor, 1);//Imgproc.a
        if (ARObservables.currentARDebugParams.isApplyHoughExtend()) {
            mat2 = mat2.clone();
        }
        if (ARObservables.currentARDebugParams.showPoints()) {
            Core.bitwise_not(mat2, mat2);
        }
        if (list != null) {
            for (LineEndPoints next : list) {
                Imgproc.line(mat2, next.startPoint, next.endPoint, new Scalar(255.0d), 1);
            }
        }
        return mat2;
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

    private Mat getMask(Mat mat, Mat mat2, Point dVar, int i) {
        Imgproc.floodFill(mat, mat2, dVar, this.scalar_255, Imgproc.boundingRect(mat2), this.lower, this.upper, (i << 8) | 8 | 131072);
        return mat2;
    }

    private Mat getSegementedMask(Mat mat, Mat mat2, List<SeedPointAndColor> list, List<LineEndPoints> list2) {
        int i;
        boolean z;
        ArrayList<SeedPointAndColor> arrayList = new ArrayList<>(list);
        int rows = mat.rows();
        int cols = mat.cols();
        ArrayList<Mat> arrayList2 = new ArrayList<>();
        Core.split(mat2, arrayList2);
        boolean z2 = false;
        Mat detectEdges = detectEdges((Mat) arrayList2.get(0), list2);
        Mat clone = detectEdges.clone();
        if (ARObservables.currentARDebugParams.isCannyFlag()) {
            this.cannyEdges = detectEdges.clone();
        }
        int i3 = this.BorderSize;
        Core.copyMakeBorder(clone, clone, i3, i3, i3, i3, 0, this.scalar_0);
        int i4 = 0;
        while (i4 < arrayList.size()) {
            Point dVar = ((SeedPointAndColor) arrayList.get(i4)).tapPoint;
            double d = dVar.x;
            if (d >= 0.0d) {
                i = i4;
                double d2 = dVar.y;
                if (d2 < 0.0d || d >= ((double) cols) || d2 >= ((double) rows)) {
                    Mat mat3 = mat;
                    z = false;
                } else {
                    z = false;
                    double d3 = clone.get((int) d2, (int) d)[0];
                    if (d3 == 0.0d || d3 == 255.0d) {
                        getMask(mat, clone, dVar, i + 1);
                    } else {
                            ((SeedPointAndColor) arrayList.get(((int) d3) - 1)).validInArea = false;
                        Mat a = Mat.zeros(clone.size(), clone.type());
                        Core.compare(clone, new Scalar(d3), a, 0);
                        clone.setTo(new Scalar((double) (i + 1)), a);
                        Mat mat4 = mat;
                    }
                }
            } else {
                Mat mat5 = mat;
                i = i4;
                z = z2;
            }
            boolean z3 = z;
            i4 = i + 1;
            z2 = z3;
        }
        Mat mat6 = new Mat(clone, new Range(1, clone.rows() - 1), new Range(1, clone.cols() - 1));
        Mat b2 = Mat.zeros(rows, cols, CvType.CV_8UC1);
        Core.subtract(mat6, detectEdges, b2);
        Imgproc.morphologyEx(b2, b2, 3, this.ellipseStructElement, this.anchor, 3);
        return b2;
    }

    private Mat hdrConversion(Mat mat) {
        MergeMertens a = Photo.createMergeMertens();
        Imgproc.cvtColor(mat, new Mat(), 1); // Imgproc.a(mat, new Mat(), 1);
        Mat mat2 = new Mat();
        Mat mat3 = new Mat();
        Imgproc.cvtColor(mat, mat2, 1);
        Imgproc.cvtColor(mat, mat3, 1);
        Mat mat4 = mat;
        mat4.convertTo(mat2, -1, (double) (1.0f - ARObservables.currentARDebugParams.getImageContrast()), 1.0d);//mat4.a(mat2, -1, (double), (double));
        mat4.convertTo(mat3, -1, (double) (ARObservables.currentARDebugParams.getImageContrast() + 1.0f), 1.0d);
        ArrayList<Mat> arrayList = new ArrayList<>();
        arrayList.add(mat2);
        arrayList.add(mat);
        arrayList.add(mat3);
        a.process(arrayList, mat); // a.a(arrayList, mat);
        return mat;
    }

    private boolean isSimilarImage(Mat mat) {
        Mat mat2 = this.cacheImage;
        if (mat2 == null || mat2.size() != mat.size()) {
            return false;
        }
        Mat mat3 = new Mat(mat.size(), CvType.CV_8UC4);
        Core.add(this.cacheImage, mat, mat3);// Core.a(this.cacheImage, mat, mat3);
        double[] dArr = Core.mean(mat3).val;// Core.a(mat3).a
        return ((dArr[0] + dArr[1]) + dArr[2]) / 3.0d <= ((double) this.imageSimilarityThreshold);
    }

    private boolean isSimilarTapPoints(List<SeedPointAndColor> list) {
        if (list == null || this.cachePoints == null || list.size() != this.cachePoints.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            SeedPointAndColor seedPointAndColor = list.get(i);
            SeedPointAndColor seedPointAndColor2 = this.cachePoints.get(i);
            if (Math.sqrt(Math.pow(seedPointAndColor.tapPoint.x - seedPointAndColor2.tapPoint.x, 2.0d) + Math.pow(seedPointAndColor.tapPoint.y - seedPointAndColor2.tapPoint.y, 2.0d)) > this.pointSimilarityThreshold || seedPointAndColor.f7764r != seedPointAndColor2.f7764r || seedPointAndColor.f7763g != seedPointAndColor2.f7763g || seedPointAndColor.f7760b != seedPointAndColor2.f7760b || seedPointAndColor.meanY != seedPointAndColor2.meanY) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: a */
    public void a(ARDebugParams aRDebugParams) {
        this.ellipseStructElement = Imgproc.getStructuringElement(0, new Size((double) ARObservables.currentARDebugParams.getEdKernel(), (double) ARObservables.currentARDebugParams.getEdKernel()), this.anchor);
        this.lower = new Scalar((double) aRDebugParams.getHoughExtendLength(), (double) aRDebugParams.getHoughExtendLength(), (double) aRDebugParams.getHoughExtendLength());
        this.upper = new Scalar((double) aRDebugParams.getHoughExtendLength(), (double) aRDebugParams.getHoughExtendLength(), (double) aRDebugParams.getHoughExtendLength());
    }

    /* renamed from: b */
    public void b(ARDebugParams aRDebugParams) {
        this.ellipseStructElement = Imgproc.getStructuringElement(0, new Size((double) ARObservables.currentARDebugParams.getEdKernel(), (double) ARObservables.currentARDebugParams.getEdKernel()), this.anchor);
        this.lower = new Scalar((double) aRDebugParams.getHoughExtendLength(), (double) aRDebugParams.getHoughExtendLength(), (double) aRDebugParams.getHoughExtendLength());
        this.upper = new Scalar((double) aRDebugParams.getHoughExtendLength(), (double) aRDebugParams.getHoughExtendLength(), (double) aRDebugParams.getHoughExtendLength());
    }

    public void invalidateCache() {
        this.cacheImage = null;
        this.cachePoints = null;
        this.cacheResult = null;
    }

    public Bitmap predictAndColorMultiTapSingleMask(Bitmap bitmap, List<SeedPointAndColor> list) {
        return predictAndColorMultiTapSingleMask(bitmap, list, (List<LineEndPoints>) null);
    }

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
            Utils.matToBitmap(reColor, createBitmap); // Utils.a(reColor, createBitmap);
            bitmap2 = createBitmap;
        }
        if (!ARObservables.currentARDebugParams.isCannyFlag()) {
            this.cacheResult = bitmap2;
        }
        return bitmap2;
    }

    /*** DICCIONARIO ***/

    // Bitmap.createBitmap(reColor.b(), reColor.i(), Bitmap.Config.ARGB_8888); => Bitmap.createBitmap(reColor.cols(), reColor.rows(), Bitmap.Config.ARGB_8888);
    // mat.a() => mat.channels()
    // mat.k() => mat.type()
    // mat.b() => mat.cols()
    // mat4.a(mat4, CvType.CV_8UC3); => mat4.assignTo(mat4, CvType.CV_8UC3);
    // mat.j() => mat.size()
    // Mat.b(rows, cols, CvType.CV_8UC1); => Mat.zeros(rows, cols, CvType.CV_8UC1);
    // Mat mat5 = new Mat(mat.e(), mat.l(), a.d); => Mat mat5 = new Mat(mat.height(), mat.width(), CvType.CV_8UC4);
    // Core.a(arrayList, mat4); => Core.merge(arrayList, mat4);
    // Core.a(mat3).a => double[] dArr = Core.mean(mat3).val;
    // Core.a(mat4, arrayList); => Core.split(mat4, arrayList);
    // Core.a(clone, clone, i3, i3, i3, i3, 0, this.scalar_0); => Core.copyMakeBorder(clone, clone, i3, i3, i3, i3, 0, this.scalar_0);
    // Core.a(mat7, gVar2, mat7, mat9); => Core.add(mat7, Scalar, mat7, mat9);
    // Core.a(mat9, mat10); => Core.convertScaleAbs(mat9, mat10);
    // Core.c(clone, new Scalar(d3), a, 0); => Core.compare(mat3, new Scalar((double) i3), mat9, i);
    // Core.d(mat6, detectEdges, b2); => Core.subtract(mat6, detectEdges, b2);
    // Scalar c = Core.c(mat6, mat9); => Core.mean(mat6, mat9);
    // g => Scalar
    // d => Point
    // h => Size
    // e => Range
    // a => CvType
    // a.a => CvType.CV_8UC1, a.c = CvType.CV_8UC3, a.d = CvType.CV_8UC4
    // Imgproc.a(b2, b2, 3, this.ellipseStructElement, this.anchor, 3); => Imgproc.filter2D(mat, mat, 3, mat, this.anchor, 3);
    // Imgproc.a(mat2) => Imgproc.boundingRect(mat2)
    // Imgproc.a(mat, mat2, dVar, this.scalar_255, Imgproc.a(mat2), this.lower, this.upper, (i << 8) | 8 | 131072); => Imgproc.floodFill(mat, mat2, dVar, this.scalar_255, Imgproc.boundingRect(mat2), this.lower, this.upper, (i << 8) | 8 | 131072);
    // Imgproc.a(mat, a, mat.k(), (int) ); => Imgproc.Canny(mat, a, mat.type(), (int)x );
    // Imgproc.a(0, new h((double), (double)), this.anchor); => Imgproc.getStructuringElement(0, new Size((double), (double)), this.anchor);
    // Imgproc.a(mat2, next.startPoint, next.endPoint, new Scalar(255.0d), 1); => Imgproc.arrowedLine(mat2, next.startPoint, next.endPoint, new Scalar(255.0d), 1);
    // Imgproc.b(mat, mat, int); => Imgproc.medianBlur(mat, mat, int);
    // Point dVar -> dVar.a, dVar.b => dVar.x dVar.y


    public Mat reColor(Mat mat, Mat mat2, Mat mat3, List<SeedPointAndColor> list) {
        Mat mat4 = mat2;
        List<SeedPointAndColor> list2 = list;
        Mat mat5 = new Mat(mat.height(), mat.width(), CvType.CV_8UC4);
        mat2.channels();
        ArrayList<Mat> arrayList = new ArrayList<>();
        Core.split(mat4, arrayList);
        int i = 0;
        Mat mat6 = (Mat) arrayList.get(0);
        Mat mat7 = (Mat) arrayList.get(1);
        Mat mat8 = (Mat) arrayList.get(2);
        int i2 = 0;
        while (i2 < list.size()) {
            new Scalar(0.0d);
            Mat mat9 = new Mat(mat3.size(), 4);
            Mat mat10 = new Mat(mat3.size(), 4);
            int i3 = i2 + 1;
            Mat mat11 = mat8;
            Core.compare(mat3, new Scalar((double) i3), mat9, i);
            Core.bitwise_not(mat9, mat10);
            Scalar c = Core.mean(mat6, mat9);
            Mat mat12 = mat11;
            list2.get(i2).meanY = c.val[i];
            Mat mat13 = mat5;
            list2.get(i2).diffY = (int) (c.val[i] - ((double) list2.get(i2).f7765y));
            Scalar gVar = new Scalar((double) (-list2.get(i2).diffY));
            Scalar gVar2 = new Scalar((double) list2.get(i2).f7761cb);
            Scalar gVar3 = new Scalar((double) list2.get(i2).f7762cr);
            Core.add(mat6, gVar, mat6, mat9);
            Core.bitwise_and(mat7, mat10, mat7);
            mat8 = mat12;
            Core.bitwise_and(mat8, mat10, mat8);
            Core.add(mat7, gVar2, mat7, mat9);
            Core.add(mat8, gVar3, mat8, mat9);
            mat5 = mat13;
            i2 = i3;
            i = 0;
        }
        arrayList.set(i, mat6);
        arrayList.set(1, mat7);
        arrayList.set(2, mat8);
        Core.merge(arrayList, mat4);
        mat4.assignTo(mat4, CvType.CV_8UC3);
        Imgproc.applyColorMap(mat4, mat4, 38);
        Mat mat14 = mat5;
        Imgproc.applyColorMap(mat4, mat14, 2);
        return mat14;
    }

    public void setIsDarkWallShiftFlag(boolean z) {
        this.isDarkWallShiftEnabled = z;
    }

    public void setIsLightWallShiftFlag(boolean z) {
        this.isLightWallShiftEnabled = z;
    }
    /*
    public static final int ANCHOR_POS_X = -1;
    public static final int ANCHOR_POS_Y = -1;
    public static final int BORDER_SIZE = 1;
    public static final int SCALAR_0_VECT = 0;
    public static final int SCALAR_255_VECT = 255;
    public static final int SCALE_ALL_VALUE = 1;
    public static final int SCALE_VECT_0 = 20;
    public static final int SCALE_VECT_1 = 20;
    public static final int SCALE_VECT_2 = 20;
    public static final int SIZE_HEIGHT = 3;
    public static final int SIZE_WIDTH = 3;
    public int BorderSize;
    public String TAG = "SegmentationModel";
    public final Point anchor = new Point(-1.0d, -1.0d);
    public Mat cacheImage;
    public Mat cacheMask;
    public List<SeedPointAndColor> cachePoints;
    public Bitmap cacheResult;
    public Mat cannyEdges;
    
    // public Mat dilationKernel = new Mat(new Size(3.0d, 3.0d), CvType.CV_16F a, Scalar.all(1.0d));
    public Mat dilationKernel = new Mat(new Size(3.0d, 3.0d), CvType.CV_16F, Scalar.all(1.0d));
    
    
    //public Mat ellipseStructElement = Imgproc.a(2, new Size((double) ARObservables.currentARDebugParams.getEdKernel(), (double) ARObservables.currentARDebugParams.getEdKernel()), this.anchor);
    public Mat ellipseStructElement = Imgproc.getStructuringElement(0, new Size(5.0d, 5.0d), this.anchor);
    
    public Mat erosionKernel = new Mat(new Size(3.0d, 3.0d), CvType.CV_16F, Scalar.all(1.0d));
    public float imageSimilarityThreshold;
    public boolean isDarkWallShiftEnabled;
    public boolean isLightWallShiftEnabled;
    public Scalar lower;
    public int mCurrentNValue;
    public boolean mEnableYShift;
    public double pointSimilarityThreshold;
    public final Scalar scalar_0;
    public final Scalar scalar_255;
    public Scalar upper;

    @SuppressLint({"CheckResult"})
    public Segmentor2ImplFinal(Boolean bool, Boolean bool2, Boolean bool3, int i, float f, double Point) {
        //ARObservables.arDebugParamsObservable.Point(new b(this));
        this.ellipseStructElement = Imgproc.a(2, new Size((double) ARObservables.currentARDebugParams.getEdKernel(), (double) ARObservables.currentARDebugParams.getEdKernel()), this.anchor);
        //.arDebugParamsObservable.Point(new a(this));
        this.lower = new Scalar(20.0d, 20.0d, 20.0d);
        this.upper = this.lower;
        this.scalar_0 = new Scalar(0.0d);
        this.scalar_255 = new Scalar(255.0d);
        this.BorderSize = 1;
        this.imageSimilarityThreshold = f;
        this.pointSimilarityThreshold = Point;
        this.mEnableYShift = bool.booleanValue();
        this.isDarkWallShiftEnabled = bool2.booleanValue();
        this.isLightWallShiftEnabled = bool3.booleanValue();
        this.mCurrentNValue = i;
        Core.setNumThreads(6);
    }

    private int clamp(int i) {
        if (i < 0) {
            return 0;
        }
        if (i > 255) {
            return 255;
        }
        return i;
    }

    private Mat detectEdges(Mat mat, List<LineEndPoints> list) {
        Mat mat2 = new Mat(mat.size(), CvType.CV_16F);
        double cannyMin = (double) ARObservables.currentARDebugParams.getCannyMin();
        double cannyMax = (double) ARObservables.currentARDebugParams.getCannyMax();
        Imgproc.accumulateWeighted(mat, mat, ARObservables.currentARDebugParams.getBlurKernel());
        if (ARObservables.currentARDebugParams.isOTSUFlag()) {
            Mat a2 = Mat.zeros(mat.size(), mat.type());
            if (ARObservables.currentARDebugParams.getImageExposure() != 0.0f) {
                Imgproc.threshold(mat, a2, mat.type(), (int) ARObservables.currentARDebugParams.getImageExposure());
            }
            Core.bitwise_not(mat, a2, mat2);
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
            for (LineEndPoints next : list) {
                Imgproc.arrowedLine(mat2, next.startPoint, next.endPoint, new Scalar(255.0d), 1);
            }
        }
        return mat2;
    }*/
}