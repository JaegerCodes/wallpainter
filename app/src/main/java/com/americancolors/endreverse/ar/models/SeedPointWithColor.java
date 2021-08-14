package com.americancolors.endreverse.ar.models;

public class SeedPointWithColor {

    /* renamed from: b */
    public int f7757b;
    public String colorUid;

    /* renamed from: g */
    public int f7758g;

    /* renamed from: r */
    public int f7759r;
    public double xPos;
    public double yPos;

    public SeedPointWithColor(double d, double d2, int i, int i2, int i3) {
        this.xPos = d;
        this.yPos = d2;
        this.f7759r = i;
        this.f7758g = i2;
        this.f7757b = i3;
    }

    public SeedPointWithColor(double d, double d2, String str) {
        this.xPos = d;
        this.yPos = d2;
        this.colorUid = str;
    }

    public int getB() {
        return this.f7757b;
    }

    public String getColorUid() {
        return this.colorUid;
    }

    public int getG() {
        return this.f7758g;
    }

    public int getR() {
        return this.f7759r;
    }

    public double getXPos() {
        return this.xPos;
    }

    public double getYPos() {
        return this.yPos;
    }

    public void setB(int i) {
        this.f7757b = i;
    }

    public void setColorUid(String str) {
        this.colorUid = str;
    }

    public void setG(int i) {
        this.f7758g = i;
    }

    public void setR(int i) {
        this.f7759r = i;
    }

    public void setXPos(double d) {
        this.xPos = d;
    }

    public void setYPos(double d) {
        this.yPos = d;
    }
}
