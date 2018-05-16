package com.zhang.okinglawenforcementphone.beans;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */
@Entity
public class GreenEvidence {
    @Id(autoincrement = true)
    private Long id;
    private Long greenCaseId;
    private String ZJID;
    private String AJID;
    private String ZJLX;
    private String ZJMC;
    private String ZJLY;
    private String ZJNR;
    private String SL;
    private Long CJSJ;
    private String CJR;
    private String CJDD;
    private String JZR;
    private String DW;
    private String BZ;
    private String SCR;
    private Long SCSJ;
    private String ZT;
    private String WSID;
    private String LXMC;
    private String ZJLYMC;
    private String LRSJ;
    private String YS;

    private String Otype;
    private Boolean isUpload = false;

    @ToMany(referencedJoinProperty = "greenMissionLogId")
    private List<GreenMedia> greenMedia;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1732101322)
    private transient GreenEvidenceDao myDao;

    @Generated(hash = 1279120131)
    public GreenEvidence(Long id, Long greenCaseId, String ZJID, String AJID,
            String ZJLX, String ZJMC, String ZJLY, String ZJNR, String SL,
            Long CJSJ, String CJR, String CJDD, String JZR, String DW, String BZ,
            String SCR, Long SCSJ, String ZT, String WSID, String LXMC,
            String ZJLYMC, String LRSJ, String YS, String Otype, Boolean isUpload) {
        this.id = id;
        this.greenCaseId = greenCaseId;
        this.ZJID = ZJID;
        this.AJID = AJID;
        this.ZJLX = ZJLX;
        this.ZJMC = ZJMC;
        this.ZJLY = ZJLY;
        this.ZJNR = ZJNR;
        this.SL = SL;
        this.CJSJ = CJSJ;
        this.CJR = CJR;
        this.CJDD = CJDD;
        this.JZR = JZR;
        this.DW = DW;
        this.BZ = BZ;
        this.SCR = SCR;
        this.SCSJ = SCSJ;
        this.ZT = ZT;
        this.WSID = WSID;
        this.LXMC = LXMC;
        this.ZJLYMC = ZJLYMC;
        this.LRSJ = LRSJ;
        this.YS = YS;
        this.Otype = Otype;
        this.isUpload = isUpload;
    }

    @Generated(hash = 283023486)
    public GreenEvidence() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGreenCaseId() {
        return this.greenCaseId;
    }

    public void setGreenCaseId(Long greenCaseId) {
        this.greenCaseId = greenCaseId;
    }

    public String getZJID() {
        return this.ZJID;
    }

    public void setZJID(String ZJID) {
        this.ZJID = ZJID;
    }

    public String getAJID() {
        return this.AJID;
    }

    public void setAJID(String AJID) {
        this.AJID = AJID;
    }

    public String getZJLX() {
        return this.ZJLX;
    }

    public void setZJLX(String ZJLX) {
        this.ZJLX = ZJLX;
    }

    public String getZJMC() {
        return this.ZJMC;
    }

    public void setZJMC(String ZJMC) {
        this.ZJMC = ZJMC;
    }

    public String getZJLY() {
        return this.ZJLY;
    }

    public void setZJLY(String ZJLY) {
        this.ZJLY = ZJLY;
    }

    public String getZJNR() {
        return this.ZJNR;
    }

    public void setZJNR(String ZJNR) {
        this.ZJNR = ZJNR;
    }

    public String getSL() {
        return this.SL;
    }

    public void setSL(String SL) {
        this.SL = SL;
    }

    public Long getCJSJ() {
        return this.CJSJ;
    }

    public void setCJSJ(Long CJSJ) {
        this.CJSJ = CJSJ;
    }

    public String getCJR() {
        return this.CJR;
    }

    public void setCJR(String CJR) {
        this.CJR = CJR;
    }

    public String getCJDD() {
        return this.CJDD;
    }

    public void setCJDD(String CJDD) {
        this.CJDD = CJDD;
    }

    public String getJZR() {
        return this.JZR;
    }

    public void setJZR(String JZR) {
        this.JZR = JZR;
    }

    public String getDW() {
        return this.DW;
    }

    public void setDW(String DW) {
        this.DW = DW;
    }

    public String getBZ() {
        return this.BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getSCR() {
        return this.SCR;
    }

    public void setSCR(String SCR) {
        this.SCR = SCR;
    }

    public Long getSCSJ() {
        return this.SCSJ;
    }

    public void setSCSJ(Long SCSJ) {
        this.SCSJ = SCSJ;
    }

    public String getZT() {
        return this.ZT;
    }

    public void setZT(String ZT) {
        this.ZT = ZT;
    }

    public String getWSID() {
        return this.WSID;
    }

    public void setWSID(String WSID) {
        this.WSID = WSID;
    }

    public String getLXMC() {
        return this.LXMC;
    }

    public void setLXMC(String LXMC) {
        this.LXMC = LXMC;
    }

    public String getZJLYMC() {
        return this.ZJLYMC;
    }

    public void setZJLYMC(String ZJLYMC) {
        this.ZJLYMC = ZJLYMC;
    }

    public String getLRSJ() {
        return this.LRSJ;
    }

    public void setLRSJ(String LRSJ) {
        this.LRSJ = LRSJ;
    }

    public String getYS() {
        return this.YS;
    }

    public void setYS(String YS) {
        this.YS = YS;
    }

    public String getOtype() {
        return this.Otype;
    }

    public void setOtype(String Otype) {
        this.Otype = Otype;
    }

    public Boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(Boolean isUpload) {
        this.isUpload = isUpload;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 356513708)
    public List<GreenMedia> getGreenMedia() {
        if (greenMedia == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GreenMediaDao targetDao = daoSession.getGreenMediaDao();
            List<GreenMedia> greenMediaNew = targetDao
                    ._queryGreenEvidence_GreenMedia(id);
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1127659830)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGreenEvidenceDao() : null;
    }

}
