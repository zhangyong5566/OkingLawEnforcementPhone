package com.zhang.okinglawenforcementphone.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/5/28.
 */
@Entity
public class GreenEvidenceMedia {
    @Id(autoincrement = true)
    private Long id;
    private Long greenEvidenceId;
    private Long time;
    private String path;
    private String userid;
    private String taskid;
    private Integer type;               //1表示证据图片  2表示视频  3表示语音
    @Generated(hash = 1044598213)
    public GreenEvidenceMedia(Long id, Long greenEvidenceId, Long time, String path,
            String userid, String taskid, Integer type) {
        this.id = id;
        this.greenEvidenceId = greenEvidenceId;
        this.time = time;
        this.path = path;
        this.userid = userid;
        this.taskid = taskid;
        this.type = type;
    }
    @Generated(hash = 612896463)
    public GreenEvidenceMedia() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getGreenEvidenceId() {
        return this.greenEvidenceId;
    }
    public void setGreenEvidenceId(Long greenEvidenceId) {
        this.greenEvidenceId = greenEvidenceId;
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

    @Override
    public String toString() {
        return "GreenEvidenceMedia{" +
                "id=" + id +
                ", greenEvidenceId=" + greenEvidenceId +
                ", time=" + time +
                ", path='" + path + '\'' +
                ", userid='" + userid + '\'' +
                ", taskid='" + taskid + '\'' +
                ", type=" + type +
                '}';
    }
}
