package com.ilife.shining.movingtrack.model;

/**
 * file：       LocationInfo
 * Description：TODO
 * Author：     Shining Chen
 * Create Date：2016/1/16
 *
 *getSpeed()
 获取当前速度 单位：米/秒 仅在AMapLocation.getProvider()是gps时有效
 */
public class LocationInfo {
    private double latitude, longitude;
    private long time;
    private float speed;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
