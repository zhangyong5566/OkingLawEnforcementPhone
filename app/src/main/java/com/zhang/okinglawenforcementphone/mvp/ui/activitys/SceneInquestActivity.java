package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yinghe.whiteboardlib.fragment.WhiteBoardFragment;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zhang.baselib.utils.FileUtil.getAssetFileToCacheDir;

/**
 * 现场勘验
 */
public class SceneInquestActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Unbinder mBind;
    private DialogUtil mDialogUtil;
    private WhiteBoardFragment mWhiteBoardFragment;
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_inquest);
        mBind = ButterKnife.bind(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        initData();
        setListener();
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mDialogUtil == null) {
                    mDialogUtil = new DialogUtil();
                    mContentView = View.inflate(BaseApplication.getApplictaion(), R.layout.scene_quest_material, null);
                    GridView materialGridView = mContentView.findViewById(R.id.gridView);
                    final String[] maters = {"m1.png", "m2.png", "m3.png", "m4.png", "m5.png", "m6.png", "m7.png"};
                    materialGridView.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return maters.length;
                        }

                        @Override
                        public Object getItem(int position) {
                            return null;
                        }

                        @Override
                        public long getItemId(int position) {
                            return 0;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            if (convertView == null) {

                                convertView = View.inflate(SceneInquestActivity.this, R.layout.mater_gv_item, null);
                            }
                            ImageView iv = (ImageView) convertView.findViewById(R.id.iv);

                            String mater = maters[position];
                            File file = new File("/storage/emulated/0/Android/data/com.zhang.okinglawenforcementphone/cache/" + mater);

                            if (!file.exists()) {
                                getAssetFileToCacheDir(mater);
                            }
                            Uri parse = Uri.parse("/storage/emulated/0/Android/data/com.zhang.okinglawenforcementphone/cache/" + maters[position]);
                            iv.setImageURI(parse);
                            return convertView;
                        }
                    });
                    materialGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            String mater = maters[position];
                            mWhiteBoardFragment.addPhotoByPath("/storage/emulated/0/Android/data/com.zhang.okinglawenforcementphone/cache/" + mater);
                        }
                    });
                }
                mDialogUtil.showBottomDialog(SceneInquestActivity.this, mContentView,200f);


                return false;
            }
        });
        mWhiteBoardFragment = WhiteBoardFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.scene_in_quest_content, mWhiteBoardFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }


}
