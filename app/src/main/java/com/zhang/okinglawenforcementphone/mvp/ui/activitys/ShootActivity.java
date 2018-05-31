package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhang.okinglawenforcementphone.EnableCallBack;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.views.MyCamera;

import java.io.File;
import java.util.ArrayList;


public class ShootActivity extends BaseActivity implements SurfaceHolder.Callback, EnableCallBack {

    boolean enable = false;
    private MyCamera mCamera = new MyCamera(this);
    private File picStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/mission_pic");
    private TextView mTakePictureButton;
    private SharedPreferences mSp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);

        //配置SurfaceView
        //setType使用外来数据源
        //设置SurfaceHolder.Callback
        picStorageDir.mkdirs();
        mSp = getSharedPreferences("fileLocation", Context.MODE_PRIVATE);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);

        mTakePictureButton = findViewById(R.id.takePictureButton);
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCamera.takePicture();
                mTakePictureButton.setEnabled(false);
            }
        });

        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> paths = mCamera.completePhotos();
                Intent intent = new Intent();
                intent.putStringArrayListExtra("picpaths", paths);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.bt_succ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> paths = mCamera.completePhotos();
                Intent intent = new Intent();
                intent.putStringArrayListExtra("picpaths", paths);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCamera.openCamera();
    }

    @Override
    protected void onStop() {
        mCamera.releaseCamera();
        super.onStop();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera.onSurfaceCreated(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.onSurfaceChanged(holder, format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.onSurfaceDestroyed(holder);
    }

    @Override
    public void notyEnablestate(boolean enable) {

        this.enable = enable;
        mTakePictureButton.setEnabled(enable);
    }


}