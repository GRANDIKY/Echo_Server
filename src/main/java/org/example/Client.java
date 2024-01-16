package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.snf4j.core.SelectorLoop;
import org.snf4j.core.session.IStreamSession;

public class Client {
    static final String HOST = "127.0.0.1";
    static final int PORT = 2536;
    static final Integer BYE_TYPED = 0;

    public static void main(String[] args) throws Exception {
        SelectorLoop loop = new SelectorLoop();

        try {
            loop.start();

            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(InetAddress.getByName(HOST), PORT));

            IStreamSession session = (IStreamSession) loop.register(channel, new ClientHandler()).sync().getSession();

            session.getReadyFuture().sync();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = in.readLine()) != null) {
                if (session.isOpen()) {
                    session.write((line).getBytes());
                }
                if ("bye".equalsIgnoreCase(line)) {
                    session.getAttributes().put(BYE_TYPED, BYE_TYPED);
                    break;
                }
            }
        }
        finally {
            loop.stop();
        }
    }
}
