package com.zhang.okinglawenforcementphone.beans;


import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */
@Entity
public class GreenMissionTask {
    @Id(autoincrement = true) //自增
    private Long id; //主键
    private String approved_person;
    private String approved_person_name;
    private Long approved_time;
    private Long begin_time;
    private Long create_time;
    private Long delivery_time;
    private Long end_time;
    private Long execute_end_time;
    private Long execute_start_time;
    private String fbdw;
    private String fbr;
    private String taskid;
    private String jjcd;
    private String jsdw;
    private String jsr;
    private String publisher;
    private String publisher_name;
    private String receiver;
    private String receiver_name;
    private String rwly;
    private String rwqyms;
    private String spr;
    private String spyj;
    private String status;
    private String task_area;
    private String task_content;
    private String task_name;
    private int task_type;
    private String typename;
    private String typeoftask;
    private String userid;
    private int drawLaLoType;                       //绘制类型（圆4或者多边形5）
    private String mcoordinateJson;             //经纬度范围
    @ToMany(referencedJoinProperty = "greenMemberId")
    private List<GreenMember> members;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 499486839)
    private transient GreenMissionTaskDao myDao;
    @Generated(hash = 1087053633)
    public GreenMissionTask(Long id, String approved_person,
            String approved_person_name, Long approved_time, Long begin_time,
            Long create_time, Long delivery_time, Long end_time,
            Long execute_end_time, Long execute_start_time, String fbdw, String fbr,
            String taskid, String jjcd, String jsdw, String jsr, String publisher,
            String publisher_name, String receiver, String receiver_name,
            String rwly, String rwqyms, String spr, String spyj, String status,
            String task_area, String task_content, String task_name, int task_type,
            String typename, String typeoftask, String userid, int drawLaLoType,
            String mcoordinateJson) {
        this.id = id;
        this.approved_person = approved_person;
        this.approved_person_name = approved_person_name;
        this.approved_time = approved_time;
        this.begin_time = begin_time;
        this.create_time = create_time;
        this.delivery_time = delivery_time;
        this.end_time = end_time;
        this.execute_end_time = execute_end_time;
        this.execute_start_time = execute_start_time;
        this.fbdw = fbdw;
        this.fbr = fbr;
        this.taskid = taskid;
        this.jjcd = jjcd;
        this.jsdw = jsdw;
        this.jsr = jsr;
        this.publisher = publisher;
        this.publisher_name = publisher_name;
        this.receiver = receiver;
        this.receiver_name = receiver_name;
        this.rwly = rwly;
        this.rwqyms = rwqyms;
        this.spr = spr;
        this.spyj = spyj;
        this.status = status;
        this.task_area = task_area;
        this.task_content = task_content;
        this.task_name = task_name;
        this.task_type = task_type;
        this.typename = typename;
        this.typeoftask = typeoftask;
        this.userid = userid;
        this.drawLaLoType = drawLaLoType;
        this.mcoordinateJson = mcoordinateJson;
    }
    @Generated(hash = 2006901514)
    public GreenMissionTask() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getApproved_person() {
        return this.approved_person;
    }
    public void setApproved_person(String approved_person) {
        this.approved_person = approved_person;
    }
    public String getApproved_person_name() {
        return this.approved_person_name;
    }
    public void setApproved_person_name(String approved_person_name) {
        this.approved_person_name = approved_person_name;
    }
    public Long getApproved_time() {
        return this.approved_time;
    }
    public void setApproved_time(Long approved_time) {
        this.approved_time = approved_time;
    }
    public Long getBegin_time() {
        return this.begin_time;
    }
    public void setBegin_time(Long begin_time) {
        this.begin_time = begin_time;
    }
    public Long getCreate_time() {
        return this.create_time;
    }
    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }
    public Long getDelivery_time() {
        return this.delivery_time;
    }
    public void setDelivery_time(Long delivery_time) {
        this.delivery_time = delivery_time;
    }
    public Long getEnd_time() {
        return this.end_time;
    }
    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }
    public Long getExecute_end_time() {
        return this.execute_end_time;
    }
    public void setExecute_end_time(Long execute_end_time) {
        this.execute_end_time = execute_end_time;
    }
    public Long getExecute_start_time() {
        return this.execute_start_time;
    }
    public void setExecute_start_time(Long execute_start_time) {
        this.execute_start_time = execute_start_time;
    }
    public String getFbdw() {
        return this.fbdw;
    }
    public void setFbdw(String fbdw) {
        this.fbdw = fbdw;
    }
    public String getFbr() {
        return this.fbr;
    }
    public void setFbr(String fbr) {
        this.fbr = fbr;
    }
    public String getTaskid() {
        return this.taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
    public String getJjcd() {
        return this.jjcd;
    }
    public void setJjcd(String jjcd) {
        this.jjcd = jjcd;
    }
    public String getJsdw() {
        return this.jsdw;
    }
    public void setJsdw(String jsdw) {
        this.jsdw = jsdw;
    }
    public String getJsr() {
        return this.jsr;
    }
    public void setJsr(String jsr) {
        this.jsr = jsr;
    }
    public String getPublisher() {
        return this.publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public String getPublisher_name() {
        return this.publisher_name;
    }
    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }
    public String getReceiver() {
        return this.receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getReceiver_name() {
        return this.receiver_name;
    }
    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }
    public String getRwly() {
        return this.rwly;
    }
    public void setRwly(String rwly) {
        this.rwly = rwly;
    }
    public String getRwqyms() {
        return this.rwqyms;
    }
    public void setRwqyms(String rwqyms) {
        this.rwqyms = rwqyms;
    }
    public String getSpr() {
        return this.spr;
    }
    public void setSpr(String spr) {
        this.spr = spr;
    }
    public String getSpyj() {
        return this.spyj;
    }
    public void setSpyj(String spyj) {
        this.spyj = spyj;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTask_area() {
        return this.task_area;
    }
    public void setTask_area(String task_area) {
        this.task_area = task_area;
    }
    public String getTask_content() {
        return this.task_content;
    }
    public void setTask_content(String task_content) {
        this.task_content = task_content;
    }
    public String getTask_name() {
        return this.task_name;
    }
    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }
    public int getTask_type() {
        return this.task_type;
    }
    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }
    public String getTypename() {
        return this.typename;
    }
    public void setTypename(String typename) {
        this.typename = typename;
    }
    public String getTypeoftask() {
        return this.typeoftask;
    }
    public void setTypeoftask(String typeoftask) {
        this.typeoftask = typeoftask;
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public int getDrawLaLoType() {
        return this.drawLaLoType;
    }
    public void setDrawLaLoType(int drawLaLoType) {
        this.drawLaLoType = drawLaLoType;
    }
    public String getMcoordinateJson() {
        return this.mcoordinateJson;
    }
    public void setMcoordinateJson(String mcoordinateJson) {
        this.mcoordinateJson = mcoordinateJson;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2076162059)
    public List<GreenMember> getMembers() {
        if (members == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GreenMemberDao targetDao = daoSession.getGreenMemberDao();
            List<GreenMember> membersNew = targetDao
                    ._queryGreenMissionTask_Members(id);
            synchronized (this) {
                if (members == null) {
                    members = membersNew;
                }
            }
        }
        return members;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1358688666)
    public synchronized void resetMembers() {
        members = null;
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
        return "GreenMissionTask{" +
                "id=" + id +
                ", approved_person='" + approved_person + '\'' +
                ", approved_person_name='" + approved_person_name + '\'' +
                ", approved_time=" + approved_time +
                ", begin_time=" + begin_time +
                ", create_time=" + create_time +
                ", delivery_time=" + delivery_time +
                ", end_time=" + end_time +
                ", execute_end_time=" + execute_end_time +
                ", execute_start_time=" + execute_start_time +
                ", fbdw='" + fbdw + '\'' +
                ", fbr='" + fbr + '\'' +
                ", taskid='" + taskid + '\'' +
                ", jjcd='" + jjcd + '\'' +
                ", jsdw='" + jsdw + '\'' +
                ", jsr='" + jsr + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publisher_name='" + publisher_name + '\'' +
                ", receiver='" + receiver + '\'' +
                ", receiver_name='" + receiver_name + '\'' +
                ", rwly='" + rwly + '\'' +
                ", rwqyms='" + rwqyms + '\'' +
                ", spr='" + spr + '\'' +
                ", spyj='" + spyj + '\'' +
                ", status='" + status + '\'' +
                ", task_area='" + task_area + '\'' +
                ", task_content='" + task_content + '\'' +
                ", task_name='" + task_name + '\'' +
                ", task_type=" + task_type +
                ", typename='" + typename + '\'' +
                ", typeoftask='" + typeoftask + '\'' +
                ", userid='" + userid + '\'' +
                ", drawLaLoType=" + drawLaLoType +
                ", mcoordinateJson='" + mcoordinateJson + '\'' +
                ", members=" + members +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                '}';
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 256028144)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGreenMissionTaskDao() : null;
    }
}
