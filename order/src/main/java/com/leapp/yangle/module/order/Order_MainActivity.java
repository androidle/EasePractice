package com.leapp.yangle.module.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Order_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main);
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

    public void openPersonal(View view) {
        Class targetClass = null;
        try {
            targetClass = Class.forName("com.leapp.yangle.module.personal.Personal_MainActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this,targetClass));
    }
}
