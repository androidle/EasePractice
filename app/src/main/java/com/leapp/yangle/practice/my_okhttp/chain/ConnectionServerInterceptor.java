package com.leapp.yangle.practice.my_okhttp.chain;

import com.leapp.yangle.practice.my_okhttp.Request2;
import com.leapp.yangle.practice.my_okhttp.Response2;
import com.leapp.yangle.practice.my_okhttp.SocketRequestServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ConnectionServerInterceptor implements Interceptor2 {

    @Override
    public Response2 intercept(Chain2 chain2) throws IOException {
        System.out.println("连接服务器拦截器, 执行了...");
        SocketRequestServer requestServer = new SocketRequestServer();
        Request2 request = chain2.request();
        Socket socket = new Socket(requestServer.getHost(request), requestServer.getPort(request));

        // todo 请求
        OutputStream os = socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
        String requestAll = requestServer.getRequestHeaderAll(request);
        System.out.println("==requestAll=>>\r\n"+requestAll);
        bufferedWriter.write(requestAll);//给服务器发送数据
        bufferedWriter.flush();//真正发出去

        // todo 响应

        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                String readLine = null;
//                while (true) {
//                    try {
//                        if ((readLine = bufferedReader.readLine()) != null) {
//                            System.out.println("==服务响应=>>" + readLine);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//            }
//        }.start();


        Response2 response2 = new Response2();
//        response2.setBody("流程走通");
        // todo 取出请求码 HTTP/1.1 200 OK
        String firstLine = bufferedReader.readLine();
        String[] strs = firstLine.split(" ");
        response2.setStatusCode(Integer.parseInt(strs[1]));

        // todo 取出响应体 只要是空行,下面就是响应体
        String readLine = null;
        try {
            while ((readLine = bufferedReader.readLine()) != null) {
                if ("" .equals(readLine)) {
                    response2.setBody(bufferedReader.readLine());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response2;
    }

}
