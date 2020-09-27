package com.siwuxie095.concurrent.chapter5th.example12th;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author Jiajing Li
 * @date 2020-09-27 21:35:53
 */
@SuppressWarnings("all")
class AioClient {

    public static void main(String[] args) throws Exception {
        // 语句一
        final AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        // 语句二
        client.connect(new InetSocketAddress("localhost", 8000), null, new CompletionHandler<Void, Object>() {
            // 连接成功
            @Override
            public void completed(Void result, Object attachment) {
                // 进行数据写入，向服务器发送数据
                client.write(ByteBuffer.wrap("Hello!".getBytes()), null, new CompletionHandler<Integer, Object>() {
                    @Override
                    public void completed(Integer result, Object attachment) {
                        try {
                            // 准备进行数据读取，从服务器读取回写的数据
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            client.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                                @Override
                                public void completed(Integer result, ByteBuffer attachment) {
                                    // 打印接收到的数据
                                    attachment.flip();
                                    System.out.println(new String(attachment.array()));
                                    try {
                                        client.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {

                    }
                });
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });
        // 语句三：由于主线程马上结束，这里等待上述处理全部完成
        Thread.sleep(1000);
    }

}
