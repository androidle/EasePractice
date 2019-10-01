package com.leapp.yangle.practice.connectionpool;

import java.io.IOException;
import java.net.Socket;

class HttpConnection {

    Socket socket;

    long hasUseTime;//连接对象的最后使用时间

    public HttpConnection(final String host, final int post) {
        // 网络请求需在子线程 todo

        try {
            socket = new Socket(host, post);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSameConnectionAction(String host, int port) {
        if (socket == null) {
            return false;
        }

        boolean b = host.equals(socket.getInetAddress().getHostName()) && port == (socket.getPort());
        return b;
    }


    public void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("closeSocket" + e.getMessage());
            }
        }
    }
}
