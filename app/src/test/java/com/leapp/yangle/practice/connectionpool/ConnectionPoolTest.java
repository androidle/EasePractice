package com.leapp.yangle.practice.connectionpool;

import org.junit.Test;

public class ConnectionPoolTest {

    @Test
    public void test() {

        UseConnectionPool useConnectionPool = new UseConnectionPool();
        useConnectionPool.useConnectionPool("www.baidu.com",443);
        useConnectionPool.useConnectionPool("www.baidu.com",443);
        useConnectionPool.useConnectionPool("www.baidu.com",443);
        useConnectionPool.useConnectionPool("www.baidu.com",443);
        useConnectionPool.useConnectionPool("www.baidu.com",443);
        useConnectionPool.useConnectionPool("www.baidu.com",443);
        useConnectionPool.useConnectionPool("www.baidu.com",443);
        useConnectionPool.useConnectionPool("www.baidu.com",443);

        useConnectionPool.useConnectionPool("www.baidu.com",443);
    }

}