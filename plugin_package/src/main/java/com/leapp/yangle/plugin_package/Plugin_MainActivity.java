package com.leapp.yangle.plugin_package;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Plugin_MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Plugin_MainActivity", "onCreate:====> "  + R.layout.activity_plugin__main);
        setContentView(R.layout.activity_plugin__main);

        Toast.makeText(this, "====Plugin_MainActivity++++", Toast.LENGTH_SHORT).show();
    }

}
