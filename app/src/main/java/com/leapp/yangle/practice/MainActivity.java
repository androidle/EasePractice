package com.leapp.yangle.practice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = findViewById(R.id.btn_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, ((Button)v).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        try {
            hook(button);//在不修改以上代码的情况下,通过hook把((Button)v).getText() 内容修改
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "hook 失败...");
        }
    }

    private void hook(final View view) throws Exception {
        // 1.监听onClick
        // 获取ListenerInfo对象 ,View 中的方法ListenerInfo getListenerInfo()
        Class<?> viewClass = Class.forName("android.view.View");
        Method getListenerInfoMethod = viewClass.getDeclaredMethod("getListenerInfo");
        getListenerInfoMethod.setAccessible(true);
        Object listenerInfo = getListenerInfoMethod.invoke(view);


        // ListenerInfo 中Field OnClickListener mOnClickListener;
        Class<?> listenerInfoClass = Class.forName("android.view.View$ListenerInfo");
        Field mOnClickListenerField = listenerInfoClass.getField("mOnClickListener");
        final Object mOnClickListener = mOnClickListenerField.get(listenerInfo);

        // 使用动态代理
        Object onClickListenerProxy = Proxy.newProxyInstance( // 1.定义代理的class
                MainActivity.class.getClassLoader(),// 1.
                new Class[]{View.OnClickListener.class}, //2.要监听的接口,对应返回的类型
                new InvocationHandler() { // 3.监听接口的方法回调
                    /**
                     *
                     * @param proxy
                     * @param method onClick
                     * @param args (View v)
                     * @return
                     * @throws Throwable
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 加入自己的逻辑
                        Log.e(TAG, "hook 拦截到OnClick中的方法了...");
                        Button button = new Button(MainActivity.this);
                        button.setText("hello Hook!!!");
                        // 让其继续执行下去
                        return method.invoke(mOnClickListener,button);
                    }
                });

        // 将mOnClickListener 替换成我们动态代理的 实现
        mOnClickListenerField.set(listenerInfo, onClickListenerProxy);

    }

}
