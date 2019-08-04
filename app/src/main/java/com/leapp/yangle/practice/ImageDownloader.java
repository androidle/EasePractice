package com.leapp.yangle.practice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.leapp.yangle.practice.model.ImageModel;
import com.leapp.yangle.practice.utils.Constant;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader {

    public void download(Callback callback, ImageModel imageModel) {
        new Thread(new Downloader(callback,imageModel)).start();
    }



    final class Downloader implements Runnable{
        private final Callback callback;
        private final ImageModel imageModel;
        public Downloader(Callback callback, ImageModel imageModel) {
            this.callback = callback;
            this.imageModel = imageModel;
        }

        @Override
        public void run() {
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
            } catch (Exception e) {
                e.printStackTrace();
                showUI(Constant.Error, null);
            }
        }

        private void showUI(int resultCode, Bitmap bitmap) {
            imageModel.setBitmap(bitmap);
            if (callback != null) {
                callback.callback(resultCode,imageModel);
            }
        }
    }
}
