package com.leapp.yangle.module.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.leapp.yangle.common.Cons;
import com.leapp.yangle.common.RecordPathManager;
import com.leapp.yangle.common.base.BaseActivity;

public class Order_MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main);
    }

    public void openMain(View view) {
        // 1.类加载方式
//        try {
//            Class targetClass = Class.forName("com.leapp.yangle.practice.MainActivity");
//            startActivity(new Intent(this,targetClass));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        // 2.
        Class<?> targetClass = RecordPathManager.getTargetClass("app", "MainActivity");
        if (targetClass == null) {
            Log.e(Cons.TAG, "targetClass == null");
        }

        startActivity(new Intent(this,targetClass));
    }

    public void openPersonal(View view) {
//        try {
//            Class targetClass = Class.forName("com.leapp.yangle.module.personal.Personal_MainActivity");
//            startActivity(new Intent(this,targetClass));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        Class<?> targetClass = RecordPathManager.getTargetClass("personal", "Personal_MainActivity");
        if (targetClass == null) {
            Log.e(Cons.TAG, "targetClass == null");
        }

        startActivity(new Intent(this,targetClass));

    }
}
