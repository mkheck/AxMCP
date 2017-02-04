package com.example;

import javax.websocket.*;

/**
 * Created by markheckler on 2/3/17.
 */
@ClientEndpoint
public class WsClientEndpoint {
    private Session session;
    private EndpointConfig config;

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        this.config = endpointConfig;
        System.out.println("Connected with session " + session.toString());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        this.session = null;
        this.config = null;
        System.out.println("Connection closed.");
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        System.out.println("Exception: " + thr.getLocalizedMessage());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message: " + message);
    }

    public Session getSession() {
        return session;
    }
}