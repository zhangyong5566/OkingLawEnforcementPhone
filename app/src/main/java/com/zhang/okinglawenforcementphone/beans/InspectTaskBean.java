package com.zhang.okinglawenforcementphone.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/13.
 */

public class InspectTaskBean implements Parcelable{

    /**
     * APPROVED_PERSON : c16e105ada884d9fb7fdcb4ef626cf62
     * APPROVED_TIME : 1507620803000
     * BEGIN_TIME : 1507620840000
     * CREATE_TIME : 1507620751000
     * DELIVERY_TIME : 1507620803000
     * DEPT_ID : 001001
     * END_TIME : 1507793640000
     * FBDW : 广东省水利厅水利水政监察局
     * FBR : 邓桂林
     * FBRID : da40394f32624b92b443d0fcaa73dbc2
     * FID : 0
     * ID : 15076207516755740071
     * JJCD : 0
     * JSDW : 广东省水利厅水利水政监察局
     * JSR : 邓桂林
     * JSRID : da40394f32624b92b443d0fcaa73dbc2
     * MC : 未发布
     * PUBLISHER : da40394f32624b92b443d0fcaa73dbc2
     * RECEIVER : da40394f32624b92b443d0fcaa73dbc2
     * RWLX : 0
     * RWLY : 0
     * RWMC : 水政监察局【巡】201710020
     * RWMS : 测试
     * RWQYMS : 测试
     * RWXH : 20
     * SJQ : 2017-10-10 15:34
     * SJZ : 2017-10-12 15:34
     * SPR : 曾建生
     * SPRID : c16e105ada884d9fb7fdcb4ef626cf62
     * SPYJ : 同意
     * STATUS : 3
     * TASK_NAME : 水政监察局【巡】201710020
     * TASK_TYPE : 0
     * XH : 1
     * EXECUTE_START_TIME : 1507711097000
     * RWCD : 1
     * EXECUTE_END_TIME : 1506413945000
     * TYPEOFTASK : 0
     */

    private String APPROVED_PERSON;
    private long APPROVED_TIME;
    private long BEGIN_TIME;
    private long CREATE_TIME;
    private long DELIVERY_TIME;
    private String DEPT_ID;
    private long END_TIME;
    private String FBDW;
    private String FBR;
    private String FBRID;
    private String FID;
    private String ID;
    private String JJCD;
    private String JSDW;
    private String JSR;
    private String JSRID;
    private String MC;
    private String PUBLISHER;
    private String RECEIVER;
    private String RWLX;
    private String RWLY;
    private String RWMC;
    private String RWMS;
    private String RWQYMS;
    private int RWXH;
    private String SJQ;
    private String SJZ;
    private String SPR;
    private String SPRID;
    private String SPYJ;
    private String STATUS;
    private String TASK_NAME;
    private String TASK_TYPE;
    private int XH;
    private long EXECUTE_START_TIME;
    private String RWCD;
    private long EXECUTE_END_TIME;
    private String TYPEOFTASK;

