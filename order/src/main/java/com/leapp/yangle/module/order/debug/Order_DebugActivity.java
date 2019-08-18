package com.leapp.yangle.module.order.debug;

import android.os.Bundle;
import android.util.Log;
import com.leapp.yangle.common.Cons;
import com.leapp.yangle.common.base.BaseActivity;
import com.leapp.yangle.module.order.R;

public class Order_DebugActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_debug);
        Log.e(Cons.TAG, "onCreate: common/Order_DebugActivity");
    }
}
