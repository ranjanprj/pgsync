/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pgpower.pgaync;

import java.sql.*;

public class NotificationTest {

    public static void main(String args[]) throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/postgres";

        // Create two distinct connections, one for the notifier
        // and another for the listener to show the communication
        // works across connections although this example would
        // work fine with just one connection.
        Connection lConn = DriverManager.getConnection(url, "postgres", "Linuxlove01");
        Connection nConn = DriverManager.getConnection(url, "postgres", "Linuxlove01");

        // Create two threads, one to issue notifications and
        // the other to receive them.
        Listener listener = new Listener(lConn);
        Notifier notifier = new Notifier(nConn);
        listener.start();
        notifier.start();
    }

}

class Listener extends Thread {

    private Connection conn;
    private org.postgresql.PGConnection pgconn;

    Listener(Connection conn) throws SQLException {
        this.conn = conn;
        this.pgconn = (org.postgresql.PGConnection) conn;
        Statement stmt = conn.createStatement();
        stmt.execute("LISTEN mymessage");
        stmt.close();
    }

    public void run() {
        long prev = 0l;
        while (true) {
            try {
                // issue a dummy query to contact the backend
                // and receive any pending notifications.
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1");
                rs.close();
                stmt.close();

                org.postgresql.PGNotification notifications[] = pgconn.getNotifications();
                if (notifications != null) {
                    for (int i = 0; i < notifications.length; i++) {
                        if (Long.valueOf(notifications[i].getParameter()) != (prev + 1)) {
                            System.out.println("**************************************");

                            System.out.println("**************************************");

                            System.out.println("**************************************");
                        }else{
                            prev = Long.valueOf(notifications[i].getParameter());
                        }
                        System.out.println("Got notification: " + notifications[i].getName() + notifications[i].getParameter());
                    }
                }

                // wait a while before checking again for new
                // notifications
//				Thread.sleep(100);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
//            } catch (InterruptedException ie) {
//                ie.printStackTrace();
            }
        }
    }

}

class Notifier extends Thread {

    private Connection conn;

    public Notifier(Connection conn) {
        this.conn = conn;
    }

    public void run() {
        long cnt = 0l;
        while (true) {
            try {
                Statement stmt = conn.createStatement();
                stmt.execute("NOTIFY mymessage,'" + cnt++ + "'");
                stmt.close();
				Thread.sleep(1000);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

}
