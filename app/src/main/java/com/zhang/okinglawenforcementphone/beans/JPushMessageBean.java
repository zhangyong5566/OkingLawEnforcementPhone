package com.zhang.okinglawenforcementphone.beans;

import java.util.List;

/**
 * Created by Administrator on 2018/8/24/024.
 */

public class JPushMessageBean {

    /**
     * audience : {"alias":["8aac8d5f586ac00901586ae91bb7050d"]}
     * notification : {"alert":"有新任务！！！！","android":{"alert_type":1,"big_pic_path":"picture url","big_text":"big text content","builder_id":3,"extras":{"newsid":321},"priority":0,"style":1,"title":"JPush test"}}
     * platform : ["android"]
     */

    private AudienceBean audience;
    private NotificationBean notification;
    private List<String> platform;

    public AudienceBean getAudience() {
        return audience;
    }

    public void setAudience(AudienceBean audience) {
        this.audience = audience;
    }

    public NotificationBean getNotification() {
        return notification;
    }

    public void setNotification(NotificationBean notification) {
        this.notification = notification;
    }

    public List<String> getPlatform() {
        return platform;
    }

    public void setPlatform(List<String> platform) {
        this.platform = platform;
    }

    public static class AudienceBean {
        private List<String> alias;

        public List<String> getAlias() {
            return alias;
        }

        public void setAlias(List<String> alias) {
            this.alias = alias;
        }
    }

    public static class NotificationBean {
        /**
         * alert : 有新任务！！！！
         * android : {"alert_type":1,"big_pic_path":"picture url","big_text":"big text content","builder_id":3,"extras":{"newsid":321},"priority":0,"style":1,"title":"JPush test"}
         */

        private String alert;
        private AndroidBean android;

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public AndroidBean getAndroid() {
            return android;
        }

        public void setAndroid(AndroidBean android) {
            this.android = android;
        }

        public static class AndroidBean {
            /**
             * alert_type : 1
             * big_pic_path : picture url
             * big_text : big text content
             * builder_id : 3
             * extras : {"newsid":321}
             * priority : 0
             * style : 1
             * title : JPush test
             */

            private int alert_type;
            private String big_pic_path;
            private String big_text;
            private int builder_id;
            private ExtrasBean extras;
            private int priority;
            private int style;
            private String title;

            public int getAlert_type() {
                return alert_type;
            }

            public void setAlert_type(int alert_type) {
                this.alert_type = alert_type;
            }

            public String getBig_pic_path() {
                return big_pic_path;
            }

            public void setBig_pic_path(String big_pic_path) {
                this.big_pic_path = big_pic_path;
            }

            public String getBig_text() {
                return big_text;
            }

            public void setBig_text(String big_text) {
                this.big_text = big_text;
            }

            public int getBuilder_id() {
                return builder_id;
            }

            public void setBuilder_id(int builder_id) {
                this.builder_id = builder_id;
            }

            public ExtrasBean getExtras() {
                return extras;
            }

            public void setExtras(ExtrasBean extras) {
                this.extras = extras;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public int getStyle() {
                return style;
            }

            public void setStyle(int style) {
                this.style = style;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public static class ExtrasBean {
                /**
                 * newsid : 321
                 */

                private String taskid;
                private String data;
                private String openType;

                public String getOpenType() {
                    return openType;
                }

                public void setOpenType(String openType) {
                    this.openType = openType;
                }

                public String getTaskid() {
                    return taskid;
                }

                public void setTaskid(String taskid) {
                    this.taskid = taskid;
                }

                public String getData() {
                    return data;
                }

                public void setData(String data) {
                    this.data = data;
                }
            }
        }
    }
}
