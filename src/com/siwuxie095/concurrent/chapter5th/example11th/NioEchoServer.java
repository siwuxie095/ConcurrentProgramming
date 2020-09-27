package com.siwuxie095.concurrent.chapter5th.example11th;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiajing Li
 * @date 2020-09-26 17:47:53
 */
@SuppressWarnings("all")
class NioEchoServer {

    private Selector selector;

    private ExecutorService executor = Executors.newCachedThreadPool();

    public static Map<Socket, Long> timeStatistic = new HashMap<>(10240);

    private void startServer() throws IOException {
        selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        //InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 8000);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8000);
        serverSocketChannel.socket().bind(inetSocketAddress);

        SelectionKey acceptKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        for (;;) {
            // 阻塞，等待数据
            selector.select();
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            long end = 0;
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    doAccept(selectionKey);
                } else if (selectionKey.isValid() && selectionKey.isReadable()) {
                    if (!timeStatistic.containsKey(((SocketChannel) selectionKey.channel()).socket())) {
                        timeStatistic.put(((SocketChannel) selectionKey.channel()).socket(), System.currentTimeMillis());
                    }
                    doRead(selectionKey);
                } else if (selectionKey.isValid() && selectionKey.isWritable()) {
                    doWrite(selectionKey);
                    end = System.currentTimeMillis();
                    long begin = timeStatistic.remove(((SocketChannel) selectionKey.channel()).socket());
                    System.out.println("spend: " + (end - begin) + " ms");
                }
            }
        }
    }

    private void doAccept(SelectionKey selectionKey) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel clientSocketChannel;
        try {
            clientSocketChannel = serverSocketChannel.accept();
            clientSocketChannel.configureBlocking(false);

            // register this channel for reading
            SelectionKey clientKey = clientSocketChannel.register(selector, SelectionKey.OP_READ);
            // allocate an EchoClient instance and attach it to this selection key
            EchoClient echoClient = new EchoClient();
            clientKey.attach(echoClient);

            InetAddress clientAddress = clientSocketChannel.socket().getInetAddress();
            System.out.println("Accepted connection from " + clientAddress.getHostAddress());
        } catch (IOException e) {
            System.out.println("Failed to accept new client");
            e.printStackTrace();
        }
    }

    private void doRead(SelectionKey selectionKey) {
        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
        int len;
        try {
            len = clientSocketChannel.read(byteBuffer);
            if (len < 0) {
                disconnect(selectionKey);
                return;
            }
        } catch (IOException e) {
            System.out.println("Failed to read from client");
            e.printStackTrace();
            disconnect(selectionKey);
            return;
        }

        byteBuffer.flip();
        executor.execute(new HandleMsg(selectionKey, byteBuffer));
    }

    private void disconnect(SelectionKey selectionKey) {
        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
        try {
            clientSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(SelectionKey selectionKey) {
        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
        EchoClient echoClient = (EchoClient) selectionKey.attachment();
        LinkedList<ByteBuffer> outputQueue = echoClient.getOutputQueue();

        ByteBuffer byteBuffer = outputQueue.getLast();
        try {
            int len = clientSocketChannel.write(byteBuffer);
            if (len == -1) {
                disconnect(selectionKey);
                return;
            }
            if (byteBuffer.remaining() == 0) {
                // the buffer was completely written, remove it.
                outputQueue.removeLast();
            }
        } catch (IOException e) {
            System.out.println("Failed to write client");
            e.printStackTrace();
            disconnect(selectionKey);
        }
        if (outputQueue.size() == 0) {
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    class HandleMsg implements Runnable {

        private SelectionKey selectionKey;

        private ByteBuffer byteBuffer;

        public HandleMsg(SelectionKey selectionKey, ByteBuffer byteBuffer) {
            this.selectionKey = selectionKey;
            this.byteBuffer = byteBuffer;
        }

        @Override
        public void run() {
            EchoClient echoClient = (EchoClient) selectionKey.attachment();
            echoClient.enqueue(byteBuffer);
            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            // 强迫 selector 立即返回
            selector.wakeup();
        }
    }

    static class EchoClient {

        private LinkedList<ByteBuffer> outputQueue;

        public EchoClient() {
            outputQueue = new LinkedList<>();
        }

        public LinkedList<ByteBuffer> getOutputQueue() {
            return outputQueue;
        }

        public void enqueue(ByteBuffer byteBuffer) {
            outputQueue.addFirst(byteBuffer);
        }
    }

    public static void main(String[] args) throws IOException {
        NioEchoServer echoServer = new NioEchoServer();
        echoServer.startServer();
    }

}
