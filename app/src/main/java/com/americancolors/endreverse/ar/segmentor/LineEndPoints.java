package com.americancolors.endreverse.ar.segmentor;

import org.opencv.core.Point;

public class LineEndPoints {
    public Point endPoint;
    public Point startPoint;

    public LineEndPoints() {
        this.startPoint = new Point();
        this.endPoint = new Point();
    }

    public LineEndPoints(double d, double d2, double d3, double d4) {
        this.startPoint = new Point(d, d2);
        this.endPoint = new Point(d3, d4);
    }

    public LineEndPoints(Point dVar, Point dVar2) {
        this.startPoint = dVar;
        this.endPoint = dVar2;
    }
}
