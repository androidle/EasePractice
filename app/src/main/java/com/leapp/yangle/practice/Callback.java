package com.leapp.yangle.practice;

import com.leapp.yangle.practice.model.ImageModel;

public interface Callback {


    /**
     *
     * @param resultCode
     * @param imageModel
     */
    void callback(int resultCode, ImageModel imageModel);
}
