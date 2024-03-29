package org.example;

import org.snf4j.core.handler.AbstractStreamHandler;
import org.snf4j.core.handler.SessionEvent;
import org.snf4j.core.session.IStreamSession;

import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends AbstractStreamHandler {
    private static Integer USERID = 0;

    private static String YOUID = "[you]";

    static final Map<Long, IStreamSession> sessions = new HashMap<Long, IStreamSession>();

    @Override
    public void read(Object msg) {
        String s = new String((byte[])msg);

        send(s);
        if ("bye".equalsIgnoreCase(s)) {
            getSession().close();
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void event(SessionEvent event) {
        switch (event) {
            case OPENED:
                sessions.put(getSession().getId(), getSession());
                getSession().getAttributes().put(USERID, "["+getSession().getRemoteAddress()+"]");
                send("{connected}");
                break;

            case CLOSED:
                sessions.remove(getSession().getId());
                send("{disconnected}");
                break;
        }
    }

    private void send(String message) {
        long youId = getSession().getId();
        String userId = (String) getSession().getAttributes().get(USERID);

        for (IStreamSession session: sessions.values()) {
            session.write(((session.getId() == youId ? YOUID : userId) + ' ' + message).getBytes());
        }
    }
}
