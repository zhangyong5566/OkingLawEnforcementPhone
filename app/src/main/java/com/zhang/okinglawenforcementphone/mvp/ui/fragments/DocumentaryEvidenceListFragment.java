package com.zhang.okinglawenforcementphone.mvp.ui.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxDialogSure;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.okinglawenforcementphone.GreenDAOMannager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.DocumentaryEvidenceListRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenCase;
import com.zhang.okinglawenforcementphone.beans.GreenEvidence;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceSZOV;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadEvidenceContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadEvidencePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentaryEvidenceListFragment extends Fragment {

    private GreenCase mycase;

    private RecyclerView ryMain;
    private ArrayList<GreenEvidence> evidences = new ArrayList<>();
    private Button add_evidence_button;


    private SimpleDateFormat mSimpleDateFormat;
    private DocumentaryEvidenceListRecyAdapter mDocumentaryEvidenceListRecyAdapter;
    private RxDialogLoading mRxDialogLoading;
    private RxDialogSure mRxDialogSure;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam2;
    private RxDialogSureCancel mRxDialogSureCancel;
    private View mInflate;
    private List<GreenMedia> mPicGreenMedias = new ArrayList<>();
    private UploadEvidencePresenter mUploadEvidencePresenter;

    public DocumentaryEvidenceListFragment() {
        // Required empty public constructor
    }

    public static DocumentaryEvidenceListFragment newInstance(String param2) {
        DocumentaryEvidenceListFragment fragment = new DocumentaryEvidenceListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_documentary_evidence_list, container, false);
        }
        EventBus.getDefault().register(this);
        initView(mInflate);
        return mInflate;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent1(GreenEvidenceSZOV event) {
        if (event.getType() == 2) {            //添加征据
            evidences.add(event.getGreenEvidence());
            mDocumentaryEvidenceListRecyAdapter.setNewData(evidences);
        } else {
            loadEvidence();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void initView(View rootView) {
        ryMain = rootView.findViewById(R.id.ry_main);
        ryMain.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mDocumentaryEvidenceListRecyAdapter = new DocumentaryEvidenceListRecyAdapter(R.layout.list_item_documentaryevidence, null);
        ryMain.setAdapter(mDocumentaryEvidenceListRecyAdapter);
        loadEvidence();
        mDocumentaryEvidenceListRecyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.upload_button:            //上传
                        if (mRxDialogLoading == null) {

                            mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    dialogInterface.cancel();
                                }
                            });
                            mRxDialogLoading.setLoadingText("上传数据中...");
                        }
                        mRxDialogLoading.show();

                        saveEvidence(evidences.get(position));

                        break;
                    case R.id.delete_button:            //删除
                        if (mRxDialogSureCancel == null) {

                            mRxDialogSureCancel = new RxDialogSureCancel(getActivity());
                            mRxDialogSureCancel.setTitle("提示");
                            mRxDialogSureCancel.setContent("是否删除证据？");
                        }
                        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GreenDAOMannager.getInstence().getDaoSession().getGreenEvidenceDao().delete(evidences.get(position));
                                evidences.remove(position);
                                mDocumentaryEvidenceListRecyAdapter.setNewData(evidences);
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
                        break;
                    case R.id.edit_button:              //查看编辑
                        if (((Button) view).getText().equals("查看")) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            DocumentaryEvidenceFragment documentaryEvidenceFragment = DocumentaryEvidenceFragment.newInstance(0);
                            documentaryEvidenceFragment.setGreenCase(mycase, evidences.get(position));
                            ft.add(R.id.sub_fragment_root, documentaryEvidenceFragment).commit();
                        } else {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            DocumentaryEvidenceFragment documentaryEvidenceFragment = DocumentaryEvidenceFragment.newInstance(1);
                            documentaryEvidenceFragment.setGreenCase(mycase, evidences.get(position));
                            ft.add(R.id.sub_fragment_root, documentaryEvidenceFragment).commit();

                        }

                        break;
                    default:
                        break;
                }
            }
        });


        add_evidence_button = (Button) rootView.findViewById(R.id.add_evidence_button);
        add_evidence_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DocumentaryEvidenceFragment documentaryEvidenceFragment = DocumentaryEvidenceFragment.newInstance(2);
                documentaryEvidenceFragment.setGreenCase(mycase, null);
                ft.add(R.id.sub_fragment_root, documentaryEvidenceFragment, "documentaryEvidenceFragment").commit();
            }
        });
    }


    /**
     * 上传数据
     *
     * @param evidence
     */
    private void saveEvidence(final GreenEvidence evidence) {

        Map<String, Object> fields = new HashMap<>();
        if (evidence.getZJID() != null) {

            fields.put("zjid", evidence.getZJID());
        }

        if (evidence.getAJID() != null) {
            fields.put("ajid", evidence.getAJID());
        }
        if (evidence.getZJLX() != null) {
            fields.put("zjlx", evidence.getZJLX());
        }
        if (evidence.getZJMC() != null) {
            fields.put("zjmc", evidence.getZJMC());
        }
        if (evidence.getZJLY() != null) {
            fields.put("zjly", evidence.getZJLY());
        }
        if (evidence.getZJNR() != null) {
            fields.put("zjnr", evidence.getZJNR());
        }
        if (evidence.getSL() != null) {
            fields.put("sl", evidence.getSL());
        }


        if (((Long) evidence.getCJSJ()) != null) {
            fields.put("cjsj", mSimpleDateFormat.format(evidence.getCJSJ()));
        }
        if (evidence.getCJR() != null) {
            fields.put("cjr", evidence.getCJR());
        }
        if (evidence.getCJDD() != null) {
            fields.put("cjdd", evidence.getCJDD());
        }
        if (evidence.getJZR() != null) {
            fields.put("jzr", evidence.getJZR());
        }
        if (evidence.getDW() != null) {
            fields.put("dw", evidence.getDW());
        }
        if (evidence.getBZ() != null) {
            fields.put("bz", evidence.getBZ());
        }
        if (OkingContract.CURRENTUSER != null) {
            fields.put("scr", OkingContract.CURRENTUSER.getUserName());
        }
        fields.put("scsj", mSimpleDateFormat.format(System.currentTimeMillis()));

        if (evidence.getZT() != null) {
            fields.put("zt", evidence.getZT());
        }
        if (evidence.getWSID() != null) {
            fields.put("wsid", evidence.getWSID());
        }
        if (evidence.getLXMC() != null) {
            fields.put("lxmc", evidence.getLXMC());
        }
        if (evidence.getZJLYMC() != null) {
            fields.put("zjlymc", evidence.getZJLYMC());
        }
        if (evidence.getYS() != null) {
            fields.put("ys", evidence.getYS());
        }


        List<GreenMedia> greenMedias = evidence.getGreenMedia();

        for (GreenMedia greenMedia : greenMedias) {
            if (greenMedia.getType() == 1) {
                mPicGreenMedias.add(greenMedia);
            }

        }


        if (mUploadEvidencePresenter == null) {
            mUploadEvidencePresenter = new UploadEvidencePresenter(new UploadEvidenceContract.View() {
                @Override
                public void uploadEvidenceSucc(String result) {
                    mRxDialogLoading.cancel();
                    checkChangeState(evidence);
                }

                @Override
                public void uploadEvidenceFail(Throwable ex) {
                    mRxDialogLoading.cancel();
                }
            });
        }
        mUploadEvidencePresenter.uploadEvidence(fields, evidence, mPicGreenMedias);


    }


    private void loadEvidence() {
        evidences.clear();
        if (mycase != null) {
            mycase.resetGreenEvidence();
            for (int i = 0; i < mycase.getGreenEvidence().size(); i++) {
                if ("SZ".equals(mycase.getGreenEvidence().get(i).getZJLX())) {
                    evidences.add(mycase.getGreenEvidence().get(i));
                }
            }
            mDocumentaryEvidenceListRecyAdapter.setNewData(evidences);
        }
    }


    private void checkChangeState(GreenEvidence evidence) {

        evidence.setIsUpload(true);
        GreenDAOMannager.getInstence().getDaoSession().getGreenEvidenceDao().update(evidence);

        mDocumentaryEvidenceListRecyAdapter.notifyDataSetChanged();
        GreenDAOMannager.getInstence().getDaoSession().getGreenCaseDao().update(mycase);
        mRxDialogLoading.cancel();

        if (mRxDialogSure == null) {

            mRxDialogSure = new RxDialogSure(getActivity());
            mRxDialogSure.setContent("上传成功");
            mRxDialogSure.setTitle("提示");
        }
        mRxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRxDialogSure.cancel();
            }
        });
        mRxDialogSure.show();

    }

    public void setGreenCase(GreenCase greenCase) {
        this.mycase = greenCase;
    }
}
