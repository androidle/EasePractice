package com.leapp.yangle.practice.presenter;

import com.leapp.yangle.practice.DownloadContract;
import com.leapp.yangle.practice.engine.DownloadEngine;
import com.leapp.yangle.practice.model.ImageModel;
import com.leapp.yangle.practice.MainActivity;
import org.jetbrains.annotations.NotNull;

public class DownloadPresenter implements DownloadContract.PV{

    private final MainActivity view;
    private final DownloadEngine engine;

    public DownloadPresenter(MainActivity view) {
        this.view = view;
        engine = new DownloadEngine(this);
    }

    @Override
    public void requestDownload(@NotNull ImageModel imageBean) {
        try {
            engine.requestDownload(imageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void responseDownloadResult(final boolean isSuccess, @NotNull final ImageModel imageBean) {
        view.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.responseDownloadResult(isSuccess,imageBean);
            }
        });
    }
}
