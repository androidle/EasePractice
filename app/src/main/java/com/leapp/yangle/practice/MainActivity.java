package com.leapp.yangle.practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.leapp.yangle.common.Cons;
import com.leapp.yangle.common.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(Cons.TAG, "onCreate: common/MainActivity");
    }
}
