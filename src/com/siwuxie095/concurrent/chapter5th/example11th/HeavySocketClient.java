package com.siwuxie095.concurrent.chapter5th.example11th;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Jiajing Li
 * @date 2020-09-26 16:57:06
 */
@SuppressWarnings("all")
class HeavySocketClient {

    private static ExecutorService executor = Executors.newCachedThreadPool();

    private static final int SLEEP_TIME = 1000 * 1000 * 1000;

    static class EchoClient implements Runnable {

        @Override
        public void run() {
            Socket clientSocket = null;
            PrintWriter writer = null;
            BufferedReader reader = null;
            try {
                clientSocket = new Socket();
                clientSocket.connect(new InetSocketAddress("localhost", 8000));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println("H");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.println("e");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.println("l");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.println("l");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.println("o");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.println("!");
                LockSupport.parkNanos(SLEEP_TIME);
                // 退出标识
                writer.println();
                writer.flush();

                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine = null;
                while (Objects.nonNull(inputLine = reader.readLine())) {
                    System.out.println(Thread.currentThread().getId() + " from server: " + inputLine);
                    if (Objects.equals(inputLine, "")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (Objects.nonNull(writer)) {
                        writer.close();
                    }
                    if (Objects.nonNull(reader)) {
                        reader.close();
                    }
                    if (Objects.nonNull(clientSocket)) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        EchoClient echoClient = new EchoClient();
        for (int i = 0; i < 10; i++) {
            executor.execute(echoClient);
        }
        executor.shutdown();
    }

}
