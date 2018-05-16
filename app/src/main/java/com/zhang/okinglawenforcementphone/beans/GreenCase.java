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
public class GreenCase {
    @Id(autoincrement = true)
    private Long id;
    private String AJID;
    private Long SLRQ;
    private String ZFBM;
    private String SLR;
    private String AJLX;
    private String AJMC;
    private String AY;
    private String AFDD;
    private Long AFSJ;
    private String AQJY;
    private String XWZSD;
    private String JGD;
    private String SSD;
    private String WHJGFSD;
    private String DSRQK;
    private String SQWTR;
    private String FLYJ;
    private String CFYJ;
    private String CFNR;
    private String CBR1;
    private String CBRDW1;
    private String ZFZH1;
    private String CBR2;
    private String CBRDW2;
    private String ZFZH2;
    private String ZT;
    private String AJLY;
    private String AJLXID;
    private String CBRID1;
    private String CBRID2;
    private String SLXX_ZT;

    @ToMany(referencedJoinProperty = "greenCaseId")
    private List<GreenEvidence> greenEvidence;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 156425666)
    private transient GreenCaseDao myDao;

    @Generated(hash = 875031119)
    public GreenCase(Long id, String AJID, Long SLRQ, String ZFBM, String SLR,
            String AJLX, String AJMC, String AY, String AFDD, Long AFSJ,
            String AQJY, String XWZSD, String JGD, String SSD, String WHJGFSD,
            String DSRQK, String SQWTR, String FLYJ, String CFYJ, String CFNR,
            String CBR1, String CBRDW1, String ZFZH1, String CBR2, String CBRDW2,
            String ZFZH2, String ZT, String AJLY, String AJLXID, String CBRID1,
            String CBRID2, String SLXX_ZT) {
        this.id = id;
        this.AJID = AJID;
        this.SLRQ = SLRQ;
        this.ZFBM = ZFBM;
        this.SLR = SLR;
        this.AJLX = AJLX;
        this.AJMC = AJMC;
        this.AY = AY;
        this.AFDD = AFDD;
        this.AFSJ = AFSJ;
        this.AQJY = AQJY;
        this.XWZSD = XWZSD;
        this.JGD = JGD;
        this.SSD = SSD;
        this.WHJGFSD = WHJGFSD;
        this.DSRQK = DSRQK;
        this.SQWTR = SQWTR;
        this.FLYJ = FLYJ;
        this.CFYJ = CFYJ;
        this.CFNR = CFNR;
        this.CBR1 = CBR1;
        this.CBRDW1 = CBRDW1;
        this.ZFZH1 = ZFZH1;
        this.CBR2 = CBR2;
        this.CBRDW2 = CBRDW2;
        this.ZFZH2 = ZFZH2;
        this.ZT = ZT;
        this.AJLY = AJLY;
        this.AJLXID = AJLXID;
        this.CBRID1 = CBRID1;
        this.CBRID2 = CBRID2;
        this.SLXX_ZT = SLXX_ZT;
    }

    @Generated(hash = 1341484882)
    public GreenCase() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAJID() {
        return this.AJID;
    }

    public void setAJID(String AJID) {
        this.AJID = AJID;
    }

    public Long getSLRQ() {
        return this.SLRQ;
    }

    public void setSLRQ(Long SLRQ) {
        this.SLRQ = SLRQ;
    }

    public String getZFBM() {
        return this.ZFBM;
    }

    public void setZFBM(String ZFBM) {
        this.ZFBM = ZFBM;
    }

    public String getSLR() {
        return this.SLR;
    }

    public void setSLR(String SLR) {
        this.SLR = SLR;
    }

    public String getAJLX() {
        return this.AJLX;
    }

    public void setAJLX(String AJLX) {
        this.AJLX = AJLX;
    }

    public String getAJMC() {
        return this.AJMC;
    }

    public void setAJMC(String AJMC) {
        this.AJMC = AJMC;
    }

    public String getAY() {
        return this.AY;
    }

    public void setAY(String AY) {
        this.AY = AY;
    }

    public String getAFDD() {
        return this.AFDD;
    }

    public void setAFDD(String AFDD) {
        this.AFDD = AFDD;
    }

    public Long getAFSJ() {
        return this.AFSJ;
    }

    public void setAFSJ(Long AFSJ) {
        this.AFSJ = AFSJ;
    }

    public String getAQJY() {
        return this.AQJY;
    }

    public void setAQJY(String AQJY) {
        this.AQJY = AQJY;
    }

    public String getXWZSD() {
        return this.XWZSD;
    }

    public void setXWZSD(String XWZSD) {
        this.XWZSD = XWZSD;
    }

    public String getJGD() {
        return this.JGD;
    }

    public void setJGD(String JGD) {
        this.JGD = JGD;
    }

    public String getSSD() {
        return this.SSD;
    }

    public void setSSD(String SSD) {
        this.SSD = SSD;
    }

    public String getWHJGFSD() {
        return this.WHJGFSD;
    }

    public void setWHJGFSD(String WHJGFSD) {
        this.WHJGFSD = WHJGFSD;
    }

    public String getDSRQK() {
        return this.DSRQK;
    }

    public void setDSRQK(String DSRQK) {
        this.DSRQK = DSRQK;
    }

    public String getSQWTR() {
        return this.SQWTR;
    }

    public void setSQWTR(String SQWTR) {
        this.SQWTR = SQWTR;
    }

    public String getFLYJ() {
        return this.FLYJ;
    }

    public void setFLYJ(String FLYJ) {
        this.FLYJ = FLYJ;
    }

    public String getCFYJ() {
        return this.CFYJ;
    }

    public void setCFYJ(String CFYJ) {
        this.CFYJ = CFYJ;
    }

    public String getCFNR() {
        return this.CFNR;
    }

    public void setCFNR(String CFNR) {
        this.CFNR = CFNR;
    }

    public String getCBR1() {
        return this.CBR1;
    }

    public void setCBR1(String CBR1) {
        this.CBR1 = CBR1;
    }

    public String getCBRDW1() {
        return this.CBRDW1;
    }

    public void setCBRDW1(String CBRDW1) {
        this.CBRDW1 = CBRDW1;
    }

    public String getZFZH1() {
        return this.ZFZH1;
    }

    public void setZFZH1(String ZFZH1) {
        this.ZFZH1 = ZFZH1;
    }

    public String getCBR2() {
        return this.CBR2;
    }

    public void setCBR2(String CBR2) {
        this.CBR2 = CBR2;
    }

    public String getCBRDW2() {
        return this.CBRDW2;
    }

    public void setCBRDW2(String CBRDW2) {
        this.CBRDW2 = CBRDW2;
    }

    public String getZFZH2() {
        return this.ZFZH2;
    }

    public void setZFZH2(String ZFZH2) {
        this.ZFZH2 = ZFZH2;
    }

    public String getZT() {
        return this.ZT;
    }

    public void setZT(String ZT) {
        this.ZT = ZT;
    }

    public String getAJLY() {
        return this.AJLY;
    }

    public void setAJLY(String AJLY) {
        this.AJLY = AJLY;
    }

    public String getAJLXID() {
        return this.AJLXID;
    }

    public void setAJLXID(String AJLXID) {
        this.AJLXID = AJLXID;
    }

    public String getCBRID1() {
        return this.CBRID1;
    }

    public void setCBRID1(String CBRID1) {
        this.CBRID1 = CBRID1;
    }

    public String getCBRID2() {
        return this.CBRID2;
    }

    public void setCBRID2(String CBRID2) {
        this.CBRID2 = CBRID2;
    }

    public String getSLXX_ZT() {
        return this.SLXX_ZT;
    }

    public void setSLXX_ZT(String SLXX_ZT) {
        this.SLXX_ZT = SLXX_ZT;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 375686542)
    public List<GreenEvidence> getGreenEvidence() {
        if (greenEvidence == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GreenEvidenceDao targetDao = daoSession.getGreenEvidenceDao();
            List<GreenEvidence> greenEvidenceNew = targetDao
                    ._queryGreenCase_GreenEvidence(id);
            synchronized (this) {
                if (greenEvidence == null) {
                    greenEvidence = greenEvidenceNew;
                }
            }
        }
        return greenEvidence;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 94580031)
    public synchronized void resetGreenEvidence() {
        greenEvidence = null;
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
    @Generated(hash = 1050274071)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGreenCaseDao() : null;
    }
   
}
