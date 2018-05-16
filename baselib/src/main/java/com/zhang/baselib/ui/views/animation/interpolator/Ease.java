package com.zhang.baselib.ui.views.animation.interpolator;

import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.animation.Interpolator;

/**
 * Created by Administrator on 2018/4/18.
 */

public class Ease {
    public static Interpolator inOut() {
        return PathInterpolatorCompat.create(0.42f, 0f, 0.58f, 1f);
    }
}
