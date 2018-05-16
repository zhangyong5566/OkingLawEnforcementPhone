package com.zhang.okinglawenforcementphone.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/3/21.
 */

@Entity
public class GreenUser {
    @Id(autoincrement = true) //自增
    private Long id; //主键
    private String userid;
    private String dept_id;
    private String acount;
    private String userName;
    private String password;
    private String deptname;
    private String logintime;
    private String phone;
    private String headimg;
    private String menuGroup;
    @Generated(hash = 2071299888)
    public GreenUser(Long id, String userid, String dept_id, String acount,
            String userName, String password, String deptname, String logintime,
            String phone, String headimg, String menuGroup) {
        this.id = id;
        this.userid = userid;
        this.dept_id = dept_id;
        this.acount = acount;
        this.userName = userName;
        this.password = password;
        this.deptname = deptname;
        this.logintime = logintime;
        this.phone = phone;
        this.headimg = headimg;
        this.menuGroup = menuGroup;
    }
    @Generated(hash = 1678257977)
    public GreenUser() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getDept_id() {
        return this.dept_id;
    }
    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }
    public String getAcount() {
        return this.acount;
    }
    public void setAcount(String acount) {
        this.acount = acount;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getDeptname() {
        return this.deptname;
    }
    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }
    public String getLogintime() {
        return this.logintime;
    }
    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getHeadimg() {
        return this.headimg;
    }
    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }
    public String getMenuGroup() {
        return this.menuGroup;
    }
    public void setMenuGroup(String menuGroup) {
        this.menuGroup = menuGroup;
    }


}
