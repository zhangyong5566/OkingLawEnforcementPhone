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
import android.widget.CheckBox;
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

public class ExpandablePenaltyTheSpotAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> implements CompoundButton.OnCheckedChangeListener {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;
    public static final int TYPE_LEVEL_3 = 3;
    private TextInputEditText mTet_parties_concerned_natural;
    private RadioButton mRb_natural_person;
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
    private TextInputEditText mTet_punishment_place_other;
    private Activity mActivity;
    private TextView mBt_idcard;
    private int mMYear;
    private int mMMonth;
    private int mMDay;
    private EditText mEt_illegal_facts;
    private EditText mEt_legal_provisions;
    private EditText mEt_legal_provisions2;
    private CheckBox mCb_warning;
    private CheckBox mCb_fine;
    private EditText mTv_amount;
    private CheckBox mCb_spot;
    private CheckBox mCb_within_time;
    private EditText mEt_year;
    private EditText mEt_month;
    private EditText mEt_day;
    private EditText mEt_banknum;
    private TextInputEditText mTet_enforcement1;
    private TextInputEditText mTet_enforcement2;
    private TextInputEditText mTet_addr;
    private TextInputEditText mTet_sign_people;
    private TextInputEditText mTet_sign_time;
    private Button mBt_prin;
    private String mNaturalInfo;
    private String fineStr;
    private String paymentFormdStr;
    private RxDialogLoading mRxDialogLoading;

    public ExpandablePenaltyTheSpotAdapter(Activity activity, List<MultiItemEntity> data) {
        super(data);
        this.mActivity = activity;
        addItemType(TYPE_LEVEL_0, R.layout.activity_mission_recor_level0);
        addItemType(TYPE_LEVEL_1, R.layout.penalty_spot_info1);
        addItemType(TYPE_LEVEL_2, R.layout.penalty_spot_info2);
        addItemType(TYPE_LEVEL_3, R.layout.penalty_spot_info3);
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
                    mRb_natural_person.setOnCheckedChangeListener(this);
                }

