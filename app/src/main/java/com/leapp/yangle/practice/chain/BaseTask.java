package com.leapp.yangle.practice.chain;

public abstract class BaseTask {

    private boolean isTask;

    private BaseTask next;

    public BaseTask(boolean isTask) {
        this.isTask = isTask;
    }

    public void addNextTask(BaseTask next) {
        this.next = next;
    }

    public abstract void doAction();

    public void action() {
        if (isTask) {
            doAction();
        } else {
            if (next != null) {
                next.action();
            }
        }
    }
}
