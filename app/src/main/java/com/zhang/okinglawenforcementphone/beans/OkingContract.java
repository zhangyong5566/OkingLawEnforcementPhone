package com.zhang.okinglawenforcementphone.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class OkingContract {
    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static GreenUser CURRENTUSER = null;
    public static String[] LOCATIONRESULT = new String[8];
    public static final String UPDATE_GPS_STATE_UI = "oking.updategpsstate";
    public static List<String> MARQUEEVIEWINFO = new ArrayList<>();

    static {
        MARQUEEVIEWINFO.add("");
        MARQUEEVIEWINFO.add("");
        MARQUEEVIEWINFO.add("");

    }
}