                if (mTet_parties_concerned_natural == null) {
                    mTet_parties_concerned_natural = helper.getView(R.id.tet_parties_concerned_natural);
                    TextUtil.setEditTextInhibitInputSpace(mTet_parties_concerned_natural);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_parties_concerned_natural);
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
                    TextUtil.setEditTextInhibitInputSpace(mTet_parties_concerned_other);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_parties_concerned_other);
                }

                if (mTet_position_other == null) {

                    mTet_position_other = helper.getView(R.id.tet_position_other);
                    TextUtil.setEditTextInhibitInputSpace(mTet_position_other);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_position_other);
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

                if (mTet_punishment_place_other == null) {
                    mTet_punishment_place_other = helper.getView(R.id.tet_punishment_place_other);
                    TextUtil.setEditTextInhibitInputSpace(mTet_punishment_place_other);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_punishment_place_other);
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

                if (mEt_legal_provisions == null) {
                    mEt_legal_provisions = helper.getView(R.id.et_legal_provisions);
                }
                if (mEt_legal_provisions2 == null) {
                    mEt_legal_provisions2 = helper.getView(R.id.et_legal_provisions2);
                }
                if (mTv_amount == null) {
                    mTv_amount = helper.getView(R.id.et_amount);
                }
                if (mCb_warning == null) {
                    mCb_warning = helper.getView(R.id.cb_warning);
                    mCb_warning.setOnCheckedChangeListener(this);
                }
                if (mCb_fine == null) {
                    mCb_fine = helper.getView(R.id.cb_fine);
                    mCb_fine.setOnCheckedChangeListener(this);
                }

                if (mCb_spot == null) {
                    mCb_spot = helper.getView(R.id.cb_spot);
                    mCb_spot.setOnCheckedChangeListener(this);
                }
                if (mCb_within_time == null) {
                    mCb_within_time = helper.getView(R.id.cb_within_time);
                    mCb_within_time.setOnCheckedChangeListener(this);
                }
                if (mEt_year == null) {
                    mEt_year = helper.getView(R.id.et_year);
                }
                if (mEt_month == null) {
                    mEt_month = helper.getView(R.id.et_month);
                }
                if (mEt_day == null) {
                    mEt_day = helper.getView(R.id.et_day);
                }
                if (mEt_banknum == null) {
                    mEt_banknum = helper.getView(R.id.et_banknum);
                }

                break;
            case TYPE_LEVEL_3:
                if (mTet_enforcement1 == null) {
                    mTet_enforcement1 = helper.getView(R.id.tet_enforcement1);
                    TextUtil.setEditTextInhibitInputSpace(mTet_enforcement1);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_enforcement1);
                }
                if (mTet_enforcement2 == null) {
                    mTet_enforcement2 = helper.getView(R.id.tet_enforcement2);
                    TextUtil.setEditTextInhibitInputSpace(mTet_enforcement2);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_enforcement2);
                }
                if (mTet_addr == null) {
                    mTet_addr = helper.getView(R.id.tet_addr);
                    TextUtil.setEditTextInhibitInputSpace(mTet_addr);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_addr);
                }
                if (mTet_sign_people == null) {
                    mTet_sign_people = helper.getView(R.id.tet_sign_people);
                    TextUtil.setEditTextInhibitInputSpace(mTet_sign_people);
                    TextUtil.setEditTextInhibitInputSpeChat(mTet_sign_people);
                }
                if (mTet_sign_time == null) {
                    mTet_sign_time = helper.getView(R.id.tet_sign_time);
                }

                if (mBt_prin == null) {

                    mBt_prin = helper.getView(R.id.bt_prin);
                    helper.addOnClickListener(R.id.bt_prin);
                }


                break;
            default:
                break;
        }
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
                String punishmentPlaceOther = mTet_punishment_place_other.getText().toString().trim();

                if (TextUtils.isEmpty(partiesConcernedOther) && TextUtils.isEmpty(positionOther)
                        && TextUtils.isEmpty(phoneOther) && TextUtils.isEmpty(representativeOther)
                        && TextUtils.isEmpty(creditCodeOher) && TextUtils.isEmpty(addressOther) && TextUtils.isEmpty(punishmentPlaceOther)) {
                    RxToast.warning(BaseApplication.getApplictaion(), "填入信息不能有空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mNaturalInfo = "<p>当事人:<u>&nbsp;" + partiesConcernedOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;职务: <u>&nbsp;" + positionOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;电话:\n" +
                        "    <u>&nbsp;" + phoneOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;法定代表人:\n" +
                        "    <u>&nbsp;" + representativeOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;统一社会信用代码:\n" +
                        "    <u>&nbsp;" + creditCodeOher + "&nbsp;</u>\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;住所:<u>&nbsp;" + addressOther + "&nbsp;</u>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;处罚地点:<u>&nbsp;" + punishmentPlaceOther + "&nbsp;</u>" +
                        "</p>";

            }
            if (mEt_illegal_facts == null || TextUtils.isEmpty(mEt_illegal_facts.getText().toString().trim())) {
                RxToast.warning("违法事实不能为空");
                return;
            }
            if (mEt_legal_provisions == null || TextUtils.isEmpty(mEt_legal_provisions.getText().toString().trim())) {
                RxToast.warning("法律条款不能为空");
                return;
            }
            if (mEt_legal_provisions2 == null || TextUtils.isEmpty(mEt_legal_provisions2.getText().toString().trim())) {
                RxToast.warning("法律条款不能为空");
                return;
            }
            if (mTet_enforcement1 == null || TextUtils.isEmpty(mTet_enforcement1.getText().toString().trim())) {
                RxToast.warning("执法人员（姓名及执法证号）不能为空");
                return;
            }
            if (mTet_enforcement2 == null || TextUtils.isEmpty(mTet_enforcement2.getText().toString().trim())) {
                RxToast.warning("执法人员（姓名及执法证号）不能为空");
                return;
            }
            if (mTet_addr == null || TextUtils.isEmpty(mTet_addr.getText().toString().trim())) {
                RxToast.warning("联系地址不能为空");
                return;
            }
            if (mTet_sign_people == null || TextUtils.isEmpty(mTet_sign_people.getText().toString().trim())) {
                RxToast.warning("签收人不能为空");
                return;
            }
            if (mTet_sign_time == null || TextUtils.isEmpty(mTet_sign_time.getText().toString().trim())) {
                RxToast.warning("签收时间不能为空");
                return;
            }

            final String etillegalFacts = mEt_illegal_facts.getText().toString().trim();
            final String etLegalProvisions1 = mEt_legal_provisions.getText().toString().trim();
            final String etLegalProvisions2 = mEt_legal_provisions2.getText().toString().trim();
            String amount = mTv_amount.getText().toString().trim();
            String banknum = mEt_banknum.getText().toString().trim();
            final String enforcement1 = mTet_enforcement1.getText().toString().trim();
            final String enforcement2 = mTet_enforcement2.getText().toString().trim();
            final String addr = mTet_addr.getText().toString().trim();
            final String signPeople = mTet_sign_people.getText().toString().trim();
            final String signTime = mTet_sign_time.getText().toString().trim();
            String year = mEt_year.getText().toString().trim();
            String month = mEt_month.getText().toString().trim();
            String day = mEt_day.getText().toString().trim();
            if (mCb_warning.isChecked()) {

                fineStr = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"checkbox\" value=\"1\"checked = true/>1.警告;<input type=\"checkbox\" value=\"2\"/>2.罚款&nbsp;<u>&nbsp;&nbsp;</u>&nbsp;元(金额大写)。</p>";
            }

            if (mCb_fine.isChecked()) {
                if (!TextUtils.isEmpty(amount)) {

                    fineStr = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"checkbox\" value=\"1\"/>1.警告;<input type=\"checkbox\" value=\"2\"checked = true/>2.罚款&nbsp;<u>&nbsp;" + amount + "&nbsp;</u>&nbsp;元(金额大写)。</p>";
                } else {

                    RxToast.warning(BaseApplication.getApplictaion(), "罚款金额不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            if (mCb_spot.isChecked()) {
                paymentFormdStr = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;收款形式:<input type=\"checkbox\" value=\"1\"checked = true/>(1)当场收缴;&nbsp;<input type=\"checkbox\" value=\"1\"/>(2)限于<u>&nbsp;&nbsp;&nbsp;</u>年<u>&nbsp;&nbsp;&nbsp;</u>月<u>&nbsp;&nbsp;&nbsp;</u>日前将罚款交至(银行名称及账户、帐号)&nbsp;<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>&nbsp;逾期不缴的，每日按罚款数额的3%加处罚款。如不服本决定，可在收到本决定书之日起60日内向（同级人民政府或上一级水行政主管部门）申请行政复议；也可以在收到本决定之日起六个月内直接向人民法院起诉。复议和起诉期间本决定不停止执行；逾期不申请复议也不起诉，又不履行本决定的，本机关将依法申请人民法院强制执行。</p>";
            }

            if (mCb_within_time.isChecked()) {
                if (!TextUtils.isEmpty(year) && !TextUtils.isEmpty(month) && !TextUtils.isEmpty(day)) {

                    paymentFormdStr = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;收款形式:<input type=\"checkbox\" value=\"1\"/>(1)当场收缴;&nbsp;<input type=\"checkbox\" value=\"1\"checked = true/>(2)限于<u>&nbsp;" + year + "&nbsp;</u>年<u>&nbsp;" + month + "&nbsp;</u>月<u>&nbsp;" + day + "&nbsp;</u>日前将罚款交至(银行名称及账户、帐号)&nbsp;<u>&nbsp;" + banknum + "&nbsp;</u>&nbsp;逾期不缴的，每日按罚款数额的3%加处罚款。如不服本决定，可在收到本决定书之日起60日内向（同级人民政府或上一级水行政主管部门）申请行政复议；也可以在收到本决定之日起六个月内直接向人民法院起诉。复议和起诉期间本决定不停止执行；逾期不申请复议也不起诉，又不履行本决定的，本机关将依法申请人民法院强制执行。</p>";
                } else {
                    RxToast.warning(BaseApplication.getApplictaion(), "年月日不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            //写文件html
            Schedulers.io().createWorker().schedule(new Runnable() {
                @Override
                public void run() {
                    writeHtml(etillegalFacts, etLegalProvisions1, etLegalProvisions2, enforcement1, enforcement2, addr, signPeople, signTime);
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            ComponentName comp = new ComponentName("com.dynamixsoftware.printershare", "com.dynamixsoftware.printershare.ActivityWeb");
                            Intent intent = new Intent();
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                            intent.setType("text/html");
                            intent.setData(Uri.parse("file:///" + Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp2.html"));
                            mActivity.startActivity(intent);
                        }
                    });
                }
            });


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

    public void setOCRData(IDCardResult result) {
        mTet_sex_natural.setText(result.getGender().toString());
        mTet_parties_concerned_natural.setText(result.getName().toString());
        mTet_card_natural.setText(result.getIdNumber().toString());
        mTet_address_natural.setText(result.getAddress().toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rb_natural_person:
                if (b) {
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
                    mTet_punishment_place_other.setEnabled(false);
                    mTet_punishment_place_other.setText("");
                    mBt_idcard.setEnabled(true);
                } else {
                    mTet_parties_concerned_natural.setEnabled(false);
                    mTet_parties_concerned_natural.setText("");
                    mTet_parties_concerned_other.setEnabled(true);
                    mTet_sex_natural.setEnabled(false);
                    mTet_sex_natural.setText("");
                    mTet_card_natural.setEnabled(false);
                    mTet_card_natural.setText("");
                    mTet_phone_natural.setEnabled(false);
                    mTet_phone_natural.setText("");
                    mTet_phone_other.setEnabled(true);
                    mTet_address_natural.setEnabled(false);
                    mTet_address_natural.setText("");
                    mTet_address_other.setEnabled(true);
                    mTet_position_other.setEnabled(true);
                    mTet_representative_other.setEnabled(true);
                    mTet_credit_code_oher.setEnabled(true);
                    mTet_punishment_place_other.setEnabled(true);
                    mBt_idcard.setEnabled(false);

                }
                break;
            case R.id.cb_warning:
                if (b) {
                    mTv_amount.setText("");
                    mTv_amount.setEnabled(false);
                    mCb_fine.setChecked(false);
                }
                break;
            case R.id.cb_spot:
                if (b) {
                    mEt_year.setText("");
                    mEt_year.setEnabled(false);
                    mEt_month.setText("");
                    mEt_month.setEnabled(false);
                    mEt_day.setText("");
                    mEt_day.setEnabled(false);
                    mEt_banknum.setText("");
                    mEt_banknum.setEnabled(false);
                    mCb_within_time.setChecked(false);
                }
                break;
            case R.id.cb_within_time:
                if (b) {
                    mEt_year.setEnabled(true);
                    mEt_month.setEnabled(true);
                    mEt_day.setEnabled(true);
                    mEt_banknum.setEnabled(true);
                    mCb_spot.setChecked(false);
                }
                break;
            case R.id.cb_fine:
                if (b) {
                    mTv_amount.setEnabled(true);
                    mCb_warning.setChecked(false);
                }
                break;
            default:
                break;
        }

    }


    private void writeHtml(String etillegalFacts, String etLegalProvisions1, String etLegalProvisions2, String enforcement1, String enforcement2, String addr, String signPeople, String signTime) {
        File destDir = new File(Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp2.html");
        String contetnt = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "\n" +
                "\n" +
                "    <style>\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1 align=\"center\">水行政当场处罚决定书</h1>\n" +
                "<p align=\"right\">x水当罚字[&nbsp;]第&nbsp;&nbsp;号</p>\n" +
                mNaturalInfo +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当事人&nbsp; <u>&nbsp;" + etillegalFacts + "&nbsp;</u>&nbsp;违反了 &nbsp;<u>&nbsp;" + etLegalProvisions1 + "&nbsp;</u>的规定，现根据《行政处罚法》第三十三条和&nbsp;<u>&nbsp;" + etLegalProvisions2 +
                "&nbsp;</u>的规定,决定给予如下处罚:</p>\n" + fineStr + paymentFormdStr +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;执法人员（姓名及执法证号）:&nbsp;<u>&nbsp;" + enforcement1 + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;执法人员（姓名及执法证号）:&nbsp;<u>&nbsp;" + enforcement2 + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系地址:&nbsp;\n" +
                "    <u>&nbsp;" + addr + "&nbsp;</u>\n" +
                "</p>\n" +
                "<p align=\"right\">" + mMYear + "年" + mMMonth + "月" + mMDay + "日&nbsp;&nbsp;</p>\n" +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;签收人:<u>&nbsp;" + signPeople + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;签收时间:&nbsp;<u>&nbsp;" + signTime + "&nbsp;</u></p>\n" +
                "</body>\n" +
                "</html>";
        FileUtil.writeFileFromString(destDir, contetnt, false);
    }
}
