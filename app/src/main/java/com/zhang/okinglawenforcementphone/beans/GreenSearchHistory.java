package com.zhang.okinglawenforcementphone.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/5/18/018.
 */
@Entity
public class GreenSearchHistory {
    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String searchText;
    private Long time;
    @Generated(hash = 298392090)
    public GreenSearchHistory(Long id, String userId, String searchText,
            Long time) {
        this.id = id;
        this.userId = userId;
        this.searchText = searchText;
        this.time = time;
    }
    @Generated(hash = 918844437)
    public GreenSearchHistory() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getSearchText() {
        return this.searchText;
    }
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
}
