package com.zhang.okinglawenforcementphone.beans;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23.
 * 巡查日志数据表
 */
@Entity
public class GreenMissionLog {
    @Id(autoincrement = true) //自增
    private Long id; //主键
    private String server_id;
    private String task_id;
    private String name;
    private String post;
    private String id_card;
    private String other_part;
    private int other_person;
    private String weather;
    private String equipment;
    private String addr;
    private String time;
    private int plan;
    private int item;
    private int type;
    private String area;
    private String route;
    private String patrol;
    private int status;
    private String deal;
    private String result;
    private String dzyj;
    private String flowTagPos;
    private String locJson;
    @ToMany(referencedJoinProperty = "greenMissionLogId")
    private List<GreenMedia> greenMedia;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 771362723)
    private transient GreenMissionLogDao myDao;
    @Generated(hash = 552827286)
    public GreenMissionLog(Long id, String server_id, String task_id, String name,
            String post, String id_card, String other_part, int other_person,
            String weather, String equipment, String addr, String time, int plan,
            int item, int type, String area, String route, String patrol,
            int status, String deal, String result, String dzyj, String flowTagPos,
            String locJson) {
        this.id = id;
        this.server_id = server_id;
        this.task_id = task_id;
        this.name = name;
        this.post = post;
        this.id_card = id_card;
        this.other_part = other_part;
        this.other_person = other_person;
        this.weather = weather;
        this.equipment = equipment;
        this.addr = addr;
        this.time = time;
        this.plan = plan;
        this.item = item;
        this.type = type;
        this.area = area;
        this.route = route;
        this.patrol = patrol;
        this.status = status;
        this.deal = deal;
        this.result = result;
        this.dzyj = dzyj;
        this.flowTagPos = flowTagPos;
        this.locJson = locJson;
    }
    @Generated(hash = 569752963)
    public GreenMissionLog() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getServer_id() {
        return this.server_id;
    }
    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }
    public String getTask_id() {
        return this.task_id;
    }
    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPost() {
        return this.post;
    }
    public void setPost(String post) {
        this.post = post;
    }
    public String getId_card() {
        return this.id_card;
    }
    public void setId_card(String id_card) {
        this.id_card = id_card;
    }
    public String getOther_part() {
        return this.other_part;
    }
    public void setOther_part(String other_part) {
        this.other_part = other_part;
    }
    public int getOther_person() {
        return this.other_person;
    }
    public void setOther_person(int other_person) {
        this.other_person = other_person;
    }
    public String getWeather() {
        return this.weather;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }
    public String getEquipment() {
        return this.equipment;
    }
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
    public String getAddr() {
        return this.addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getPlan() {
        return this.plan;
    }
    public void setPlan(int plan) {
        this.plan = plan;
    }
    public int getItem() {
        return this.item;
    }
    public void setItem(int item) {
        this.item = item;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getArea() {
        return this.area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getRoute() {
        return this.route;
    }
    public void setRoute(String route) {
        this.route = route;
    }
    public String getPatrol() {
        return this.patrol;
    }
    public void setPatrol(String patrol) {
        this.patrol = patrol;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getDeal() {
        return this.deal;
    }
    public void setDeal(String deal) {
        this.deal = deal;
    }
    public String getResult() {
        return this.result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getDzyj() {
        return this.dzyj;
    }
    public void setDzyj(String dzyj) {
        this.dzyj = dzyj;
    }
    public String getFlowTagPos() {
        return this.flowTagPos;
    }
    public void setFlowTagPos(String flowTagPos) {
        this.flowTagPos = flowTagPos;
    }
    public String getLocJson() {
        return this.locJson;
    }
    public void setLocJson(String locJson) {
        this.locJson = locJson;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1521493460)
    public List<GreenMedia> getGreenMedia() {
        if (greenMedia == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GreenMediaDao targetDao = daoSession.getGreenMediaDao();
            List<GreenMedia> greenMediaNew = targetDao
                    ._queryGreenMissionLog_GreenMedia(id);
            synchronized (this) {
                if (greenMedia == null) {
                    greenMedia = greenMediaNew;
                }
            }
        }
        return greenMedia;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1415851636)
    public synchronized void resetGreenMedia() {
        greenMedia = null;
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
        return "GreenMissionLog{" +
                "id=" + id +
                ", server_id='" + server_id + '\'' +
                ", task_id='" + task_id + '\'' +
                ", name='" + name + '\'' +
                ", post='" + post + '\'' +
                ", id_card='" + id_card + '\'' +
                ", other_part='" + other_part + '\'' +
                ", other_person=" + other_person +
                ", weather='" + weather + '\'' +
                ", equipment='" + equipment + '\'' +
                ", addr='" + addr + '\'' +
                ", time='" + time + '\'' +
                ", plan=" + plan +
                ", item=" + item +
                ", type=" + type +
                ", area='" + area + '\'' +
                ", route='" + route + '\'' +
                ", patrol='" + patrol + '\'' +
                ", status=" + status +
                ", deal='" + deal + '\'' +
                ", result='" + result + '\'' +
                ", dzyj='" + dzyj + '\'' +
                ", flowTagPos='" + flowTagPos + '\'' +
                ", locJson='" + locJson + '\'' +
                ", greenMedia=" + greenMedia +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                '}';
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1652000962)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGreenMissionLogDao() : null;
    }
}
