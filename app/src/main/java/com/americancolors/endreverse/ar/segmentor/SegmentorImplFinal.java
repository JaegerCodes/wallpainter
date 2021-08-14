package com.americancolors.endreverse.ar.segmentor;

import android.graphics.Bitmap;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class SegmentorImplFinal implements Segmentor {
    public static final int ANCHOR_POS_X = -1;
    public static final int ANCHOR_POS_Y = -1;
    public static final int BORDER_SIZE = 1;
    public static int DELAY_IN_MS = 62;
    public static int EXTEND_LENGTH = 14;
    public static final int FPS_CONTROLLER_0FPS = 0;
    public static final int FPS_CONTROLLER_16FPS = 62;
    public static final int FPS_CONTROLLER_20FPS = 50;
    public static final int FPS_CONTROLLER_25FPS = 40;
    public static final int IMAGE_RESIZE_FRAME_WIDTH = 600;
    public static final int IMAGE_SIMI_THRESHOLD = 3;
    public static final int KERNEL_SIZE_HEIGHT = 5;
    public static final int KERNEL_SIZE_WIDTH = 5;
    public static final double KSIZE_WIDHT_HEIGHT = 11.0d;
    public static final double LOWER_THRESHOLD = 20.0d;
    public static final int LOW_LIGHT_THRESHOLD = 90;
    public static final int MEDIAN_BLUR_KERNEL = 3;
    public static final int NO_OF_THREADS = 6;
    public static final int POINT_SIMI_THRESHOLD = 3;
    public static final int SCALAR_0_VECT = 0;
    public static final int SCALAR_255_VECT = 255;
    public static final int SCALE_ALL_VALUE = 1;
    public static final int SIZE_HEIGHT = 3;
    public static final int SIZE_WIDTH = 3;
    public static final double UPPER_THRESHOLD = 25.0d;
    public int BorderSize = 1;
    public String TAG = "SegmentationModel";
    public final Point anchor = new Point(-1.0d, 1.0d);
    public Mat cacheCanny;
    public Mat cacheImage;
    public List<SeedPointAndColor> cachePoints;
    public Bitmap cacheResult;
    public Mat dilationKernel = new Mat(new Size(3.0d, 3.0d), CvType.CV_16F, Scalar.all(1.0d));
    public Mat ellipseStructElement = Imgproc.getStructuringElement(0, new Size(5.0d, 5.0d), this.anchor);
    public Mat erosionKernel = new Mat(new Size(3.0d, 3.0d), CvType.CV_16F, Scalar.all(1.0d));
    public float imageSimilarityThreshold = 3.0f;
    public int mCurrentNValue;
    public boolean mEnableYShift;
    public int mResizedImageWidth;
    public double pointSimilarityThreshold = 3.0d;
    public final Scalar scalar_0 = new Scalar(0.0d);
    public final Scalar scalar_255 = new Scalar(255.0d);

    public SegmentorImplFinal(Boolean bool, Boolean bool2, Boolean bool3, int i, float f, double d) {
        Core.setNumThreads(6);
        this.mResizedImageWidth = IMAGE_RESIZE_FRAME_WIDTH;
    }


    private boolean isSimilarImage(Mat mat) {
        Mat mat2 = this.cacheImage;
        if (mat2 == null || !mat2.size().equals(mat.size())) {
            return false;
        }
        Mat mat3 = new Mat(mat.size(), CvType.CV_32S);
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
            if (Math.sqrt(Math.pow(seedPointAndColor.tapPoint.x - seedPointAndColor2.tapPoint.x, 2.0d) + Math.pow(seedPointAndColor.tapPoint.y - seedPointAndColor2.tapPoint.y, 2.0d)) > this.pointSimilarityThreshold || seedPointAndColor.f7764r != seedPointAndColor2.f7764r || seedPointAndColor.f7763g != seedPointAndColor2.f7763g || seedPointAndColor.f7760b != seedPointAndColor2.f7760b || seedPointAndColor.meanY != seedPointAndColor2.meanY) {
                return false;
            }
        }
        return true;
    }

    public void invalidateCache() {
        this.cacheImage = null;
        this.cachePoints = null;
        this.cacheResult = null;
    }

    public Bitmap predictAndColorMultiTapSingleMask(Bitmap bitmap, List<SeedPointAndColor> list) {
        return predictAndColorMultiTapSingleMask(bitmap, list, (List<LineEndPoints>) null);
    }

    @Override
    public Bitmap predictAndColorMultiTapSingleMask(Bitmap bitmap, List<SeedPointAndColor> list, List<LineEndPoints> list2) {
        return null;
    }

    public void setIsDarkWallShiftFlag(boolean z) {
    }

    public void setIsLightWallShiftFlag(boolean z) {
    }
}
