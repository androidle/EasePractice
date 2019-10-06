package com.leapp.yangle.practice;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;


public class HookApplication extends Application {

    private static List<String> activityClass = new ArrayList<>();

    static {
        activityClass.add(TestActivity.class.getName());// 添加权限
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            hookAMSAction();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hook", "hookAMSAction 失败");
        }


        try {
            hookLaunchActivity();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hook", "hookLaunchActivity 失败");
        }

    }

    /**
     * 要在执行AMS之前,替换可用的Activity
     */
    private void hookAMSAction() throws Exception{

        Class<?> mIActivityManagerClass = Class.forName("android.app.IActivityManager");

        // 需要拿到IActivityManager对象,才能使动态代理的invoke正常执行
        // 扫行 ActivityManagerNative 中的static public IActivityManager getDefault()
        Class<?> mActivityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
        final Object mIActivityManager = mActivityManagerNativeClass.getMethod("getDefault").invoke(null);


        // 动态代理
        Object mIActivityManagerProxy = Proxy.newProxyInstance(
                HookApplication.class.getClassLoader(),
                new Class[]{mIActivityManagerClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("startActivity".equals(method.getName())) {//仅处理这个方法
                            // 替换AMS检查 proxyActivity,
                            Intent intent = new Intent(HookApplication.this, ProxyActivity.class);
                            // 把原始TestActivity intent保存 在AMS检查过后,再将TestActivity换回来,目标是跳转到TextActivity
                            intent.putExtra("actionIntent", (Intent) args[2]);
                            args[2] = intent;
                        }

                        Log.e("hook", "拦截到mIActivityManager里的方法 ==>"+method.getName() );
                        return method.invoke(mIActivityManager,args);
                    }
                }
        );

//        Class<?> mActivityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
        Field gDefaultFeild = mActivityManagerNativeClass.getDeclaredField("gDefault");
        gDefaultFeild.setAccessible(true);
        Object gDefault = gDefaultFeild.get(null);



        // 替换点
        Class mSingletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = mSingletonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);

        // 替换
        mInstanceField.set(gDefault,mIActivityManagerProxy);// 需要gDefault对象
    }


    private void hookLaunchActivity() throws Exception{

        Field mCallbackFeild = Handler.class.getDeclaredField("mCallback");
        mCallbackFeild.setAccessible(true);
        /**
         * 处理handle对象如何来
         * 1.H
         * 2.获取ActivityThread---> public static ActivityThread currentActivityThread()
         *
         *
         */

        Class<?> mActivityThreadClass = Class.forName("android.app.ActivityThread");
        Field mHField = mActivityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Object mActivityThread = mActivityThreadClass.getMethod("currentActivityThread").invoke(null);
        Handler mH = (Handler) mHField.get(mActivityThread);

        mCallbackFeild.set(mH,new MyCallBack(mH));// 替换 我们自己的逻辑
    }

    public static final int LAUNCH_ACTIVITY         = 100;

    public class MyCallBack implements Handler.Callback{

        private Handler mH;

        public MyCallBack(Handler h) {
            mH = h;
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LAUNCH_ACTIVITY:// 只处理Launch Activity
                    Object obj = msg.obj;

                    try {
                        Field intentField = obj.getClass().getDeclaredField("intent");
                        intentField.setAccessible(true);

                        // 获取 intent对象,取出保存携带过来的 TestActivity
                        Intent intent = (Intent) intentField.get(obj);
                        Intent actionIntent = intent.getParcelableExtra("actionIntent");
                        if (actionIntent != null) {
                            if (activityClass.contains(actionIntent.getComponent().getClassName())) {
                                intentField.set(obj, actionIntent);
                            } else {//没有权限
                                intentField.set(obj, new Intent(HookApplication.this,PermissionActivity.class));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

            mH.handleMessage(msg);
            return true;
        }
    }
}
