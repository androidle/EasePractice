package com.leapp.yangle.practice;

import com.leapp.yangle.common.RecordPathManager;
import com.leapp.yangle.common.base.BaseApplication;
import com.leapp.yangle.module.order.Order_MainActivity;
import com.leapp.yangle.module.personal.Personal_MainActivity;

public class AppApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        RecordPathManager.joinGroup("app","MainActivity",MainActivity.class);
        RecordPathManager.joinGroup("order","Order_MainActivity", Order_MainActivity.class);
        RecordPathManager.joinGroup("personal","Personal_MainActivity", Personal_MainActivity.class);
    }
}
