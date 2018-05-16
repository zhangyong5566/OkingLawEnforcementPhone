package com.zhang.okinglawenforcementphone.beans;

import com.amap.api.maps.model.LatLng;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10/010.
 */

public class LatLngListOV {
    private List<LatLng> latLngs;
    private float left;
    private float right;
    private float top;
    private float bottom;
    private int type;
    private float zoom;
    private LatLng centerLatLng;
    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public LatLng getCenterLatLng() {
        return centerLatLng;
    }

    public void setCenterLatLng(LatLng centerLatLng) {
        this.centerLatLng = centerLatLng;
    }

    @Override
    public String toString() {
        return "LatLngListOV{" +
                "latLngs=" + latLngs +
                ", left=" + left +
                ", right=" + right +
                ", top=" + top +
                ", bottom=" + bottom +
                ", type=" + type +
                ", zoom=" + zoom +
                ", centerLatLng=" + centerLatLng +
                '}';
    }
}
