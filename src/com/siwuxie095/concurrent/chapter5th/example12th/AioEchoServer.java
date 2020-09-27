package com.siwuxie095.concurrent.chapter5th.example12th;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Jiajing Li
 * @date 2020-09-27 21:35:40
 */
@SuppressWarnings("all")
class AioEchoServer {

    public static final int PORT = 8000;

    private AsynchronousServerSocketChannel server;

    public AioEchoServer() throws IOException {
        this.server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(PORT));
    }

    public void start() {
        System.out.println("Server listen on " + PORT);
        // 注册事件和事件完成后的处理器
        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

            final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                System.out.println(Thread.currentThread().getName());
                Future<Integer> writeResult = null;
                try {
                    byteBuffer.clear();
                    result.read(byteBuffer).get(100, TimeUnit.SECONDS);
                    byteBuffer.flip();
                    writeResult = result.write(byteBuffer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        server.accept(null, this);
                        writeResult.get();
                        result.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("failed: " + exc);
            }
        });
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        new AioEchoServer().start();
        // 主线程可以继续自己的行为
        while (true) {
            Thread.sleep(1000);
        }
    }
}
