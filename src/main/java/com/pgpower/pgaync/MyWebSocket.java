/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pgpower.pgaync;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/*
 * @WebServlet indicates the url of this file (ex: localhost:8080/WSTest/ws)
*/
@ServerEndpoint(value = "/data.socket")
public class MyWebSocket {
    @OnOpen
    public void onOpen(Session session) {
        // Here is where i need the origin and remote client address
        
        System.out.println("ONOPEN****************8888");
    }

    @OnClose
    public void onClose() {
        // disconnection handling
    }

    @OnMessage
    public void onMessage(String message) {
        // message handling
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Error handling
    }
}
