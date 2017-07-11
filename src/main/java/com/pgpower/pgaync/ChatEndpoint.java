package com.pgpower.pgaync;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.postgresql.PGConnection;

@ServerEndpoint(
        value = "/chat/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)
public class ChatEndpoint {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Session session;
    private static Set<ChatEndpoint> chatEndpoints
            = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) throws IOException, EncodeException, SQLException, InterruptedException, ClassNotFoundException {

        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), username);

        Message message = new Message();
        message.setFrom(username);
        message.setContent("Connected!");
        broadcast(message);
        saySomething();
       
    }

    @OnMessage
    public void onMessage(Session session, Message message)
            throws IOException, EncodeException, SQLException, ClassNotFoundException {
        message.setFrom(users.get(session.getId()));
        
         broadcast(message);
        
        
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {

        chatEndpoints.remove(this);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("Disconnected!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void broadcast(Message message)
            throws IOException, EncodeException {

        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().
                            sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void saySomething() throws IOException, EncodeException, InterruptedException, SQLException, ClassNotFoundException {
       System.out.println("ON MESSAGE CALLED");
       Message message = new Message();
//        broadcast(message);
        //----------------------------------------
        Class.forName("org.postgresql.Driver");

        // Create two distinct connections, one for the notifier
        // and another for the listener to show the communication
        // works across connections although this example would
        // work fine with just one connection.
        Statement stmt = PGConnect.getConnection().createStatement();
        stmt.execute("LISTEN mymessage");
        stmt.close();
        PGConnection pgconn = (org.postgresql.PGConnection) PGConnect.getConnection();
        long prev = 0l;
        while (true) {
            try {
                // issue a dummy query to contact the backend
                // and receive any pending notifications.
                stmt = PGConnect.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1");
                rs.close();
                stmt.close();

                org.postgresql.PGNotification notifications[] = pgconn.getNotifications();
                if (notifications != null) {
                    for (int i = 0; i < notifications.length; i++) {
                        if (Long.valueOf(notifications[i].getParameter()) != (prev + 1)) {
                            System.out.println("**************************************");                         
                        } else {
                            prev = Long.valueOf(notifications[i].getParameter());
                        }
                        System.out.println("Got notification: " + notifications[i].getName() + notifications[i].getParameter());
                        message.setContent( notifications[i].getParameter());
                        broadcast(message);
                    }
                }

                // wait a while before checking again for new
                // notifications
                Thread.sleep(2000);
                
                
                
                
            } catch (SQLException | InterruptedException sqle) {
                sqle.printStackTrace();
            }
            
            
           
        }

        //------------------------------------------
    }
}


class MyListener extends Thread{
    @Override
    public void run() {
        try {
            this.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}