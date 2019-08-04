package com.leapp.yangle.practice;

import com.leapp.yangle.practice.model.ImageModel;

public interface DownloadContract {

    interface M {
        void requestDownload(ImageModel imageModel) throws Exception;
    }


    interface PV{

        void requestDownload(ImageModel imageModel);

        void responseDownloadResult(boolean isSuccess, ImageModel imageModel);
    }

}
