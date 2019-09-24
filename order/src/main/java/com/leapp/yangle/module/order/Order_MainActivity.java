package com.leapp.yangle.module.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.leapp.yangle.arouter.annotations.ARouter;
import com.leapp.yangle.common.base.BaseActivity;

@ARouter(path = "/order/MainActivity")
public class Order_MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main);

        Intent intent = getIntent();
        if (intent != null) {
            Log.e("ARouter", intent.getStringExtra("name"));
        }
    }

    public void openMain(View view) {

    }

    public void openPersonal(View view) {

    }
}
