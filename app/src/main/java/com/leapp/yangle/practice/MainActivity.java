package com.leapp.yangle.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.leapp.yangle.arouter.annotations.ARouter;
import com.leapp.yangle.arouter.annotations.model.RouterBean;
import com.leapp.yangle.arouter.api.core.ARouterLoadGroup;
import com.leapp.yangle.arouter.api.core.ARouterLoadPath;
import com.leapp.yangle.common.base.BaseActivity;
import com.leapp.yangle.practice.test.ARouter$$Group$$Order;
import com.leapp.yangle.practice.test.ARouter$$Group$$Personal;
import java.util.Map;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openPersonal(View view) {
        ARouterLoadGroup loadGroup = new ARouter$$Group$$Personal();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        Class<? extends ARouterLoadPath> clazz = groupMap.get("personal");

        try {
            ARouterLoadPath path = clazz.newInstance();
            Map<String, RouterBean> pathMap = path.loadPath();
            RouterBean routerBean = pathMap.get("/personal/Personal_MainActivity");
            if (routerBean != null) {
                Intent intent = new Intent(this, routerBean.getClazz());
                intent.putExtra("name", "personal");
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openOrder(View view) {
        ARouterLoadGroup loadGroup = new ARouter$$Group$$Order();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        Class<? extends ARouterLoadPath> clazz = groupMap.get("order");

        try {
            ARouterLoadPath path = clazz.newInstance();
            Map<String, RouterBean> pathMap = path.loadPath();
            RouterBean routerBean = pathMap.get("/order/Order_MainActivity");
            if (routerBean != null) {
                Intent intent = new Intent(this, routerBean.getClazz());
                intent.putExtra("name", "order");
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
