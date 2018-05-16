package com.zhang.baselib;

import android.util.Log;
import android.webkit.CookieManager;


/**
 * Created by pc on 16/10/19.
 *
 *
 */

public class DefaultContants {


    public static boolean HASGOTTOKEN = false;
    //喆
//    public static final String SERVER_HOST = "http://192.168.1.120:8087/gdWater";
//    public static final String SERVER = "http://192.168.1.120:8087";
//    public static String DOMAIN = "http://192.168.1.120:8087";

    //刘
//    public static final String SERVER_HOST = "http://172.19.22.1:8080/gdWater";
//    public static final String SERVER = "http://172.19.22.1:8080";
//    public static String DOMAIN = "172.19.22.1:8080";

    //测试服务器
    public static final String SERVER_HOST = "http://10.44.21.26:8087/gdWater";
    public static final String SERVER = "http://10.44.21.26:8087";
    public static String DOMAIN = "10.44.21.26:8087";

    //正式服务器
//    public static final String SERVER_HOST = "http://10.44.21.26:80/gdWater";
//    public static final String SERVER = "http://10.44.21.26:80";
//    public static String DOMAIN = "10.44.21.26:80";


    public static String PATH = "/gdWater";
    public static String JSESSIONID = "";
    public static String ACCESS_TOKEN = "";
    public static boolean ISHTTPLOGIN = false;

    public static void syncCookie(String url) {
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookies(null);
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                Log.i("Oking", oldCookie);
            }

//            StringBuilder sbCookie = new StringBuilder();
//            sbCookie.append(String.format("JSESSIONID=%s", DefaultContants.JSESSIONID));
//            sbCookie.append(String.format(";domain=%s", DefaultContants.DOMAIN));
//            sbCookie.append(String.format(";path=%s", DefaultContants.PATH));

//            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url,String.format("JSESSIONID=%s", DefaultContants.JSESSIONID));
            cookieManager.setCookie(url, String.format(";domain=%s", DefaultContants.DOMAIN));
            cookieManager.setCookie(url, String.format(";path=%s", DefaultContants.PATH));
            cookieManager.flush();

//            String newCookie = cookieManager.getCookie(url);


        } catch (Exception e) {
        }
    }




}
