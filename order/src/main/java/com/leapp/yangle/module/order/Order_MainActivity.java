package com.leapp.yangle.module.order;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.leapp.yangle.arouter.annotations.ARouter;
import com.leapp.yangle.arouter.annotations.Parameter;
import com.leapp.yangle.arouter.api.ParameterManager;
import com.leapp.yangle.common.base.BaseActivity;

@ARouter(path = "/order/Order_MainActivity")
public class Order_MainActivity extends BaseActivity {

    @Parameter
    String name;

    @Parameter(name = "isRelease")
    boolean release;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main);

        ParameterManager.getInstance().loadParameter(this);

        if (getIntent() != null) {
            Log.e("ARouter", "name ==>>>" + name + " / release ===>>>" + release);
        }
    }

    public void openMain(View view) {

    }

    public void openPersonal(View view) {

    }
}
