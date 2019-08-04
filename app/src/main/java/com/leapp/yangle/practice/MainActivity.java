package com.leapp.yangle.practice;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.leapp.yangle.practice.model.ImageModel;
import com.leapp.yangle.practice.utils.Constant;

public class MainActivity extends AppCompatActivity implements Callback {

    private ImageView imageView;
    private ImageDownloader imageDownloader;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    break;
                case Constant.Error:
                    Toast.makeText(MainActivity.this, "Download Failure", Toast.LENGTH_SHORT).show();
                    break;
            }

            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image_photo);
        imageDownloader = new ImageDownloader();
    }

    public void download(View view) {
        ImageModel imageModel = new ImageModel();
        imageModel.setRequestPath(Constant.IMAGE_PATH);

        imageDownloader.download(this,imageModel);
    }

    @Override
    public void callback(int resultCode, ImageModel imageModel) {
        Message message = handler.obtainMessage(resultCode);
        message.obj = imageModel.getBitmap();
        handler.sendMessageDelayed(message, 500);
    }
}
