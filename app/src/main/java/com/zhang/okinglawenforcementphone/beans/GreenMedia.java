package com.zhang.okinglawenforcementphone.beans;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.sql.Date;

/**
 * Created by Administrator on 2018/3/27.
 */
@Entity
public class GreenMedia {
    @Id(autoincrement = true)
    private Long id;
    private Long greenMissionLogId;
    private Long time;
    private String path;
    private String userid;
    private String taskid;
    @Transient
    private GreenLocation souceLocation;
    private Integer type;               //1表示日志图片  2表示视频  3表示语音   4签名图片
    private Long greenGreenLocationId;
    @ToOne(joinProperty = "greenGreenLocationId")
    private GreenLocation location;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1532472546)
    private transient GreenMediaDao myDao;
    @Generated(hash = 24618416)
    public GreenMedia(Long id, Long greenMissionLogId, Long time, String path,
            String userid, String taskid, Integer type, Long greenGreenLocationId) {
        this.id = id;
        this.greenMissionLogId = greenMissionLogId;
        this.time = time;
        this.path = path;
        this.userid = userid;
        this.taskid = taskid;
        this.type = type;
        this.greenGreenLocationId = greenGreenLocationId;
    }
    @Generated(hash = 1581839435)
    public GreenMedia() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getGreenMissionLogId() {
        return this.greenMissionLogId;
    }
    public void setGreenMissionLogId(Long greenMissionLogId) {
        this.greenMissionLogId = greenMissionLogId;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getTaskid() {
        return this.taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
    public Integer getType() {
        return this.type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Long getGreenGreenLocationId() {
        return this.greenGreenLocationId;
    }
    public void setGreenGreenLocationId(Long greenGreenLocationId) {
        this.greenGreenLocationId = greenGreenLocationId;
    }

    public GreenLocation getSouceLocation() {
        return souceLocation;
    }

    public void setSouceLocation(GreenLocation souceLocation) {
        this.souceLocation = souceLocation;
    }

    @Generated(hash = 1068795426)
    private transient Long location__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2016527076)
    public GreenLocation getLocation() {
        Long __key = this.greenGreenLocationId;
        if (location__resolvedKey == null || !location__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GreenLocationDao targetDao = daoSession.getGreenLocationDao();
            GreenLocation locationNew = targetDao.load(__key);
            synchronized (this) {
                location = locationNew;
                location__resolvedKey = __key;
            }
        }
        return location;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 592716253)
    public void setLocation(GreenLocation location) {
        synchronized (this) {
            this.location = location;
            greenGreenLocationId = location == null ? null : location.getId();
            location__resolvedKey = greenGreenLocationId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    @Override
    public String toString() {
        return "GreenMedia{" +
                "id=" + id +
                ", greenMissionLogId=" + greenMissionLogId +
                ", time=" + time +
                ", path='" + path + '\'' +
                ", userid='" + userid + '\'' +
                ", taskid='" + taskid + '\'' +
                ", souceLocation=" + souceLocation +
                ", type=" + type +
                ", greenGreenLocationId=" + greenGreenLocationId +
                ", location=" + location +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                ", location__resolvedKey=" + location__resolvedKey +
                '}';
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1652320658)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGreenMediaDao() : null;
    }
}



