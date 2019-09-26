package com.leapp.yangle.module.personal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.leapp.yangle.arouter.annotations.ARouter;
import com.leapp.yangle.arouter.annotations.Parameter;
import com.leapp.yangle.arouter.api.ParameterManager;
import com.leapp.yangle.common.base.BaseActivity;

@ARouter(path = "/personal/Personal_MainActivity")
public class Personal_MainActivity extends BaseActivity {

    @Parameter
    String name;

    @Parameter
    int age = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main);

        ParameterManager.getInstance().loadParameter(this);

        if (getIntent() != null) {
            Log.e("ARouter", "name ==>>>" + name + " / age ===>>>" + age);
        }
    }

    public void openMain(View view) {
    }

    public void openOrder(View view) {
    }

}
