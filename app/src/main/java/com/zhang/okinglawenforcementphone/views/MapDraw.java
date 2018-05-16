package com.zhang.okinglawenforcementphone.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;
import com.yinghe.whiteboardlib.bean.StrokeRecord;
import com.zhang.okinglawenforcementphone.beans.LatLngListOV;


import java.util.ArrayList;
import java.util.List;

import static com.yinghe.whiteboardlib.bean.StrokeRecord.STROKE_TYPE_CIRCLE;
import static com.yinghe.whiteboardlib.bean.StrokeRecord.STROKE_TYPE_RECTANGLE;

/**
 * Created by Administrator on 2018/5/9/009.
 */

public class MapDraw extends View implements View.OnTouchListener {
    public Context mContext;
    public StrokeRecord curStrokeRecord;
    public float downX, downY, curX, curY;;
    public OnDrawChangedListener onDrawChangedListener;
    private List<LatLng> mLatLngs = new ArrayList<>();
    private AMap mAMap;
    public MapDraw(Context context) {
        super(context);
    }

    public MapDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setOnTouchListener(this);
        setBackgroundColor(Color.TRANSPARENT);
        invalidate();
    }



    @Override
    public boolean onTouch(View view, MotionEvent event) {
        curX = event.getX();
        curY = event.getY();
        mLatLngs.add(mAMap.getProjection().fromScreenLocation(new Point((int) curX,(int) curY)));
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:    //一个点

                break;
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp(event);
                invalidate();
                break;
            default:
                break;

        }

        return true;
    }

    private void touchUp(MotionEvent event) {
        if (onDrawChangedListener != null) {
            LatLngListOV latLngListOV = new LatLngListOV();
            latLngListOV.setType(curStrokeRecord.type);
            latLngListOV.setLatLngs(mLatLngs);
            latLngListOV.setLeft(curStrokeRecord.rect.left);
            latLngListOV.setRight(curStrokeRecord.rect.right);
            latLngListOV.setTop(curStrokeRecord.rect.top);
            latLngListOV.setBottom(curStrokeRecord.rect.bottom);
            latLngListOV.setZoom(mAMap.getCameraPosition().zoom);
            latLngListOV.setCenterLatLng(getMapCenterPoint());
            onDrawChangedListener.onDrawChanged(latLngListOV);
        }
    }

    private void touchMove(MotionEvent event) {
        curStrokeRecord.rect.set(downX < curX ? downX : curX, downY < curY ? downY : curY, downX > curX ? downX : curX, downY > curY ? downY : curY);
    }

    private void touchDown(MotionEvent event) {
        mLatLngs.clear();
        downX = event.getX();
        downY = event.getY();
        RectF rect = new RectF(downX, downY, downX, downY);
        curStrokeRecord.rect = rect;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (curStrokeRecord.rect!=null){
            int type = curStrokeRecord.type;
            if (type == STROKE_TYPE_CIRCLE) {
                canvas.drawOval(curStrokeRecord.rect, curStrokeRecord.paint);
            } else if (type == STROKE_TYPE_RECTANGLE) {
                canvas.drawRect(curStrokeRecord.rect, curStrokeRecord.paint);
            }


        }



    }
    public void setStrokeRecord(StrokeRecord curStrokeRecord) {
        this.curStrokeRecord = curStrokeRecord;
    }

    public void setOnDrawChangedListener(OnDrawChangedListener listener) {
        this.onDrawChangedListener = listener;
    }

    public void setAmap(AMap aMap) {
        this.mAMap = aMap;
    }

    public interface OnDrawChangedListener {

        void onDrawChanged(LatLngListOV latLngListOV);
    }

    public LatLng getMapCenterPoint() {
        int left = getLeft();
        int top = getTop();
        int right = getRight();
        int bottom = getBottom();
        // 获得屏幕点击的位置
        int x = (int) (getX() + (right - left) / 2);
        int y = (int) (getY() + (bottom - top) / 2);
        Projection projection = mAMap.getProjection();
        LatLng pt = projection.fromScreenLocation(new Point(x, y));

        return pt;
    }
}
