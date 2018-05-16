package com.zhang.okinglawenforcementphone.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/3/27.
 */
@Entity
public class GreenMember {
    @Id(autoincrement = true)
    private Long id;
    private String servId;
    private String depatid;
    private String depatname;
    private String remark;
    private String zfzh;
    private String account;
    private Long greenMemberId;
    private String taskid;
    private String userid;
    private String username;
    private String post;
    private String signPic;
    @Generated(hash = 1299555132)
    public GreenMember(Long id, String servId, String depatid, String depatname,
            String remark, String zfzh, String account, Long greenMemberId,
            String taskid, String userid, String username, String post,
            String signPic) {
        this.id = id;
        this.servId = servId;
        this.depatid = depatid;
        this.depatname = depatname;
        this.remark = remark;
        this.zfzh = zfzh;
        this.account = account;
        this.greenMemberId = greenMemberId;
        this.taskid = taskid;
        this.userid = userid;
        this.username = username;
        this.post = post;
        this.signPic = signPic;
    }
    @Generated(hash = 211611258)
    public GreenMember() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getServId() {
        return this.servId;
    }
    public void setServId(String servId) {
        this.servId = servId;
    }
    public String getDepatid() {
        return this.depatid;
    }
    public void setDepatid(String depatid) {
        this.depatid = depatid;
    }
    public String getDepatname() {
        return this.depatname;
    }
    public void setDepatname(String depatname) {
        this.depatname = depatname;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getZfzh() {
        return this.zfzh;
    }
    public void setZfzh(String zfzh) {
        this.zfzh = zfzh;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public Long getGreenMemberId() {
        return this.greenMemberId;
    }
    public void setGreenMemberId(Long greenMemberId) {
        this.greenMemberId = greenMemberId;
    }
    public String getTaskid() {
        return this.taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPost() {
        return this.post;
    }
    public void setPost(String post) {
        this.post = post;
    }
    public String getSignPic() {
        return this.signPic;
    }
    public void setSignPic(String signPic) {
        this.signPic = signPic;
    }

    @Override
    public String toString() {
        return "GreenMember{" +
                "id=" + id +
                ", servId='" + servId + '\'' +
                ", depatid='" + depatid + '\'' +
                ", depatname='" + depatname + '\'' +
                ", remark='" + remark + '\'' +
                ", zfzh='" + zfzh + '\'' +
                ", account='" + account + '\'' +
                ", greenMemberId=" + greenMemberId +
                ", taskid='" + taskid + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", post='" + post + '\'' +
                ", signPic='" + signPic + '\'' +
                '}';
    }
}