    public InspectTaskBean(Parcel in) {
        APPROVED_PERSON = in.readString();
        APPROVED_TIME = in.readLong();
        BEGIN_TIME = in.readLong();
        CREATE_TIME = in.readLong();
        DELIVERY_TIME = in.readLong();
        DEPT_ID = in.readString();
        END_TIME = in.readLong();
        FBDW = in.readString();
        FBR = in.readString();
        FBRID = in.readString();
        FID = in.readString();
        ID = in.readString();
        JJCD = in.readString();
        JSDW = in.readString();
        JSR = in.readString();
        JSRID = in.readString();
        MC = in.readString();
        PUBLISHER = in.readString();
        RECEIVER = in.readString();
        RWLX = in.readString();
        RWLY = in.readString();
        RWMC = in.readString();
        RWMS = in.readString();
        RWQYMS = in.readString();
        RWXH = in.readInt();
        SJQ = in.readString();
        SJZ = in.readString();
        SPR = in.readString();
        SPRID = in.readString();
        SPYJ = in.readString();
        STATUS = in.readString();
        TASK_NAME = in.readString();
        TASK_TYPE = in.readString();
        XH = in.readInt();
        EXECUTE_START_TIME = in.readLong();
        RWCD = in.readString();
        EXECUTE_END_TIME = in.readLong();
        TYPEOFTASK = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(APPROVED_PERSON);
        dest.writeLong(APPROVED_TIME);
        dest.writeLong(BEGIN_TIME);
        dest.writeLong(CREATE_TIME);
        dest.writeLong(DELIVERY_TIME);
        dest.writeString(DEPT_ID);
        dest.writeLong(END_TIME);
        dest.writeString(FBDW);
        dest.writeString(FBR);
        dest.writeString(FBRID);
        dest.writeString(FID);
        dest.writeString(ID);
        dest.writeString(JJCD);
        dest.writeString(JSDW);
        dest.writeString(JSR);
        dest.writeString(JSRID);
        dest.writeString(MC);
        dest.writeString(PUBLISHER);
        dest.writeString(RECEIVER);
        dest.writeString(RWLX);
        dest.writeString(RWLY);
        dest.writeString(RWMC);
        dest.writeString(RWMS);
        dest.writeString(RWQYMS);
        dest.writeInt(RWXH);
        dest.writeString(SJQ);
        dest.writeString(SJZ);
        dest.writeString(SPR);
        dest.writeString(SPRID);
        dest.writeString(SPYJ);
        dest.writeString(STATUS);
        dest.writeString(TASK_NAME);
        dest.writeString(TASK_TYPE);
        dest.writeInt(XH);
        dest.writeLong(EXECUTE_START_TIME);
        dest.writeString(RWCD);
        dest.writeLong(EXECUTE_END_TIME);
        dest.writeString(TYPEOFTASK);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InspectTaskBean> CREATOR = new Creator<InspectTaskBean>() {
        @Override
        public InspectTaskBean createFromParcel(Parcel in) {
            return new InspectTaskBean(in);
        }

        @Override
        public InspectTaskBean[] newArray(int size) {
            return new InspectTaskBean[size];
        }
    };

    public String getAPPROVED_PERSON() {
        return APPROVED_PERSON;
    }

    public void setAPPROVED_PERSON(String APPROVED_PERSON) {
        this.APPROVED_PERSON = APPROVED_PERSON;
    }

    public long getAPPROVED_TIME() {
        return APPROVED_TIME;
    }

    public void setAPPROVED_TIME(long APPROVED_TIME) {
        this.APPROVED_TIME = APPROVED_TIME;
    }

    public long getBEGIN_TIME() {
        return BEGIN_TIME;
    }

    public void setBEGIN_TIME(long BEGIN_TIME) {
        this.BEGIN_TIME = BEGIN_TIME;
    }

    public long getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(long CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public long getDELIVERY_TIME() {
        return DELIVERY_TIME;
    }

    public void setDELIVERY_TIME(long DELIVERY_TIME) {
        this.DELIVERY_TIME = DELIVERY_TIME;
    }

    public String getDEPT_ID() {
        return DEPT_ID;
    }

    public void setDEPT_ID(String DEPT_ID) {
        this.DEPT_ID = DEPT_ID;
    }

    public long getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(long END_TIME) {
        this.END_TIME = END_TIME;
    }

    public String getFBDW() {
        return FBDW;
    }

    public void setFBDW(String FBDW) {
        this.FBDW = FBDW;
    }

    public String getFBR() {
        return FBR;
    }

    public void setFBR(String FBR) {
        this.FBR = FBR;
    }

    public String getFBRID() {
        return FBRID;
    }

    public void setFBRID(String FBRID) {
        this.FBRID = FBRID;
    }

    public String getFID() {
        return FID;
    }

    public void setFID(String FID) {
        this.FID = FID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getJJCD() {
        return JJCD;
    }

    public void setJJCD(String JJCD) {
        this.JJCD = JJCD;
    }

    public String getJSDW() {
        return JSDW;
    }

    public void setJSDW(String JSDW) {
        this.JSDW = JSDW;
    }

    public String getJSR() {
        return JSR;
    }

    public void setJSR(String JSR) {
        this.JSR = JSR;
    }

    public String getJSRID() {
        return JSRID;
    }

    public void setJSRID(String JSRID) {
        this.JSRID = JSRID;
    }

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public String getPUBLISHER() {
        return PUBLISHER;
    }

    public void setPUBLISHER(String PUBLISHER) {
        this.PUBLISHER = PUBLISHER;
    }

    public String getRECEIVER() {
        return RECEIVER;
    }

    public void setRECEIVER(String RECEIVER) {
        this.RECEIVER = RECEIVER;
    }

    public String getRWLX() {
        return RWLX;
    }

    public void setRWLX(String RWLX) {
        this.RWLX = RWLX;
    }

    public String getRWLY() {
        return RWLY;
    }

    public void setRWLY(String RWLY) {
        this.RWLY = RWLY;
    }

    public String getRWMC() {
        return RWMC;
    }

    public void setRWMC(String RWMC) {
        this.RWMC = RWMC;
    }

    public String getRWMS() {
        return RWMS;
    }

    public void setRWMS(String RWMS) {
        this.RWMS = RWMS;
    }

    public String getRWQYMS() {
        return RWQYMS;
    }

    public void setRWQYMS(String RWQYMS) {
        this.RWQYMS = RWQYMS;
    }

    public int getRWXH() {
        return RWXH;
    }

    public void setRWXH(int RWXH) {
        this.RWXH = RWXH;
    }

    public String getSJQ() {
        return SJQ;
    }

    public void setSJQ(String SJQ) {
        this.SJQ = SJQ;
    }

    public String getSJZ() {
        return SJZ;
    }

    public void setSJZ(String SJZ) {
        this.SJZ = SJZ;
    }

    public String getSPR() {
        return SPR;
    }

    public void setSPR(String SPR) {
        this.SPR = SPR;
    }

    public String getSPRID() {
        return SPRID;
    }

    public void setSPRID(String SPRID) {
        this.SPRID = SPRID;
    }

    public String getSPYJ() {
        return SPYJ;
    }

    public void setSPYJ(String SPYJ) {
        this.SPYJ = SPYJ;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getTASK_NAME() {
        return TASK_NAME;
    }

    public void setTASK_NAME(String TASK_NAME) {
        this.TASK_NAME = TASK_NAME;
    }

    public String getTASK_TYPE() {
        return TASK_TYPE;
    }

    public void setTASK_TYPE(String TASK_TYPE) {
        this.TASK_TYPE = TASK_TYPE;
    }

    public int getXH() {
        return XH;
    }

    public void setXH(int XH) {
        this.XH = XH;
    }

    public long getEXECUTE_START_TIME() {
        return EXECUTE_START_TIME;
    }

    public void setEXECUTE_START_TIME(long EXECUTE_START_TIME) {
        this.EXECUTE_START_TIME = EXECUTE_START_TIME;
    }

    public String getRWCD() {
        return RWCD;
    }

    public void setRWCD(String RWCD) {
        this.RWCD = RWCD;
    }

    public long getEXECUTE_END_TIME() {
        return EXECUTE_END_TIME;
    }

    public void setEXECUTE_END_TIME(long EXECUTE_END_TIME) {
        this.EXECUTE_END_TIME = EXECUTE_END_TIME;
    }

    public String getTYPEOFTASK() {
        return TYPEOFTASK;
    }

    public void setTYPEOFTASK(String TYPEOFTASK) {
        this.TYPEOFTASK = TYPEOFTASK;
    }
}
