package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogEditSureCancel;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.MissionMemberAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.MemberOV;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.views.SignatureView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MemberSignActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_add)
    Button mBtnAdd;
    @BindView(R.id.complete_button)
    Button mCompleteButton;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.signature_View)
    SignatureView mSignatureView;
    @BindView(R.id.clear_btn)
    Button mClearBtn;
    @BindView(R.id.save_btn)
    Button mSaveBtn;
    @BindView(R.id.member_listView)
    RecyclerView mMemberListView;
    @BindView(R.id.sdv)
    ImageView mSdv;
    private List<GreenMember> memberList = new ArrayList<>();
    private MissionMemberAdapter memberAdapter;
    private GreenMissionTask missionTask;
    private int selection = -1;

    private RxDialogSureCancel mRxDialogSureCancel;
    private RxDialogEditSureCancel mRxDialogEditSureCancel;
    private Unbinder mBind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_sign);
        mBind = ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        long id = getIntent().getLongExtra("id", -1L);
        if (id != -1L) {
            missionTask = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder().where(GreenMissionTaskDao.Properties.Id.eq(id)).unique();
            memberList = missionTask.getMembers();
        }

        mSignatureView.setCanPaint(false);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mMemberListView.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.HORIZONTAL, false));

        memberAdapter = new MissionMemberAdapter(R.layout.member_sign_item_layout, memberList);
        mMemberListView.setAdapter(memberAdapter);
        memberAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                selection = position;
                mNameTv.setText(memberList.get(position).getUsername());
                mSignatureView.clear();
                mSignatureView.setCanPaint(true);

                GreenMember greenMember = memberList.get(position);
                if (greenMember.getSignPic() == null) {
                    mClearBtn.setVisibility(View.VISIBLE);
                    mSaveBtn.setVisibility(View.VISIBLE);
                    mSdv.setVisibility(View.GONE);
                    mSignatureView.setVisibility(View.VISIBLE);
                } else {
                    mClearBtn.setVisibility(View.GONE);
                    mSaveBtn.setVisibility(View.GONE);
                    mSdv.setVisibility(View.VISIBLE);
                    mSignatureView.setVisibility(View.GONE);
                    Glide.with(MemberSignActivity.this)
                            .load(Uri.parse("file://" + greenMember.getSignPic()))
                            .into(mSdv);
                }

            }
        });
        memberAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                final GreenMember member = memberList.get(position);
                if (!TextUtils.isEmpty(member.getUserid())) {
                    RxToast.warning(BaseApplication.getApplictaion(), "该队员不能删除", Toast.LENGTH_SHORT).show();
                } else {
                    if (member.getSignPic() != null) {
                        RxToast.warning(BaseApplication.getApplictaion(), "已签名不能删除", Toast.LENGTH_SHORT).show();

                    } else {
                        if (mRxDialogSureCancel == null) {

                            mRxDialogSureCancel = new RxDialogSureCancel(MemberSignActivity.this);
                            mRxDialogSureCancel.setContent("是否删除该队员？");
                        }
                        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().delete(member);
                                memberAdapter.remove(position);
                                mRxDialogSureCancel.cancel();
                            }
                        });
                        mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mRxDialogSureCancel.cancel();
                            }
                        });

                        mRxDialogSureCancel.show();
                    }

                }
                return false;

            }
        });

    }

    private void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @OnClick({R.id.btn_add, R.id.complete_button, R.id.clear_btn, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                if (mRxDialogEditSureCancel == null) {

                    mRxDialogEditSureCancel = new RxDialogEditSureCancel(MemberSignActivity.this, false, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            dialogInterface.cancel();
                        }
                    });
                }
                mRxDialogEditSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String edname = mRxDialogEditSureCancel.getEditText().getText().toString().trim();
                        if (!TextUtils.isEmpty(edname)) {
                            GreenMember member = new GreenMember();
                            member.setUsername(edname);
                            member.setPost("组员");
                            member.setGreenMemberId(missionTask.getId());
                            GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().insert(member);
                            memberAdapter.addData(member);
                            mRxDialogEditSureCancel.cancel();
                            RxToast.success(BaseApplication.getApplictaion(), "添加队员成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mRxDialogEditSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRxDialogEditSureCancel.cancel();
                    }
                });
                mRxDialogEditSureCancel.show();

                break;
            case R.id.complete_button:
                MemberOV memberOV = new MemberOV();
                memberOV.setGreenMembers(memberList);
                EventBus.getDefault().post(memberOV);
                finish();
                break;
            case R.id.clear_btn:
                mSignatureView.clear();
                break;
            case R.id.save_btn:
                //提示弹窗
                if (mRxDialogSureCancel == null) {

                    mRxDialogSureCancel = new RxDialogSureCancel(MemberSignActivity.this);
                }
                mRxDialogSureCancel.setContent("保存签名后不能修改，是否继续？");
                mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRxDialogSureCancel.cancel();
                        if (selection != -1) {
                            Schedulers.io().createWorker().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap bitmap = mSignatureView.save();

                                    if (bitmap != null) {


                                        File signatureFile = new File(Environment.getExternalStorageDirectory() + "/oking/mission_signature", DateFormat
                                                .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
                                                + ".jpg");

                                        try {
                                            OutputStream os = new FileOutputStream(signatureFile);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                                            os.flush();
                                            os.close();
//                                            bitmap.recycle();
                                            bitmap = null;
                                            System.gc();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                       final GreenMember greenMember = memberAdapter.getData().get(selection);

                                        if (greenMember != null) {
                                            if (greenMember.getSignPic() != null && new File(FileUtil.praseUritoPath(MemberSignActivity.this, Uri.parse(greenMember.getSignPic()))).exists()) {

                                                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        RxToast.warning(BaseApplication.getApplictaion(), "已签名，不能再次签名！", Toast.LENGTH_SHORT, true).show();

                                                    }
                                                });
                                                signatureFile.delete();
                                            } else {
                                                Log.i("Oking", Uri.fromFile(signatureFile).getPath());
                                                greenMember.setSignPic(signatureFile.getPath());
                                            }



                                            AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                                @Override
                                                public void run() {
                                                    memberAdapter.setData(selection,greenMember);
                                                    mClearBtn.setVisibility(View.INVISIBLE);
                                                    mSaveBtn.setVisibility(View.INVISIBLE);
                                                }
                                            });

                                            GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().update(greenMember);
                                        }
                                    } else {
                                        AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                            @Override
                                            public void run() {
                                                RxToast.warning(BaseApplication.getApplictaion(), "未签名不能保存！", Toast.LENGTH_SHORT, true).show();

                                            }
                                        });

                                    }
                                }
                            });

                        } else {
                            RxToast.warning(BaseApplication.getApplictaion(), "请选择要签名的人员", Toast.LENGTH_SHORT, true).show();

                        }

                    }
                });

                mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRxDialogSureCancel.cancel();
                    }
                });
                mRxDialogSureCancel.show();

                break;
            default:
                break;
        }
    }
}
