package com.leapp.yangle.practice.chain;

public class Task1 extends BaseTask {


    public Task1(boolean isTask) {
        super(isTask);
    }

    @Override
    public void doAction() {
        System.out.println("Task1 执行了....");
    }
}
