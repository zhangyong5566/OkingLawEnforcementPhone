package com.zhang.okinglawenforcementphone.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2018/4/11.
 */
@Entity
public class GreenEquipment implements MultiItemEntity {
    @Id(autoincrement = true)
    private Long id;
    private String type2;
    private String mc1;      //装备类型
    private String type;      //装备类型code
    private String mc2;    //装备名称
    private String value;
    private String ly;
    private String remarks;
    private String deptId;
    private int itemType = 0;

    @Generated(hash = 996505078)
    public GreenEquipment(Long id, String type2, String mc1, String type,
            String mc2, String value, String ly, String remarks, String deptId,
            int itemType) {
        this.id = id;
        this.type2 = type2;
        this.mc1 = mc1;
        this.type = type;
        this.mc2 = mc2;
        this.value = value;
        this.ly = ly;
        this.remarks = remarks;
        this.deptId = deptId;
        this.itemType = itemType;
    }

    @Generated(hash = 119980583)
    public GreenEquipment() {
    }
    
    @Override
    public int getItemType() {
        return itemType;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType2() {
        return this.type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getMc1() {
        return this.mc1;
    }

    public void setMc1(String mc1) {
        this.mc1 = mc1;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMc2() {
        return this.mc2;
    }

    public void setMc2(String mc2) {
        this.mc2 = mc2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLy() {
        return this.ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "GreenEquipment{" +
                "id=" + id +
                ", type2='" + type2 + '\'' +
                ", mc1='" + mc1 + '\'' +
                ", type='" + type + '\'' +
                ", mc2='" + mc2 + '\'' +
                ", value='" + value + '\'' +
                ", ly='" + ly + '\'' +
                ", remarks='" + remarks + '\'' +
                ", deptId='" + deptId + '\'' +
                ", itemType=" + itemType +
                '}';
    }
}
