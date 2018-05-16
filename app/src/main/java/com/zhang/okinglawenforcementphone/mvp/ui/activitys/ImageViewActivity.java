package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zhang.okinglawenforcementphone.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ImageViewActivity extends Activity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.imageView)
    SimpleDraweeView mImageView;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        mBind = ButterKnife.bind(this);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();

        Uri imageUri = intent.getData();
        if (imageUri != null) {
            imageView.setImageURI(imageUri);
        } else {
            this.finish();
        }
        String picLocation = intent.getStringExtra("picLocation");
        if (picLocation != null) {
            TextView tvLocation = findViewById(R.id.tv_location);

            tvLocation.setText("经纬度：" + picLocation);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @OnClick(R.id.imageView)
    public void onViewClicked() {
        ImageViewActivity.this.finish();
    }
}
