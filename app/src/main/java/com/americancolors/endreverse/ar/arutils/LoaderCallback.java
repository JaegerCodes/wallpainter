package com.americancolors.endreverse.ar.arutils;

import android.content.Context;

import org.json.JSONObject;

import java.util.Objects;

public class LoaderCallback {

    /* renamed from: a */
    public String aa = "";

    /* renamed from: b */
    public String bb = "";

    /* renamed from: c */
    public String cc = "";

    /* renamed from: d */
    public String dd = "";


    public LoaderCallback(JSONObject jSONObject, String aa, String bb, String cc, String dd) {
        this.aa = aa;
        this.bb = bb;
        this.cc = cc;
        this.dd = dd;
    }

    public LoaderCallback(Context applicationContext) {
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || LoaderCallback.class != obj.getClass() || !super.equals(obj)) {
            return false;
        }
        LoaderCallback loaderCallbackVar = (LoaderCallback) obj;
        if (!Objects.equals(this.aa, loaderCallbackVar.aa)) {
            return false;
        }
        if (!Objects.equals(this.bb, loaderCallbackVar.bb)) {
            return false;
        }
        if (!Objects.equals(this.cc, loaderCallbackVar.cc)) {
            return false;
        }
        return Objects.equals(this.dd, loaderCallbackVar.dd);
    }

    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        String str = this.aa;
        int i = 0;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.bb;
        int hashCode3 = (hashCode2 + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.cc;
        int hashCode4 = (hashCode3 + (str3 != null ? str3.hashCode() : 0)) * 31;
        String str4 = this.dd;
        if (str4 != null) {
            i = str4.hashCode();
        }
        return hashCode4 + i;
    }

    /* renamed from: m */
    public String mo8291m() {
        return this.aa;
    }

    /* renamed from: n */
    public String mo8292n() {
        return this.bb;
    }

    /* renamed from: o */
    public String mo8293o() {
        return this.cc;
    }

    /* renamed from: p */
    public String mo8294p() {
        return this.dd;
    }

}
