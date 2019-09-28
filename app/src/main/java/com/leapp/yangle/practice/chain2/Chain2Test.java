package com.leapp.yangle.practice.chain2;

import java.util.ArrayList;
import java.util.List;

public class Chain2Test {

    public static void main(String[] args) {

        List<IBaseTask> tasks = new ArrayList<>();
        tasks.add(new Task1());
        tasks.add(new Task2());
        tasks.add(new Task3());

        ChainManager chainManager = new ChainManager(0, tasks);
        chainManager.doRunAction("OK",chainManager);
    }
}
