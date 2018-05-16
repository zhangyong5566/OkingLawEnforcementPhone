package com.zhang.okinglawenforcementphone.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2018/4/16.
 */

@Entity
public class GreenLocation {
    @Id()
    private Long id;
    private String longitude;
    private String latitude;
    @Generated(hash = 1810241318)
    public GreenLocation(Long id, String longitude, String latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    @Generated(hash = 248498775)
    public GreenLocation() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLongitude() {
        return this.longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLatitude() {
        return this.latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "GreenLocation{" +
                "id=" + id +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
