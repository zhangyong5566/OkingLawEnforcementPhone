package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.EquipmentRecyAdapter;
import com.zhang.okinglawenforcementphone.adapter.SourceArrayRecyAdapter;
import com.zhang.okinglawenforcementphone.adapter.SpinnerArrayAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenEquipment;
import com.zhang.okinglawenforcementphone.beans.GreenEquipmentDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.SourceArrayOV;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadEquipmentContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadEquipmentPresenter;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tv_taskname)
    TextInputEditText mTvTaskname;
    @BindView(R.id.type_nature)
    TextInputEditText mTypeNature;
    @BindView(R.id.tv_tasktype)
    TextInputEditText mTvTasktype;
    @BindView(R.id.plan_spinner)
    TextInputEditText mPlanSpinner;
    @BindView(R.id.item_spinner)
    TextInputEditText mItemSpinner;
    @BindView(R.id.area_TextView)
    TextInputEditText mAreaTextView;
    @BindView(R.id.tv_description)
    TextInputEditText mTvDescription;
    @BindView(R.id.part_flow_layout)
    TagFlowLayout mPartFlowLayout;
    @BindView(R.id.part_other_editText)
    EditText mPartOtherEditText;
    @BindView(R.id.equipment_textView)
    TextView mEquipmentTextView;
    @BindView(R.id.edit_Equipment_Btn)
    Button mEditEquipmentBtn;
    Unbinder unbinder;
    @BindView(R.id.tl_plan)
    TextInputLayout mTlPlan;
    @BindView(R.id.tl_item)
    TextInputLayout mTlItem;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private GreenMissionTask mGreenMissionTask;
    private GreenMissionLog mGreenMissionLog;
    private List<String> partList = new ArrayList<String>();
    private TagAdapter partTagAdapter;
    private boolean mIsSelected = false;
    private String mFlowTagPos;
    private String parts = "";
    private DialogUtil mDialogUtil;
    private DialogUtil mDialogEquipentUtil;
    private View mButtonDailog;
    private TextView mTv_title;
    private View mEquipentView;
    private Spinner mSpinnerEquipentType;
    private RecyclerView mCanSelectEquipentRcy;
    private EquipmentRecyAdapter mEquipmentRecyAdapter;
    private RxDialogLoading mRxDialogLoading;
    private Button mSaveBtn;
    private Button mCloseBtn;
    private LoadEquipmentPresenter mLoadEquipmentPresenter;
    private ArrayList<GreenEquipment> canSelectList = new ArrayList<>();
    private String[] mPlanArray;
    private String[] mMattersArray;
    private ArrayList<SourceArrayOV> mPlanArrayOVS;
    private ArrayList<SourceArrayOV> mMattersArrayOVS;
    private SourceArrayRecyAdapter mSourceArrayRecyAdapter;
    private int mSelePlanPos;
    private int mSeleMattersPos;

    public TaskInfoFragment() {
        // Required empty public constructor
    }

    public static TaskInfoFragment newInstance(String param1, String param2) {
        TaskInfoFragment fragment = new TaskInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_task_info, container, false);
        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        return mInflate;
    }

    private void initData() {


        //{id=24, approved_person='da40394f32624b92b443d0fcaa73dbc2',
        // approved_person_name='张伟民', approved_time=null,
        // begin_time=1534385700000, create_time=null,
        // delivery_time=null, end_time=1534694400000,
        // execute_end_time=null,
        // execute_start_time=1534385700957,
        // fbdw='广东省水利厅水利水政监察局', fbr='null',
        // taskid='15343859421543498132', jjcd='1',
        // jsdw='null', jsr='null', publisher='null',
        // publisher_name='邓桂林', receiver='null',
        // receiver_name='null', rwly='2',
        // rwqyms='珠江', spr='null',
        // spyj='null', status='4',
        // task_area='珠江',
        // task_content='？？！',
        // task_name='广东省水利厅【急】201808012',
        // task_type=0, typename='联合执法',
        // typeoftask='0',
        // userid='da40394f32624b92b443d0fcaa73dbc2',
        // examine_status=-1, drawLaLoType=0,
        // mcoordinateJson='null',
        // members=[GreenMember{id=67, servId='null', depatid='null', depatname='null', remark='null', zfzh='null', account='13600000000', greenMemberId=24, taskid='null', userid='da40394f32624b92b443d0fcaa73dbc2', username='邓桂林', post='负责人', signPic='null'}, GreenMember{id=68, servId='null', depatid='001001', depatname='广东省水利厅水利水政监察局', remark='CBR', zfzh='O038606', account='null', greenMemberId=24, taskid='null', userid='f0d387bb2e1e4028a5a2171a4939a315', username='蔡桂煌', post='组员', signPic='null'}], daoSession=com.zhang.okinglawenforcementphone.beans.DaoSession@351b5e2, myDao=com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao@e017d73}
        mTvTaskname.setText(mGreenMissionTask.getTask_name());
        mTypeNature.setText(mGreenMissionTask.getTypename());
        if ("0".equals(mGreenMissionTask.getTypeoftask())) {
            mTvTasktype.setText("河道管理");
        } else if ("1".equals(mGreenMissionTask.getTypeoftask())) {
            mTvTasktype.setText("河道采砂");
        } else if ("2".equals(mGreenMissionTask.getTypeoftask())) {
            mTvTasktype.setText("水资源管理");
        } else if ("3".equals(mGreenMissionTask.getTypeoftask())) {
            mTvTasktype.setText("水土保持管理");
        } else if ("4".equals(mGreenMissionTask.getTypeoftask())) {
            mTvTasktype.setText("水利工程管理");
        }
        mAreaTextView.setText(mGreenMissionTask.getRwqyms());
        mTvDescription.setText(mGreenMissionTask.getTask_content());

        partList = Arrays.asList("公安", "海事", "环保", "航道", "交通", "国土", "城管", "纪检");
        partTagAdapter = new TagAdapter<String>(partList) {
            @Override
            public View getView(FlowLayout parent, int position, String parts) {
                View inflate = View.inflate(BaseApplication.getApplictaion(), R.layout.tag_item, null);
                TextView tv_tag = inflate.findViewById(R.id.tv_tag);
                tv_tag.setText(parts);
                return inflate;
            }
        };
        String flowTagPos = mGreenMissionLog.getFlowTagPos();
        if (!TextUtils.isEmpty(flowTagPos)) {
            String[] split = flowTagPos.split(",");
            Set<Integer> integers = new HashSet<>();
            for (String s : split) {
                parts += partList.get(Integer.parseInt(s)) + ",";
                integers.add(Integer.parseInt(s));
            }
            partTagAdapter.setSelectedList(integers);
        }

        mPartFlowLayout.setAdapter(partTagAdapter);
        mPartFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                mIsSelected = true;
                mFlowTagPos = "";
                parts = "";
                Iterator<Integer> it = selectPosSet.iterator();
                while (it.hasNext()) {
                    Integer next = it.next();
                    parts += partList.get(next) + ",";
                    mFlowTagPos += next + ",";
                }

                if (!"".equals(mPartOtherEditText.getText().toString())) {
                    parts += mPartOtherEditText.getText().toString() + ",";
                }


            }
        });
        if (mGreenMissionTask.getStatus().equals("100")) {
            mPartFlowLayout.setEnabled(false);
        } else if (mGreenMissionTask.getStatus().equals("5")) {
            mPartFlowLayout.setEnabled(false);
            mEditEquipmentBtn.setVisibility(View.GONE);
            mPlanSpinner.setClickable(false);
            mItemSpinner.setClickable(false);
        } else if (mGreenMissionTask.getStatus().equals("9")) {
            mPartFlowLayout.setEnabled(false);
            mPlanSpinner.setEnabled(false);
            mItemSpinner.setEnabled(false);
            mEditEquipmentBtn.setVisibility(View.GONE);
        }

        mPlanArray = BaseApplication.getApplictaion().getResources().getStringArray(R.array.spinner_plan);
        mPlanArrayOVS = new ArrayList<>();
        for (String s : mPlanArray) {
            SourceArrayOV sourceArrayOV = new SourceArrayOV();
            sourceArrayOV.setType(0);
            sourceArrayOV.setSource(s);
            mPlanArrayOVS.add(sourceArrayOV);
        }

        mMattersArray = BaseApplication.getApplictaion().getResources().getStringArray(R.array.spinner_matters);

        mMattersArrayOVS = new ArrayList<>();
        for (String s : mMattersArray) {
            SourceArrayOV sourceArrayOV = new SourceArrayOV();
            sourceArrayOV.setType(1);
            sourceArrayOV.setSource(s);
            mMattersArrayOVS.add(sourceArrayOV);
        }

        if (mDialogUtil == null) {
            mDialogUtil = new DialogUtil();
            mButtonDailog = View.inflate(BaseApplication.getApplictaion(), R.layout.maptask_dialog, null);
            mTv_title = mButtonDailog.findViewById(R.id.tv_title);
            RecyclerView recyList = mButtonDailog.findViewById(R.id.recy_task);
            recyList.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
            recyList.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 3, Color.TRANSPARENT));
            mSourceArrayRecyAdapter = new SourceArrayRecyAdapter(R.layout.source_item, null);
            mSourceArrayRecyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
            recyList.setAdapter(mSourceArrayRecyAdapter);
            mSourceArrayRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    List<SourceArrayOV> datas = adapter.getData();
                    SourceArrayOV sourceArrayOV = datas.get(position);
                    switch (sourceArrayOV.getType()) {
                        case 0:
                            mSelePlanPos = position;
                            mPlanSpinner.setText(sourceArrayOV.getSource());
                            break;
                        case 1:
                            mSeleMattersPos = position;
                            mItemSpinner.setText(sourceArrayOV.getSource());
                            break;
                        default:
                            break;
                    }
                    mDialogUtil.cancelDialog();
                }
            });
        }
        mPlanSpinner.setText(mPlanArray[mGreenMissionLog.getPlan()]);
        mItemSpinner.setText(mMattersArray[mGreenMissionLog.getItem()]);
        mEquipmentTextView.setText(mGreenMissionLog.getEquipment());
    }

    public void setMission(GreenMissionTask mission) {
        mGreenMissionTask = mission;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setGreenMissionLog(GreenMissionLog greenMissionLog) {
        mGreenMissionLog = greenMissionLog;
    }

    @OnClick({R.id.edit_Equipment_Btn, R.id.tl_plan, R.id.tl_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_Equipment_Btn:
                if (mDialogEquipentUtil == null) {
                    mDialogEquipentUtil = new DialogUtil();
                    mEquipentView = View.inflate(BaseApplication.getApplictaion(), R.layout.set_equipent_dialog, null);
                    mSpinnerEquipentType = mEquipentView.findViewById(R.id.type_spinner);

                    String[] typeArray = getResources().getStringArray(R.array.spinner_equipment_type);
                    SpinnerArrayAdapter typeArrayAdapter = new SpinnerArrayAdapter(typeArray);
                    mSpinnerEquipentType.setAdapter(typeArrayAdapter);
                    mSpinnerEquipentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            setCanSelectList();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    mCanSelectEquipentRcy = mEquipentView.findViewById(R.id.canSelect_rcy);
                    mCanSelectEquipentRcy.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
                    mCanSelectEquipentRcy.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 1, BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain4)));

                    mEquipmentRecyAdapter = new EquipmentRecyAdapter(null);
                    mCanSelectEquipentRcy.setAdapter(mEquipmentRecyAdapter);
                    mSaveBtn = mEquipentView.findViewById(R.id.save_button);
                    mCloseBtn = mEquipentView.findViewById(R.id.close_button);
                    getEquipentData();

                    mSaveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<GreenEquipment> checkItem = mEquipmentRecyAdapter.getCheckItem();
                            if (checkItem != null && checkItem.size() > 0) {
                                String equipmentString = createEquipmentString(checkItem);
                                mEquipmentTextView.setText(equipmentString);

                            }
                            mDialogEquipentUtil.cancelDialog();
                        }
                    });

                    mCloseBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDialogEquipentUtil.cancelDialog();
                        }
                    });
                }
                mDialogEquipentUtil.showBottomDialog(getActivity(), mEquipentView, 400f);
                break;
            case R.id.tl_plan:
                mTv_title.setText("计划");
                mSourceArrayRecyAdapter.setNewData(mPlanArrayOVS);
                mDialogUtil.showBottomDialog(getActivity(), mButtonDailog, 200f);
                break;
            case R.id.tl_item:
                mTv_title.setText("事项");
                mSourceArrayRecyAdapter.setNewData(mMattersArrayOVS);
                mDialogUtil.showBottomDialog(getActivity(), mButtonDailog, 350f);
                break;
            default:
                break;

        }



    }


    private void getEquipentData() {
        if (mRxDialogLoading == null) {
            mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mRxDialogLoading.cancel();
                }
            });
        }
        mRxDialogLoading.setLoadingText("正在获取装备数据...");
        mRxDialogLoading.show();
        if (mLoadEquipmentPresenter == null) {
            mLoadEquipmentPresenter = new LoadEquipmentPresenter(new LoadEquipmentContract.View() {
                @Override
                public void loadEquipmentSucc(final String result) {
                    canSelectList.clear();
                    mRxDialogLoading.cancel();
                    Schedulers.io().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                List<GreenEquipment> greenEquipments = GreenDAOManager.getInstence().getDaoSession().getGreenEquipmentDao().queryBuilder().where(GreenEquipmentDao.Properties.DeptId.eq(OkingContract.CURRENTUSER.getDept_id())).list();
                                if (greenEquipments.size() > 0) {
                                    for (GreenEquipment greenEquipment : greenEquipments) {
                                        GreenDAOManager.getInstence().getDaoSession().getGreenEquipmentDao().deleteByKey(greenEquipment.getId());
                                    }

                                }
                                JSONArray jsonArray = new JSONArray(result);

                                GreenEquipment equipmentHead = new GreenEquipment();
                                equipmentHead.setItemType(0);
                                canSelectList.add(equipmentHead);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    GreenEquipment equipment = new GreenEquipment();
                                    equipment.setItemType(1);
                                    equipment.setDeptId(OkingContract.CURRENTUSER.getDept_id());
                                    equipment.setType(jsonObject.getString("type"));
                                    equipment.setType2(jsonObject.getString("type2"));
                                    equipment.setMc1(jsonObject.getString("mc1"));
                                    equipment.setMc2(jsonObject.getString("mc2"));
                                    equipment.setLy(jsonObject.getString("ly"));
                                    equipment.setValue(jsonObject.getString("value"));
                                    equipment.setRemarks(jsonObject.getString("remarks"));
                                    canSelectList.add(equipment);
                                }

                                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        mEquipmentRecyAdapter.setNewData(canSelectList);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }

                @Override
                public void loadEquipmentFail(Throwable ex) {
                    mRxDialogLoading.cancel();
                    Schedulers.io().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            final List<GreenEquipment> greenEquipments = GreenDAOManager.getInstence().getDaoSession().getGreenEquipmentDao().queryBuilder().where(GreenEquipmentDao.Properties.DeptId.eq(OkingContract.CURRENTUSER.getDept_id())).list();

                            AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    if (greenEquipments.size() > 0) {
                                        mEquipmentRecyAdapter.setNewData(greenEquipments);
                                        GreenEquipment equipmentHead = new GreenEquipment();
                                        equipmentHead.setItemType(0);
                                        mEquipmentRecyAdapter.addData(0, equipmentHead);
                                    } else {
                                        //空
                                    }
                                }
                            });
                        }
                    });

                }
            });

        }
        mLoadEquipmentPresenter.loadEquipment();
    }

    private String createEquipmentString(List<GreenEquipment> checkItem) {
        String dataStr = "", trafficStr = "", communicationStr = "", forensicsStr = "", officeStr = "";

        for (int i = 0; i < checkItem.size(); i++) {
            GreenEquipment e = checkItem.get(i);
            if ("交通工具".equals(e.getMc1())) {
                trafficStr = trafficStr + e.getValue() + ",";
            } else if ("通讯工具".equals(e.getMc1())) {
                communicationStr = communicationStr + e.getValue() + ",";
            } else if ("取证工具".equals(e.getMc1())) {
                forensicsStr = forensicsStr + e.getValue() + ",";
            } else if ("办公设备及场所".equals(e.getMc1())) {
                officeStr = officeStr + e.getValue() + ",";
            }
        }

        if (!"".equals(trafficStr)) {
            dataStr += "交通工具：" + trafficStr.subSequence(0, trafficStr.length() - 1) + "  ";
        }

        if (!"".equals(communicationStr)) {
            dataStr += "通讯工具：" + communicationStr.subSequence(0, communicationStr.length() - 1) + "  ";
        }

        if (!"".equals(forensicsStr)) {
            dataStr += "取证工具：" + forensicsStr.subSequence(0, forensicsStr.length() - 1);
        }

        if (!"".equals(officeStr)) {
            dataStr += "办公设备及场所：" + officeStr.subSequence(0, officeStr.length() - 1);
        }


        return dataStr;
    }

    private void setCanSelectList() {
        ArrayList<GreenEquipment> aList = new ArrayList<GreenEquipment>();
        if ("0".equals(String.valueOf(mSpinnerEquipentType.getSelectedItemPosition()))) {           //全部
            for (GreenEquipment equipment : canSelectList) {
                aList.add(equipment);
            }

        } else if ("1".equals(String.valueOf(mSpinnerEquipentType.getSelectedItemPosition()))) {     //
            for (GreenEquipment equipment : canSelectList) {
                if ("交通工具".equals(equipment.getMc1()) || equipment.getItemType() == 0) {
                    aList.add(equipment);
                }
            }
        } else if ("2".equals(String.valueOf(mSpinnerEquipentType.getSelectedItemPosition()))) {
            for (GreenEquipment equipment : canSelectList) {
                if ("通讯工具".equals(equipment.getMc1()) || equipment.getItemType() == 0) {
                    aList.add(equipment);
                }
            }

        } else if ("3".equals(String.valueOf(mSpinnerEquipentType.getSelectedItemPosition()))) {
            for (GreenEquipment equipment : canSelectList) {
                if ("取证工具".equals(equipment.getMc1()) || equipment.getItemType() == 0) {
                    aList.add(equipment);
                }
            }

        } else if ("4".equals(String.valueOf(mSpinnerEquipentType.getSelectedItemPosition()))) {
            for (GreenEquipment equipment : canSelectList) {
                if ("办公设备及场所".equals(equipment.getMc1()) || equipment.getItemType() == 0) {
                    aList.add(equipment);
                }
            }

        }
        mEquipmentRecyAdapter.setNewData(aList);
    }

    public void saveTaskInfo() {
        if (parts.length() > 0 && parts.endsWith(",")) {
            parts = parts.substring(0, parts.length() - 1);
        }
        mGreenMissionLog.setOther_part(parts);
        if (mIsSelected) {
            mGreenMissionLog.setFlowTagPos(mFlowTagPos);
        }
        mGreenMissionLog.setPlan(mSelePlanPos);
        mGreenMissionLog.setItem(mSeleMattersPos);
        mGreenMissionLog.setEquipment(mEquipmentTextView.getText().toString().trim());
        GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().update(mGreenMissionLog);
    }
}
