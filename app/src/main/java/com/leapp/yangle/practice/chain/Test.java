package com.leapp.yangle.practice.chain;

public class Test {

    public static void main(String[] args) {
        Task1 task1 = new Task1(false);
        Task2 task2 = new Task2(false);
        Task3 task3 = new Task3(false);
        Task4 task4 = new Task4(true);

        task1.addNextTask(task2);
        task2.addNextTask(task3);
        task3.addNextTask(task4);

        task1.action();
    }
}
