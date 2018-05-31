package com.zhang.okinglawenforcementphone.mvp.contract;

import com.zhang.okinglawenforcementphone.beans.GreenEvidence;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8/008.
 */

public interface UploadEvidenceContract {
    interface Model {
        void uploadEvidence(Map<String, Object> fields,GreenEvidence evidence,List<GreenEvidenceMedia> picGreenMedias);
    }

    interface View {
       void uploadEvidenceSucc(String result);
       void uploadEvidenceFail(Throwable ex);
    }

    interface Presenter {
        void uploadEvidence(Map<String, Object> fields,GreenEvidence evidence,List<GreenEvidenceMedia> picGreenMedias);
        void uploadEvidenceSucc(String result);
        void uploadEvidenceFail(Throwable ex);
    }
}
