package com.siwuxie095.concurrent.chapter5th.example11th;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * @author Jiajing Li
 * @date 2020-09-26 18:44:05
 */
@SuppressWarnings("all")
class NioClient {

    private Selector selector;

    public void init(String ip, int port) throws IOException {
        SocketChannel clientSocketChannel = SocketChannel.open();
        clientSocketChannel.configureBlocking(false);
        selector = SelectorProvider.provider().openSelector();
        clientSocketChannel.connect(new InetSocketAddress(ip, port));
        clientSocketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    public void working() throws IOException {
        while (true) {
            if (!selector.isOpen()) {
                break;
            }
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                // 连接事件发生
                if (selectionKey.isConnectable()) {
                    connect(selectionKey);
                    // 通道可读事件发生
                } else if (selectionKey.isReadable()) {
                    read(selectionKey);
                }
            }
        }
    }

    private void connect(SelectionKey selectionKey) throws IOException {
        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
        // 如果正在连接，则完成连接
        if (clientSocketChannel.isConnectionPending()) {
            clientSocketChannel.finishConnect();
        }
        clientSocketChannel.configureBlocking(false);
        clientSocketChannel.write(ByteBuffer.wrap(new String("hello server!\r\n").getBytes()));
        clientSocketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey selectionKey) throws IOException {
        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
        // 创建读取的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        clientSocketChannel.read(byteBuffer);
        byte[] data = byteBuffer.array();
        String msg = new String(data).trim();
        System.out.println("客户端收到信息：" + msg);
        clientSocketChannel.close();
        selectionKey.selector().close();
    }

    public static void main(String[] args) throws IOException {
        NioClient client = new NioClient();
        client.init("localhost", 8000);
        client.working();
    }

}
