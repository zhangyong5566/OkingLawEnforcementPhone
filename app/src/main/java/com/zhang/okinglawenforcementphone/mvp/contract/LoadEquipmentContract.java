package com.zhang.okinglawenforcementphone.mvp.contract;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public interface LoadEquipmentContract {
    interface Model {
        void loadEquipment();

    }

    interface View {
        void loadEquipmentSucc(String result);

        void loadEquipmentFail(Throwable ex);
    }

    interface Presenter {
        void loadEquipment();

        void loadEquipmentSucc(String result);

        void loadEquipmentFail(Throwable ex);
    }
}
