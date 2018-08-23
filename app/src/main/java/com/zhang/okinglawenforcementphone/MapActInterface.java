package com.zhang.okinglawenforcementphone;

import android.os.Bundle;

/**
 * Created by Administrator on 2018/6/7/007.
 */

public interface MapActInterface {
    void onCreate(Bundle savedInstanceState);
    void onPause();
    void onResume();
    void onSaveInstanceState(Bundle outState);
    void onDestroy();

}
