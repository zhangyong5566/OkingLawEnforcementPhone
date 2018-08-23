package com.zhang.okinglawenforcementphone.beans;

import java.util.List;

/**
 * Created by Administrator on 2018/6/9/009.
 */

public class UnitOV {

    /**
     * data : {"msg":"获人员成功！","records":[{"children":false,"icon":"groupIcon","id":"10000000","text":"超级用户"},{"children":false,"icon":"groupIcon","id":"14767554594971734208","text":"tanni"},{"children":false,"icon":"groupIcon","id":"3eba8a760a824809bc588c68d4ae1091","text":"232"},{"children":false,"icon":"groupIcon","id":"5c0c1d0034084b01bf5812cab73b39c9","text":"测试2"},{"children":false,"icon":"groupIcon","id":"a20f6615bfcb4474ae7bbfdf08bc3418","text":"系统管理员"},{"children":false,"icon":"groupIcon","id":"a8c8e9e5a0d74cb7b242d5aa837cf16c","text":"GBA"},{"children":false,"icon":"groupIcon","id":"b3872f8408e440608527b4ba4179e085","text":"周周"},{"children":false,"icon":"groupIcon","id":"cce93796fe224f30a502d9cf4f84c3d8","text":"account_6"},{"children":false,"icon":"groupIcon","id":"d9b6b2915f8847a6aacaf08729ca7111","text":"水政执法群"},{"children":false,"icon":"groupIcon","id":"dcd986049d0d405c9ca5e4036be07358","text":"evan"},{"children":false,"icon":"groupIcon","id":"e339b8522000445fb62bacbb085684de","text":"测试 "},{"children":false,"icon":"groupIcon","id":"e8f37db5a5144f189aac2f5ac4ed7304","text":"account_2"}]}
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
         * records : [{"children":false,"icon":"groupIcon","id":"10000000","text":"超级用户"},{"children":false,"icon":"groupIcon","id":"14767554594971734208","text":"tanni"},{"children":false,"icon":"groupIcon","id":"3eba8a760a824809bc588c68d4ae1091","text":"232"},{"children":false,"icon":"groupIcon","id":"5c0c1d0034084b01bf5812cab73b39c9","text":"测试2"},{"children":false,"icon":"groupIcon","id":"a20f6615bfcb4474ae7bbfdf08bc3418","text":"系统管理员"},{"children":false,"icon":"groupIcon","id":"a8c8e9e5a0d74cb7b242d5aa837cf16c","text":"GBA"},{"children":false,"icon":"groupIcon","id":"b3872f8408e440608527b4ba4179e085","text":"周周"},{"children":false,"icon":"groupIcon","id":"cce93796fe224f30a502d9cf4f84c3d8","text":"account_6"},{"children":false,"icon":"groupIcon","id":"d9b6b2915f8847a6aacaf08729ca7111","text":"水政执法群"},{"children":false,"icon":"groupIcon","id":"dcd986049d0d405c9ca5e4036be07358","text":"evan"},{"children":false,"icon":"groupIcon","id":"e339b8522000445fb62bacbb085684de","text":"测试 "},{"children":false,"icon":"groupIcon","id":"e8f37db5a5144f189aac2f5ac4ed7304","text":"account_2"}]
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
             * children : false
             * icon : groupIcon
             * id : 10000000
             * text : 超级用户
             */

            private boolean children;
            private String icon;
            private String id;
            private String text;

            @Override
            public String toString() {
                return "RecordsBean{" +
                        "children=" + children +
                        ", icon='" + icon + '\'' +
                        ", id='" + id + '\'' +
                        ", text='" + text + '\'' +
                        '}';
            }

            public boolean isChildren() {
                return children;
            }

            public void setChildren(boolean children) {
                this.children = children;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }

    @Override
    public String toString() {
        return "UnitOV{" +
                "data=" + data +
                ", status='" + status + '\'' +
                '}';
    }
}
