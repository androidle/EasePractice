package com.leapp.yangle.arouter.api;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BundlerManager {

    private Bundle bundle = new Bundle();

    // 是否是startWithResult
    private boolean isResult;

    public Bundle getBundle() {
        return bundle;
    }

    public boolean isResult() {
        return isResult;
    }

    public BundlerManager withString(@NonNull String key, @Nullable String value) {
        bundle.putString(key, value);
        return this;
    }

    public BundlerManager withResultString(@NonNull String key, @Nullable String value) {
        isResult = true;
        bundle.putString(key, value);
        return this;
    }

    public BundlerManager withBoolean(@NonNull String key, @Nullable boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public BundlerManager withResultBoolean(@NonNull String key, @Nullable boolean value) {
        isResult = true;
        bundle.putBoolean(key, value);
        return this;
    }

    public BundlerManager withInt(@NonNull String key, @Nullable int value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundlerManager withResultInt(@NonNull String key, @Nullable int value) {
        isResult = true;
        bundle.putInt(key, value);
        return this;
    }

    public Object navigation(Context context) {
       return navigation(context, -1);
    }

    public Object navigation(Context context, int code) {
        return RouterManager.getInstance().navigation(context, code);
    }
}
