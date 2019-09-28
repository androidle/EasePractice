package com.leapp.yangle.module;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.leapp.yangle.arouter.annotations.ARouter;
import com.leapp.yangle.arouter.annotations.Parameter;
import com.leapp.yangle.arouter.api.ParameterManager;
import com.leapp.yangle.arouter.api.RouterManager;
import com.leapp.yangle.common.base.BaseActivity;
import com.leapp.yangle.common.order.OrderDrawable;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends BaseActivity {

    @Parameter(name = "age")
    int age = 1;
    @Parameter
    String name;

    @Parameter(name = "/order/getDrawable")
    OrderDrawable drawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ParameterManager.getInstance().loadParameter(this);
        ImageView imageView = findViewById(R.id.iv_Test);
        imageView.setImageResource(drawable.getDrawable());
    }

    public void openPersonal(View view) {
        RouterManager.getInstance()
                .build("/personal/Personal_MainActivity")
                .withString("name", "personal")
                .withInt("age", 28)
                .navigation(this,162);
    }

    public void openOrder(View view) {
        RouterManager.getInstance()
                .build("/order/Order_MainActivity")
                .withString("name", "order")
                .withBoolean("isRelease", true)
                .navigation(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.e("====>>>", "onActivityResult: " + data.getStringExtra("callBack"));
        }
    }
}
