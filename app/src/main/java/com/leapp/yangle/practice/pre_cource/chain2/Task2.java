package com.leapp.yangle.practice.pre_cource.chain2;

public class Task2 implements IBaseTask {
    
    @Override
    public void doRunAction(String isTask, IBaseTask iBaseTask) {
        if ("OK" .equals(isTask)) {
            System.out.println("Task2 执行了....");
            iBaseTask.doRunAction(isTask,iBaseTask);
        }
    }
}
