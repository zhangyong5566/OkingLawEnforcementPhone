package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;

import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.views.FullScreenVideoView;


public class VideoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// no title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// full screen
        setContentView(R.layout.activity_video);

        FullScreenVideoView videoView = (FullScreenVideoView) findViewById(R.id.videoView);

        Uri videouri = getIntent().getData();
        if(videouri != null){
            MediaController mc = new MediaController(this);
            videoView.setMediaController(mc);
            videoView.setVideoURI(videouri);
            mc.setMediaPlayer(videoView);
            videoView.requestFocus();
            videoView.start();
        }else{
            this.finish();
        }
    }
}
