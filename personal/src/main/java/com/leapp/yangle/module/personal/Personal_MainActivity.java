package com.leapp.yangle.module.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Personal_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main);
    }

    public void openMain(View view) {
        // 1.类加载方式
        Class targetClass = null;
        try {
            targetClass = Class.forName("com.leapp.yangle.practice.MainActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this,targetClass));
    }

    public void openOrder(View view) {
        Class targetClass = null;
        try {
            targetClass = Class.forName("com.leapp.yangle.module.order.Order_MainActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this,targetClass));
    }

}
