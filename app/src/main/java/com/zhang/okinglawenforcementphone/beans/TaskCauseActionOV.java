package com.zhang.okinglawenforcementphone.beans;

import java.util.List;

/**
 * Created by Administrator on 2018/6/9/009.
 */

public class TaskCauseActionOV {

    /**
     * data : {"msg":"查询成功！","records":[{"afdd":"测试","afsj":1528515892000,"afsjstr":"2018-06-09 11:44:52","aqjy":"民工","aylrrq":1528515892000,"aylrrqstr":"2018-06-09 11:44:52","aylx":"执法检查","ayqy":"民工","id":"15285158922175264911","slsj":1528516041000,"slsjstr":"2018-06-09 11:47:21"}]}
     * status : 1
     */

    private DataBean data;
    private String status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * msg : 查询成功！
         * records : [{"afdd":"测试","afsj":1528515892000,"afsjstr":"2018-06-09 11:44:52","aqjy":"民工","aylrrq":1528515892000,"aylrrqstr":"2018-06-09 11:44:52","aylx":"执法检查","ayqy":"民工","id":"15285158922175264911","slsj":1528516041000,"slsjstr":"2018-06-09 11:47:21"}]
         */

        private String msg;
        private List<RecordsBean> records;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean {
            /**
             * afdd : 测试
             * afsj : 1528515892000
             * afsjstr : 2018-06-09 11:44:52
             * aqjy : 民工
             * aylrrq : 1528515892000
             * aylrrqstr : 2018-06-09 11:44:52
             * aylx : 执法检查
             * ayqy : 民工
             * id : 15285158922175264911
             * slsj : 1528516041000
             * slsjstr : 2018-06-09 11:47:21
             */

            private String afdd;
            private long afsj;
            private String afsjstr;
            private String aqjy;
            private long aylrrq;
            private String aylrrqstr;
            private String aylx;
            private String ayqy;
            private String id;
            private String slsjstr;

            public String getAfdd() {
                return afdd;
            }

            public void setAfdd(String afdd) {
                this.afdd = afdd;
            }

            public long getAfsj() {
                return afsj;
            }

            public void setAfsj(long afsj) {
                this.afsj = afsj;
            }

            public String getAfsjstr() {
                return afsjstr;
            }

            public void setAfsjstr(String afsjstr) {
                this.afsjstr = afsjstr;
            }

            public String getAqjy() {
                return aqjy;
            }

            public void setAqjy(String aqjy) {
                this.aqjy = aqjy;
            }

            public long getAylrrq() {
                return aylrrq;
            }

            public void setAylrrq(long aylrrq) {
                this.aylrrq = aylrrq;
            }

            public String getAylrrqstr() {
                return aylrrqstr;
            }

            public void setAylrrqstr(String aylrrqstr) {
                this.aylrrqstr = aylrrqstr;
            }

            public String getAylx() {
                return aylx;
            }

            public void setAylx(String aylx) {
                this.aylx = aylx;
            }

            public String getAyqy() {
                return ayqy;
            }

            public void setAyqy(String ayqy) {
                this.ayqy = ayqy;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }


            public String getSlsjstr() {
                return slsjstr;
            }

            public void setSlsjstr(String slsjstr) {
                this.slsjstr = slsjstr;
            }

        }
    }

    @Override
    public String toString() {
        return "TaskCauseActionOV{" +
                "data=" + data +
                ", status='" + status + '\'' +
                '}';
    }


}
