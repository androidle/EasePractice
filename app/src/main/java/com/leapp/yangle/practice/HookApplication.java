package com.leapp.yangle.practice;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;


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

        try {
            pluginToAppAction();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hook", "pluginToAppAction 失败");
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
//                            if (activityClass.contains(actionIntent.getComponent().getClassName())) {
//                                intentField.set(obj, actionIntent);
//                            } else {//没有权限
//                                intentField.set(obj, new Intent(HookApplication.this,PermissionActivity.class));
//                            }
                            intentField.set(obj, actionIntent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:

            }

            mH.handleMessage(msg);
            return true;
        }
    }

    /**
     * 把插件的dexElements和宿主的dexElements融为一体
     * @throws Exception
     */
    public void pluginToAppAction() throws Exception{
        // 1.find app dexElements 代表PathClassLoader
        PathClassLoader pathClassLoader = (PathClassLoader) this.getClassLoader();

        Class<?> mBaseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = mBaseDexClassLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object mDexPathList = pathListField.get(pathClassLoader);
        Field dexElementsField = mDexPathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        // 本质是Element[] dexElements
        Object appDexElements = dexElementsField.get(mDexPathList);

        // 2.find plugin dexElements 代表插件DexClassLoader
        File pluginFile = getPluginFile();
        File fileDir = this.getDir("pluginDir", Context.MODE_PRIVATE);
        DexClassLoader dexClassLoader = new DexClassLoader(pluginFile.getAbsolutePath(),
                fileDir.getAbsolutePath(), null, getClassLoader());

        Class<?> mBaseDexClassLoaderClassPlugin = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListFieldPlugin = mBaseDexClassLoaderClassPlugin.getDeclaredField("pathList");
        pathListFieldPlugin.setAccessible(true);
        Object mDexPathListPlugin = pathListFieldPlugin.get(dexClassLoader);
        Field dexElementsFieldPlugin = mDexPathListPlugin.getClass().getDeclaredField("dexElements");
        dexElementsFieldPlugin.setAccessible(true);
        // 本质是Element[] dexElements
        Object pluginDexElements = dexElementsFieldPlugin.get(mDexPathListPlugin);

        // 3.创建新的 dexElements对象
        int appDexLength = Array.getLength(appDexElements);
        int pluginDexLength = Array.getLength(pluginDexElements);
        int sumLength = appDexLength + pluginDexLength;
        Object newDexElements = Array.newInstance(appDexElements.getClass().getComponentType(), sumLength);
        // 4.合并app 与 plugin dexElements 给新胡dexElements对象
        for (int i = 0; i < sumLength; i++) {
            if (i < appDexLength) {
                Array.set(newDexElements, i, Array.get(appDexElements, i));
            } else {
                Array.set(newDexElements,i,Array.get(pluginDexElements,i - appDexLength));
            }
        }

        // 5.将新的dexElements对象设置到app中去
        dexElementsField.set(mDexPathList,newDexElements);


        // 二 处理加载plugin中layout
        try {
            doPluginLayoutLoad();
        } catch (Exception e) {
            Log.e("hook", "doPluginLayoutLoad 失败" + e.toString());
        }
    }

    @NotNull
    private File getPluginFile() {
        return new File(Environment.getExternalStorageDirectory() + File.separator + "plugin_package-debug.apk");
    }

    private Resources mResources;
    private AssetManager mAssetManager;

    private void doPluginLayoutLoad() throws Exception {
        mAssetManager = AssetManager.class.newInstance();
        // 执行此方法 才能把让插件中的路径添加进来 public int addAssetPath(String path) {
        Method addAssetPathMethod = mAssetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
        addAssetPathMethod.setAccessible(true);
        addAssetPathMethod.invoke(mAssetManager, getPluginFile().getAbsolutePath());

        Resources appResources = getResources();//宿主的配置信息

        // 实例化 final StringBlock[] ensureStringBlocks() 执行后,String.xml color.xml anim.xml才会被初始化
        Method ensureStringBlocksMethod = mAssetManager.getClass().getDeclaredMethod("ensureStringBlocks");
        ensureStringBlocksMethod.setAccessible(true);
        ensureStringBlocksMethod.invoke(mAssetManager);

        mResources = new Resources(mAssetManager, appResources.getDisplayMetrics(), appResources.getConfiguration());
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }
}
