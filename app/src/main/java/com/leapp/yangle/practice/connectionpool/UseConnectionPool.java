package com.leapp.yangle.practice.connectionpool;

public class UseConnectionPool {

    private ConnectionPool mConnectionPool = new ConnectionPool();

    public void useConnectionPool(String host,int port) {

        HttpConnection connection = mConnectionPool.getConnection(host, port);

        if (connection == null) {
            connection = new HttpConnection(host,port);
            System.out.println("连接池没有连接对象 需要实例化一个HttpConnecton ");
        } else {
            System.out.println("复用 HttpConnecton ");
        }

        connection.hasUseTime = System.currentTimeMillis();
        mConnectionPool.putConnection(connection);
    }
}
