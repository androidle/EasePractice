package com.leapp.yangle.common.base;

import android.app.Application;
import android.util.Log;
import com.leapp.yangle.common.Cons;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(Cons.TAG, "onCreate: common/BaseApplication" );
    }

}
