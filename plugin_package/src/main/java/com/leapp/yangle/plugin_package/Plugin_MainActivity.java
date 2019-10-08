package com.leapp.yangle.plugin_package;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Plugin_MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.plugin_main_activity);

        Toast.makeText(this, "This is Plugin", Toast.LENGTH_SHORT).show();
    }
}
