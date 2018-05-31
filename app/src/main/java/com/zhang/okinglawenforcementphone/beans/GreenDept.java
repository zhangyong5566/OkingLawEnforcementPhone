package com.zhang.okinglawenforcementphone.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/5/24/024.
 */
@Entity
public class GreenDept {
    @Id(autoincrement = true)
    private Long id;
    private String account;
    private String deptid;
    private String deptname;
    private Integer num;
    private String phone;
    private String remark;
    private String sex;
    private String tel;
    private String userid;
    private String username;
    private String usertype;
    private Integer xh;
    @Generated(hash = 438934450)
    public GreenDept(Long id, String account, String deptid, String deptname,
            Integer num, String phone, String remark, String sex, String tel,
            String userid, String username, String usertype, Integer xh) {
        this.id = id;
        this.account = account;
        this.deptid = deptid;
        this.deptname = deptname;
        this.num = num;
        this.phone = phone;
        this.remark = remark;
        this.sex = sex;
        this.tel = tel;
        this.userid = userid;
        this.username = username;
        this.usertype = usertype;
        this.xh = xh;
    }
    @Generated(hash = 2028510082)
    public GreenDept() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getDeptid() {
        return this.deptid;
    }
    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }
    public String getDeptname() {
        return this.deptname;
    }
    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }
    public Integer getNum() {
        return this.num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getTel() {
        return this.tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
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
    public String getUsertype() {
        return this.usertype;
    }
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
    public Integer getXh() {
        return this.xh;
    }
    public void setXh(Integer xh) {
        this.xh = xh;
    }

    @Override
    public String toString() {
        return "GreenDept{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", deptid='" + deptid + '\'' +
                ", deptname='" + deptname + '\'' +
                ", num=" + num +
                ", phone='" + phone + '\'' +
                ", remark='" + remark + '\'' +
                ", sex='" + sex + '\'' +
                ", tel='" + tel + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", usertype='" + usertype + '\'' +
                ", xh=" + xh +
                '}';
    }
}
