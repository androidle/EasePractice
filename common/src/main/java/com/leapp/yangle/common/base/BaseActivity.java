package com.leapp.yangle.common.base;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.leapp.yangle.common.Cons;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(Cons.TAG, "onCreate: " + this.getClass().getSimpleName() );
    }
}
