package com.siwuxie095.concurrent.chapter5th.example11th;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiajing Li
 * @date 2020-09-26 16:33:47
 */
@SuppressWarnings("all")
public class MultiThreadEchoServer {

    private static ExecutorService executor = Executors.newCachedThreadPool();

    static class HandleMsg implements Runnable {

        private Socket clientSocket;

        public HandleMsg(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            PrintWriter writer = null;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
                // 从 InputStream 中读取客户端发送的数据
                String inputLine = null;
                long begin = System.currentTimeMillis();
                while (Objects.nonNull(inputLine = reader.readLine())) {
                    writer.println(inputLine);
                }
                long end = System.currentTimeMillis();
                System.out.println("spend: " + (end - begin) + " ms");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (Objects.nonNull(reader)) {
                        reader.close();
                    }
                    if (Objects.nonNull(writer)) {
                        writer.close();
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getRemoteSocketAddress() + " connect!");
                executor.execute(new HandleMsg(clientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
