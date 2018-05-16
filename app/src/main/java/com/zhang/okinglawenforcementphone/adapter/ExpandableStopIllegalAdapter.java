package com.zhang.okinglawenforcementphone.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.model.IDCardResult;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.AppUtil;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.baselib.utils.TextUtil;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.WrittenItemBean;
import com.zhang.okinglawenforcementphone.beans.WrittenRecordLevel0;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/5/11/011.
 */

public class ExpandableStopIllegalAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;
    public static final int TYPE_LEVEL_3 = 3;
    private RadioButton mRb_natural_person;
    private TextInputEditText mTet_parties_concerned_natural;
    private TextInputEditText mTet_sex_natural;
    private TextInputEditText mTet_card_natural;
    private TextInputEditText mTet_phone_natural;
    private TextInputEditText mTet_address_natural;
    private TextInputEditText mTet_parties_concerned_other;
    private TextInputEditText mTet_position_other;
    private TextInputEditText mTet_phone_other;
    private TextInputEditText mTet_representative_other;
    private TextInputEditText mTet_credit_code_oher;
    private TextInputEditText mTet_address_other;
    private TextView mBt_idcard;
    private EditText mEt_illegal_facts;
    private EditText mEt_legal_provisions1;
    private EditText mEt_legal_provisions2;
    private TextInputEditText mTet_contact;
    private TextInputEditText mTet_phone;
    private TextInputEditText mTet_addr;
    private Button mBt_prin;
    private RxDialogLoading mRxDialogLoading;
    private Activity mActivity;
    private String mNaturalInfo;
    private int mMYear;
    private int mMMonth;
    private int mMDay;
    private TextView mTv_time;

    public ExpandableStopIllegalAdapter(Activity activity, List<MultiItemEntity> data) {
        super(data);
        this.mActivity = activity;
        addItemType(TYPE_LEVEL_0, R.layout.activity_mission_recor_level0);
        addItemType(TYPE_LEVEL_1, R.layout.stop_thellegal_activities_info1);
        addItemType(TYPE_LEVEL_2, R.layout.stop_thellegal_activities_info2);
        addItemType(TYPE_LEVEL_3, R.layout.stop_thellegal_activities_info3);

        Calendar c = Calendar.getInstance();//
        // 获取当前年份
        mMYear = c.get(Calendar.YEAR);
        // 获取当前月份
        mMMonth = c.get(Calendar.MONTH) + 1;
        // 获取当日期
        mMDay = c.get(Calendar.DAY_OF_MONTH);


    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final WrittenRecordLevel0 lv0 = (WrittenRecordLevel0) item;
                helper.setText(R.id.title, lv0.subTitle);
                helper.itemView.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        int adapterPosition = helper.getAdapterPosition();
                        List<WrittenItemBean> subItems = lv0.getSubItems();
                        int subItemType = subItems.get(0).getItemType();

                        if (lv0.isExpanded()) {
                            collapse(adapterPosition);


                        } else {
                            expand(adapterPosition);
                        }


                    }

                });
                break;
            case TYPE_LEVEL_1:
                if (mRb_natural_person == null) {

                    mRb_natural_person = helper.getView(R.id.rb_natural_person);
                    mRb_natural_person.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (mRb_natural_person.isChecked()) {
                                mTet_parties_concerned_natural.setEnabled(true);
                                mTet_parties_concerned_other.setEnabled(false);
                                mTet_parties_concerned_other.setText("");
                                mTet_sex_natural.setEnabled(true);
                                mTet_card_natural.setEnabled(true);
                                mTet_phone_natural.setEnabled(true);
                                mTet_phone_other.setEnabled(false);
                                mTet_phone_other.setText("");
                                mTet_address_natural.setEnabled(true);
                                mTet_address_other.setEnabled(false);
                                mTet_address_other.setText("");
                                mTet_position_other.setEnabled(false);
                                mTet_position_other.setText("");
                                mTet_representative_other.setEnabled(false);
                                mTet_representative_other.setText("");
                                mTet_credit_code_oher.setEnabled(false);
                                mTet_credit_code_oher.setText("");
                                mBt_idcard.setEnabled(true);
                            } else {
                                mTet_parties_concerned_natural.setEnabled(false);
                                mTet_parties_concerned_other.setEnabled(true);
                                mTet_sex_natural.setEnabled(false);
                                mTet_card_natural.setEnabled(false);
                                mTet_phone_natural.setEnabled(false);
                                mTet_phone_other.setEnabled(true);
                                mTet_address_natural.setEnabled(false);
                                mTet_address_other.setEnabled(true);
                                mTet_position_other.setEnabled(true);
                                mTet_representative_other.setEnabled(true);
                                mTet_credit_code_oher.setEnabled(true);
                                mBt_idcard.setEnabled(false);
                            }

                        }
                    });
                }

                if (mTet_parties_concerned_natural == null) {

                    mTet_parties_concerned_natural = helper.getView(R.id.tet_parties_concerned_natural);
                }

                if (mTet_sex_natural == null) {

                    mTet_sex_natural = helper.getView(R.id.tet_sex_natural);
                }

                if (mTet_card_natural == null) {

                    mTet_card_natural = helper.getView(R.id.tet_card_natural);
                }

                if (mTet_phone_natural == null) {

                    mTet_phone_natural = helper.getView(R.id.tet_phone_natural);
                }

                if (mTet_address_natural == null) {

                    mTet_address_natural = helper.getView(R.id.tet_address_natural);
                    TextUtil.setEditTextInhibitInputSpace(mTet_address_natural);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_address_natural);
                }

                if (mTet_parties_concerned_other == null) {

                    mTet_parties_concerned_other = helper.getView(R.id.tet_parties_concerned_other);
                }

                if (mTet_position_other == null) {

                    mTet_position_other = helper.getView(R.id.tet_position_other);
                }

                if (mTet_phone_other == null) {

                    mTet_phone_other = helper.getView(R.id.tet_phone_other);
                }

                if (mTet_representative_other == null) {

                    mTet_representative_other = helper.getView(R.id.tet_representative_other);
                    TextUtil.setEditTextInhibitInputSpace(mTet_representative_other);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_representative_other);
                }

                if (mTet_credit_code_oher == null) {

                    mTet_credit_code_oher = helper.getView(R.id.tet_credit_code_oher);
                }

                if (mTet_address_other == null) {

                    mTet_address_other = helper.getView(R.id.tet_address_other);
                    TextUtil.setEditTextInhibitInputSpace(mTet_address_other);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_address_other);
                }

                if (mBt_idcard == null) {

                    mBt_idcard = helper.getView(R.id.bt_idcard);
                    helper.addOnClickListener(R.id.bt_idcard);
                }
                break;
            case TYPE_LEVEL_2:
                if (mEt_illegal_facts == null) {

                    mEt_illegal_facts = helper.getView(R.id.et_illegal_facts);
                }

                if (mEt_legal_provisions1 == null) {

                    mEt_legal_provisions1 = helper.getView(R.id.et_legal_provisions);
                }

                if (mEt_legal_provisions2 == null) {

                    mEt_legal_provisions2 = helper.getView(R.id.et_legal_provisions2);
                }
                break;
            case TYPE_LEVEL_3:
                if (mTet_contact == null) {

                    mTet_contact = helper.getView(R.id.tet_contact);
                    TextUtil.setEditTextInhibitInputSpace(mTet_contact);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_contact);
                }

                if (mTet_phone == null) {

                    mTet_phone = helper.getView(R.id.tet_phone);
                }

                if (mTet_addr == null) {

                    mTet_addr = helper.getView(R.id.tet_addr);
                    TextUtil.setEditTextInhibitInputSpace(mTet_addr);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_addr);
                }

                if (mBt_prin == null) {

                    mBt_prin = helper.getView(R.id.bt_prin);
                    helper.addOnClickListener(R.id.bt_prin);
                }
                if (mTv_time == null) {

                    mTv_time = helper.getView(R.id.tv_time);
                    mTv_time.setText(mMYear + "年" + mMMonth + "月" + mMDay + "日");
                }
                break;
            default:
                break;
        }
    }

    public void setOCRData(IDCardResult result) {
        mTet_sex_natural.setText(result.getGender().toString());
        mTet_parties_concerned_natural.setText(result.getName().toString());
        mTet_card_natural.setText(result.getIdNumber().toString());
        mTet_address_natural.setText(result.getAddress().toString());
    }

    public void print() {
        boolean installApp = AppUtil.isInstallApp(BaseApplication.getApplictaion(), "com.dynamixsoftware.printershare");

        if (installApp) {
            if (mRb_natural_person.isChecked()) {    //当事人为自然人

                final String partiesConcernedNatural = mTet_parties_concerned_natural.getText().toString().trim();
                final String sexNatural = mTet_sex_natural.getText().toString().trim();
                final String tetCardNatural = mTet_card_natural.getText().toString().trim();
                final String tetPhoneNatural = mTet_phone_natural.getText().toString().trim();
                final String tetAddressNatural = mTet_address_natural.getText().toString().trim();
                if (TextUtils.isEmpty(partiesConcernedNatural) && TextUtils.isEmpty(sexNatural)
                        && TextUtils.isEmpty(tetCardNatural) && TextUtils.isEmpty(tetPhoneNatural)
                        && TextUtils.isEmpty(tetAddressNatural)) {
                    RxToast.warning(BaseApplication.getApplictaion(), "填入信息不能有空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mNaturalInfo = "<p>当事人:<u>&nbsp;" + partiesConcernedNatural + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;性别: <u>&nbsp;" + sexNatural + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;身份证号:\n" +
                        "    <u>&nbsp;" + tetCardNatural + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;电话:\n" +
                        "    <u>&nbsp;" + tetPhoneNatural + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;住址:\n" +
                        "    <u>&nbsp;" + tetAddressNatural + "&nbsp;</u>\n" +
                        "</p>";
                //---------------------------------------------------


            } else {
                String partiesConcernedOther = mTet_parties_concerned_other.getText().toString().trim();
                String positionOther = mTet_position_other.getText().toString().trim();
                String phoneOther = mTet_phone_other.getText().toString().trim();
                String representativeOther = mTet_representative_other.getText().toString().trim();
                String creditCodeOher = mTet_credit_code_oher.getText().toString().trim();
                String addressOther = mTet_address_other.getText().toString().trim();

                if (TextUtils.isEmpty(partiesConcernedOther) && TextUtils.isEmpty(positionOther)
                        && TextUtils.isEmpty(phoneOther) && TextUtils.isEmpty(representativeOther)
                        && TextUtils.isEmpty(creditCodeOher) && TextUtils.isEmpty(addressOther)) {
                    RxToast.warning(BaseApplication.getApplictaion(), "填入信息不能有空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mNaturalInfo = "<p>当事人:<u>&nbsp;" + partiesConcernedOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;职务: <u>&nbsp;" + positionOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;电话:\n" +
                        "    <u>&nbsp;" + phoneOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;法定代表人:\n" +
                        "    <u>&nbsp;" + representativeOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;统一社会信用代码:\n" +
                        "    <u>&nbsp;" + creditCodeOher + "&nbsp;</u>\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;住所:<u>&nbsp;" + addressOther + "&nbsp;</u>" +
                        "</p>";

            }

            if (mEt_illegal_facts == null || TextUtils.isEmpty(mEt_illegal_facts.getText().toString().trim())) {
                RxToast.warning("违法事实不能为空");
                return;
            }

            if (mEt_legal_provisions1 == null || TextUtils.isEmpty(mEt_legal_provisions1.getText().toString().trim())) {
                RxToast.warning("法律条款不能为空");
                return;
            }

            if (mEt_legal_provisions2 == null || TextUtils.isEmpty(mEt_legal_provisions2.getText().toString().trim())) {
                RxToast.warning("法律条款不能为空");
                return;
            }

            if (mTet_contact == null || TextUtils.isEmpty(mTet_contact.getText().toString().trim())) {
                RxToast.warning("联系人不能为空");
                return;
            }

            if (mTet_phone == null || TextUtils.isEmpty(mTet_phone.getText().toString().trim())) {
                RxToast.warning("联系电话不能为空");
                return;
            }

            if (mTet_addr == null || TextUtils.isEmpty(mTet_addr.getText().toString().trim())) {
                RxToast.warning("联系地址不能为空");
                return;
            }
            final String illegalFacts = mEt_illegal_facts.getText().toString().trim();
            final String legalProvisions1 = mEt_legal_provisions1.getText().toString().trim();
            final String legalProvisions2 = mEt_legal_provisions2.getText().toString().trim();
            final String contact = mTet_contact.getText().toString().trim();
            final String phone = mTet_phone.getText().toString().trim();
            final String addr = mTet_addr.getText().toString().trim();
            //写文件html
            Schedulers.io().createWorker().schedule(new Runnable() {
                @Override
                public void run() {
                    writeHtml(illegalFacts, legalProvisions1, legalProvisions2, contact, phone, addr);

                }
            });
            ComponentName comp = new ComponentName("com.dynamixsoftware.printershare", "com.dynamixsoftware.printershare.ActivityWeb");
            Intent intent = new Intent();
            intent.setComponent(comp);
            intent.setAction("android.intent.action.VIEW");
            intent.setType("text/html");
            intent.setData(Uri.parse("file:///" + Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp1.html"));
            mActivity.startActivity(intent);

        } else {
            if (mRxDialogLoading == null) {

                mRxDialogLoading = new RxDialogLoading(mActivity, false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.cancel();
                    }
                });
                mRxDialogLoading.setLoadingText("正在解压插件...");
            }
            mRxDialogLoading.show();
            Schedulers.io().createWorker().schedule(new Runnable() {
                @Override
                public void run() {
                    final File assetFileToCacheDir = FileUtil.getAssetFileToCacheDir("PrinterShare.apk");
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            mRxDialogLoading.cancel();
                            AppUtil.installAPK(BaseApplication.getApplictaion(), assetFileToCacheDir.getPath());
                        }
                    });
                }
            });

        }

    }

    private void writeHtml(String illegalFacts, String legalProvisions1, String legalProvisions2, String contact, String phone, String addr) {
        File destDir = new File(Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp1.html");
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE HTML>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n");
        sb.append("    <style>\n");
        sb.append("    </style>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("<h1 align=\"center\">水行政责令停止违法行为通知书</h1>\n");
        sb.append("<p align=\"right\">x水当罚字[&nbsp;]第&nbsp;&nbsp;号</p>\n");
        sb.append(mNaturalInfo);
        sb.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;据初步调查,你(单位)&nbsp; <u>&nbsp;" + illegalFacts + "&nbsp;</u>&nbsp;涉嫌违反了 &nbsp;<u>&nbsp;" + legalProvisions1 + "&nbsp;</u>&nbsp;\n");
        sb.append("    的规定,现根据:<u>&nbsp;" + legalProvisions2 + "&nbsp;</u>的规定,责令你(单位)立即停止违法行为,听后处理。</p>\n");
        sb.append("<p align=\"right\">" + mMYear + "年" + mMMonth + "月" + mMDay + "日&nbsp;&nbsp;</p>\n");
        sb.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系人:<u>&nbsp;" + contact + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系电话:&nbsp;<u>&nbsp;" + phone + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系地址:&nbsp;<u>&nbsp;" + addr + "&nbsp;</u></p>\n");
        sb.append("</body>\n");
        sb.append("</html>");
        FileUtil.writeFileFromString(destDir, sb.toString(), false);
    }
}
