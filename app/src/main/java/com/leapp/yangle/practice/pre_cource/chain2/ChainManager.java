package com.leapp.yangle.practice.pre_cource.chain2;

import java.util.List;

public class ChainManager implements IBaseTask {

    private List<IBaseTask> taskList;

    private int index;

    public ChainManager(int index, List<IBaseTask> taskList) {
        this.taskList = taskList;
        this.index = index;
    }


    @Override
    public void doRunAction(String isTask, IBaseTask iBaseTask) {
        if (taskList.isEmpty()) {
            return;
        }

        if (index >= taskList.size()) {
            return;
        }

        IBaseTask task = taskList.get(index);
        index++;

        task.doRunAction(isTask, iBaseTask);
    }
}
