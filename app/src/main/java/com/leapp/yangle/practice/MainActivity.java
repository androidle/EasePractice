package com.leapp.yangle.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.leapp.yangle.arouter.annotations.ARouter;
import com.leapp.yangle.common.base.BaseActivity;
import com.leapp.yangle.module.order.Order_MainActivity;
import com.leapp.yangle.module.personal.Personal_MainActivity;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openPersonal(View view) {
        Intent intent = new Intent(this, Personal_MainActivity.class);
        intent.putExtra("name", "app");
        startActivity(intent);
    }

    public void openOrder(View view) {
        Intent intent = new Intent(this, Order_MainActivity.class);
        intent.putExtra("name", "app");
        startActivity(intent);
    }
}
