package com.americancolors.endreverse.ar.models;

/* renamed from: com.akzonobel.ar.models.RecColor */
public class RecColor {
    public String collectId;
    public String colorName;
    public String colorUid;

    public RecColor(String str, String str2) {
        this.colorUid = str;
        this.collectId = str2;
    }

    public RecColor(String str, String str2, String str3) {
        this.colorUid = str;
        this.collectId = str2;
        this.colorName = str3;
    }

    public String getCollectId() {
        return this.collectId;
    }

    public String getColorName() {
        return this.colorName;
    }

    public String getColorUid() {
        return this.colorUid;
    }

    public void setCollectId(String str) {
        this.collectId = str;
    }

    public void setColorName(String str) {
        this.colorName = str;
    }

    public void setColorUid(String str) {
        this.colorUid = str;
    }
}
