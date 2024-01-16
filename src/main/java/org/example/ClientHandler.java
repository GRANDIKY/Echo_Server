package org.example;

import org.snf4j.core.EndingAction;
import org.snf4j.core.handler.AbstractStreamHandler;
import org.snf4j.core.handler.SessionEvent;
import org.snf4j.core.session.DefaultSessionConfig;
import org.snf4j.core.session.ISessionConfig;

public class ClientHandler extends AbstractStreamHandler {
    @Override
    public void read(Object msg) {
        System.err.println(new String((byte[])msg));
    }

    @Override
    public void event(SessionEvent event) {
        if (event == SessionEvent.CLOSED) {
            if (!getSession().getAttributes().containsKey(Client.BYE_TYPED)) {
                System.err.println("Connection closed. Type \"bye\" to exit");
            }
        }
    }

    @Override
    public ISessionConfig getConfig() {
        return new DefaultSessionConfig()
                .setEndingAction(EndingAction.STOP);
    }
}