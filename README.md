POC to show WebSocket updates in OPENUI5 , Postgres Listen/Notify and Java Servlet 3.0

1. Have an Postgresql running.
2. Change the connection parameters in PGConnect.java file.
3. Run the NotificationTest.java to generate some NOTIFY signals to Postgres.
4. Deploy the pgasync app into Tomcat and run the index.html app.


![OpenUI5, PG Listen/Notify Websocket](https://github.com/ranjanprj/pgsync/blob/master/src/main/webapp/PG_UI5_LISTENNOTIFY.gif "OpenUI5, PG Listen/Notify Websocket")