package com.zhang.okinglawenforcementphone.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.PicUtil;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ShootActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;


@SuppressWarnings("deprecation")
public class MyCamera implements Camera.PictureCallback, Camera.ShutterCallback {
    private Camera mCamera;
    private ScheduledThreadPoolExecutor mTimerShootingExecutor;
    private ArrayList<String> paths = new ArrayList<String>();
    private ShootActivity mShootActivity;
    private Handler mHandler = new Handler();

    public MyCamera(ShootActivity shootActivity) {
        this.mShootActivity = shootActivity;
    }

    public void openCamera() {
        if (null == mCamera) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
        }
    }

    public void releaseCamera() {
        if (null != mCamera) {
            if (isTimerShootingStart()) {
                stopTimerShooting();
            }

            mCamera.release();
            mCamera = null;
        }
    }

    public void takePicture() {
        mCamera.takePicture(this, null, this);
    }


    public synchronized void stopTimerShooting() {
        if (null != mTimerShootingExecutor) {
            mTimerShootingExecutor.shutdown();
            mTimerShootingExecutor = null;
        }
    }

    public synchronized boolean isTimerShootingStart() {
        if (null != mTimerShootingExecutor) {
            return true;
        } else {
            return false;
        }
    }

    public void onSurfaceCreated(SurfaceHolder holder) {
        try {
            //surface创建成功能够拿到回调的holder
            //holder中包含有成功创建的Surface
            //从而交给摄像机预览使用
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //surface的尺寸发生变化
        //配置预览参数，如分辨率等
        //这里使用的分辨率简单选取了支持的预览分辨率的第一项
        //网上可以查找对应的优选算法
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        parameters.setPreviewSize(selected.width, selected.height);
        parameters.setPictureSize(selected.width, selected.height);

        //给摄像机设置参数，开始预览
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    public void onSurfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            bmp = PicUtil.rotateBitmapByDegree(bmp, 90);
            String filename = UUID.randomUUID().toString();
            String filePathname = "/storage/emulated/0/oking/mission_pic/" + filename+".jpg";
            save(bmp, filePathname, filename);


    }


    private void save(Bitmap bitmap, String filePath, String fileName) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // 创建文件夹
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos); // 向缓冲区之中压缩图片
            bos.flush();
            bos.close();

            paths.add(filePath);

            RxToast.success(BaseApplication.getApplictaion(), "拍照成功", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mShootActivity.notyEnablestate(true);
                }
            }, 300);

            //重新启动预览
            mCamera.startPreview();

        } catch (Exception e) {

        }
    }

    @Override
    public void onShutter() {
    }

    public ArrayList<String> completePhotos() {

        return paths;
    }

}
