package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.AddMemberContract;
import com.zhang.okinglawenforcementphone.mvp.model.AddMemberModel;

/**
 * Created by Administrator on 2018/4/20.
 */

public class AddMemberPresenter implements AddMemberContract.Presenter {
    private AddMemberContract.Model mModel;
    private AddMemberContract.View mView;

    public AddMemberPresenter(AddMemberContract.View view) {
        mView = view;
        mModel = new AddMemberModel(this);
    }

    @Override
    public void addMember(String userid, String mtaskId, String userids) {
        mModel.addMember(userid, mtaskId, userids);
    }

    @Override
    public void addMemberSucc(String result) {
        mView.addMemberSucc(result);
    }

    @Override
    public void addMemberFail(Throwable ex) {
        mView.addMemberFail(ex);
    }
}
