package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.beans.GreenEvidence;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadEvidenceContract;
import com.zhang.okinglawenforcementphone.mvp.model.UploadEvidenceModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8/008.
 */

public class UploadEvidencePresenter implements UploadEvidenceContract.Presenter {
    private UploadEvidenceContract.Model mModel;
    private UploadEvidenceContract.View mView;

    public UploadEvidencePresenter(UploadEvidenceContract.View view) {
        mView = view;
        mModel = new UploadEvidenceModel(this);
    }

    @Override
    public void uploadEvidence(Map<String, Object> fields, GreenEvidence evidence, List<GreenEvidenceMedia> picGreenMedias) {
        mModel.uploadEvidence(fields,evidence,picGreenMedias);
    }

    @Override
    public void uploadEvidenceSucc(String result) {
        mView.uploadEvidenceSucc(result);
    }

    @Override
    public void uploadEvidenceFail(Throwable ex) {
        mView.uploadEvidenceFail(ex);
    }
}
