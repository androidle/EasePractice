package com.leapp.yangle.module.personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.leapp.yangle.common.Cons;
import com.leapp.yangle.common.RecordPathManager;
import com.leapp.yangle.common.base.BaseActivity;

public class Personal_MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main);
    }

    public void openMain(View view) {
//        // 1.类加载方式
//        Class targetClass = null;
//        try {
//            targetClass = Class.forName("com.leapp.yangle.practice.MainActivity");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        startActivity(new Intent(this,targetClass));

        Class<?> targetClass = RecordPathManager.getTargetClass("app", "MainActivity");
        if (targetClass == null) {
            Log.e(Cons.TAG, "targetClass == null");
        }

        startActivity(new Intent(this,targetClass));
    }

    public void openOrder(View view) {
//        Class targetClass = null;
//        try {
//            targetClass = Class.forName("com.leapp.yangle.module.order.Order_MainActivity");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        startActivity(new Intent(this,targetClass));

        Class<?> targetClass = RecordPathManager.getTargetClass("order", "Order_MainActivity");
        if (targetClass == null) {
            Log.e(Cons.TAG, "targetClass == null");
        }

        startActivity(new Intent(this,targetClass));
    }

}
