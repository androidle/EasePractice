package com.leapp.yangle.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import com.leapp.yangle.common.Cons;
import com.leapp.yangle.common.base.BaseActivity;
import com.leapp.yangle.module.order.Order_MainActivity;
import com.leapp.yangle.module.personal.Personal_MainActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(Cons.TAG, "onCreate: common/MainActivity");
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
