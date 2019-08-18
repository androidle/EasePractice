package com.leapp.yangle.module.personal.debug;

import android.os.Bundle;
import android.util.Log;
import com.leapp.yangle.common.Cons;
import com.leapp.yangle.common.base.BaseActivity;
import com.leapp.yangle.module.personal.R;

public class Personal_DebugActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_debug_activity);
        Log.e(Cons.TAG, "onCreate: common/Personal_DebugActivity");
    }
}
