package com.leapp.yangle.practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.leapp.yangle.practice.model.ImageModel;
import com.leapp.yangle.practice.presenter.DownloadPresenter;
import com.leapp.yangle.practice.utils.Constant;

public class MainActivity extends AppCompatActivity implements DownloadContract.PV {

    private ImageView imageView;
    private DownloadContract.PV presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_photo);
        presenter = new DownloadPresenter(this);
    }

    public void download(View view) {
        ImageModel imageModel = new ImageModel();
        imageModel.setRequestPath(Constant.IMAGE_PATH);
        requestDownload(imageModel);
    }

    @Override
    public void requestDownload(ImageModel imageModel) {
        if (presenter != null) {
            presenter.requestDownload(imageModel);
        }
    }

    @Override
    public void responseDownloadResult(boolean isSuccess, ImageModel imageModel) {
        Toast.makeText(this, isSuccess ? "Download Success" : "Download Failure", Toast.LENGTH_SHORT).show();
        if (isSuccess) {
            imageView.setImageBitmap(imageModel.getBitmap());
        }
    }
}
