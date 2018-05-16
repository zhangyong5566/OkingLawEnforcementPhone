package com.zhang.baselib.ui.views;


import com.zhang.baselib.ui.views.sprite.Sprite;
import com.zhang.baselib.ui.views.style.ChasingDots;
import com.zhang.baselib.ui.views.style.Circle;
import com.zhang.baselib.ui.views.style.CubeGrid;
import com.zhang.baselib.ui.views.style.DoubleBounce;
import com.zhang.baselib.ui.views.style.FadingCircle;
import com.zhang.baselib.ui.views.style.FoldingCube;
import com.zhang.baselib.ui.views.style.MultiplePulse;
import com.zhang.baselib.ui.views.style.MultiplePulseRing;
import com.zhang.baselib.ui.views.style.Pulse;
import com.zhang.baselib.ui.views.style.PulseRing;
import com.zhang.baselib.ui.views.style.RotatingCircle;
import com.zhang.baselib.ui.views.style.RotatingPlane;
import com.zhang.baselib.ui.views.style.Style;
import com.zhang.baselib.ui.views.style.ThreeBounce;
import com.zhang.baselib.ui.views.style.WanderingCubes;
import com.zhang.baselib.ui.views.style.Wave;

/**
 * Created by Administrator on 2018/4/18.
 */

public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            case WANDERING_CUBES:
                sprite = new WanderingCubes();
                break;
            case PULSE:
                sprite = new Pulse();
                break;
            case CHASING_DOTS:
                sprite = new ChasingDots();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            case MULTIPLE_PULSE:
                sprite = new MultiplePulse();
                break;
            case PULSE_RING:
                sprite = new PulseRing();
                break;
            case MULTIPLE_PULSE_RING:
                sprite = new MultiplePulseRing();
                break;
            default:
                break;
        }
        return sprite;
    }
}
