package com.zhang.okinglawenforcementphone.beans;

import java.util.List;

/**
 * Created by Administrator on 2018/6/9/009.
 */

public class SmameLeveOV {

    /**
     * data : {"msg":"获人员成功！","records":[{"DEPTID":"001007","DEPTNAME":"西江局水政监察支队"},{"DEPTID":"001003","DEPTNAME":"汕头水政监察支队"},{"DEPTID":"001005","DEPTNAME":"广州市水务局执法监察支队"},{"DEPTID":"001001","DEPTNAME":"广东省水利厅水利水政监察局"},{"DEPTID":"001002","DEPTNAME":"中山水政监察支队"},{"DEPTID":"001006","DEPTNAME":"德庆水政监察大队"}]}
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
         * msg : 获人员成功！
         * records : [{"DEPTID":"001007","DEPTNAME":"西江局水政监察支队"},{"DEPTID":"001003","DEPTNAME":"汕头水政监察支队"},{"DEPTID":"001005","DEPTNAME":"广州市水务局执法监察支队"},{"DEPTID":"001001","DEPTNAME":"广东省水利厅水利水政监察局"},{"DEPTID":"001002","DEPTNAME":"中山水政监察支队"},{"DEPTID":"001006","DEPTNAME":"德庆水政监察大队"}]
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
             * DEPTID : 001007
             * DEPTNAME : 西江局水政监察支队
             */

            private String DEPTID;
            private String DEPTNAME;

            public String getDEPTID() {
                return DEPTID;
            }

            public void setDEPTID(String DEPTID) {
                this.DEPTID = DEPTID;
            }

            public String getDEPTNAME() {
                return DEPTNAME;
            }

            public void setDEPTNAME(String DEPTNAME) {
                this.DEPTNAME = DEPTNAME;
            }
        }
    }
}
