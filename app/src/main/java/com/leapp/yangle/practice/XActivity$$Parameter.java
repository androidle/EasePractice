package com.leapp.yangle.practice;

import com.leapp.yangle.arouter.api.core.ParameterLoad;

public class XActivity$$Parameter implements ParameterLoad {

    @Override
    public void loadParameter(Object target) {
        MainActivity t = (MainActivity) target;

        t.name = t.getIntent().getStringExtra("name");
        t.age = t.getIntent().getIntExtra("age",t.age);
    }
}
