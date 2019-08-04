package com.leapp.yangle.practice.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.leapp.yangle.practice.DownloadContract;
import com.leapp.yangle.practice.model.ImageModel;
import com.leapp.yangle.practice.utils.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadEngine implements DownloadContract.M {

    private final DownloadContract.PV presenter;

    public DownloadEngine(DownloadContract.PV presenter) {
        this.presenter = presenter;
    }

    @Override
    public void requestDownload(ImageModel imageModel) {
        new Thread(new DownLoader(imageModel)).start();
    }


    final class DownLoader implements Runnable{

        private final ImageModel imageModel;
        public DownLoader(ImageModel imageModel) {
            this.imageModel = imageModel;
        }

        @Override
        public void run() {
           // request image
            try {
                URL url = new URL(imageModel.getRequestPath());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    showUI(Constant.SUCCESS, bitmap);
                } else {
                    showUI(Constant.Error, null);
                } 

            } catch (IOException e) {
                e.printStackTrace();
                showUI(Constant.Error, null);
            }

        }

        private void showUI(int resultCode, Bitmap bitmap) {
            imageModel.setBitmap(bitmap);
            presenter.responseDownloadResult(resultCode == Constant.SUCCESS,imageModel);
        }
    }
}
