package com.siwuxie095.concurrent.chapter5th.example11th;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

/**
 * @author Jiajing Li
 * @date 2020-09-26 16:49:30
 */
@SuppressWarnings("all")
class SocketClient {

    public static void main(String[] args) {
        Socket clientSocket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress("localhost", 8000));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("Hello!");
            // 退出标识
            writer.println();
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine = null;
            while (Objects.nonNull(inputLine = reader.readLine())) {
                System.out.println("from server: " + inputLine);
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
