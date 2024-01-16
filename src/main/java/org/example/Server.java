package org.example;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.snf4j.core.SelectorLoop;
import org.snf4j.core.factory.AbstractSessionFactory;
import org.snf4j.core.handler.IStreamHandler;

public class Server {
    static final int PORT = 2536;

    public static void main(String[] args) throws Exception {
        SelectorLoop loop = new SelectorLoop();
        try {
            loop.start();

            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(PORT));

            loop.register(channel, new AbstractSessionFactory() {
                @Override
                protected IStreamHandler createHandler(SocketChannel channel) {
                    return new ServerHandler();
                }
            }).sync();

            loop.join();
        }
        finally {
            loop.stop();
        }
    }
}