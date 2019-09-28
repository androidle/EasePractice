package com.leapp.yangle.module.order.impl;

import com.leapp.yangle.arouter.annotations.ARouter;
import com.leapp.yangle.common.order.OrderDrawable;
import com.leapp.yangle.module.order.R;

@ARouter(path = "/order/getDrawable")
public class OrderDrawableImpl implements OrderDrawable {
    @Override
    public int getDrawable() {
        return R.drawable.ic_arrow_downward_black_24dp;
    }
}
