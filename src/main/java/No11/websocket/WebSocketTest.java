package No11.websocket;


import net.sf.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/webSocket/{username}")
public class WebSocketTest {
    private static int onlineCount = 0;

    private static Map<String, WebSocketTest> clients = new ConcurrentHashMap<String, WebSocketTest>();

    private Session session;

    private String username;

    //打开连接
    @OnOpen
    public void onOpen(@PathParam("username") String username,Session session) throws IOException{
        this.username = username;
        this.session = session;
        addOnlineCount();

        clients.put(username, this);

        System.out.println("已连接");
    }

    //关闭连接
    @OnClose
    public void onClose(){
        clients.remove(username);
        System.out.println("断开");
        subOnlineCount();
    }

    @OnMessage
    public void onMessage(String message)throws IOException{
        JSONObject jsonTo = JSONObject.fromObject(message);
        String mes = (String) jsonTo.get("message");
        if (!jsonTo.get("To").equals("All")){
            sendMessageTo(mes, jsonTo.get("To").toString());
        }else{
            sendMessageAll(mes);
        }
    }

    public void sendMessageTo(String message, String To) throws IOException {

        // session.getBasicRemote().sendText(message);

        //session.getAsyncRemote().sendText(message);

        for (WebSocketTest item : clients.values()) {
            if (item.username.equals(To) )
                item.session.getAsyncRemote().sendText(message);
        }

    }

    public static synchronized int getOnlineCount() {

        return onlineCount;

    }

    public void sendMessageAll(String message) throws IOException {

        for (WebSocketTest item : clients.values()) {

            item.session.getAsyncRemote().sendText(message);

        }

    }

    @OnError

    public void onError(Session session, Throwable error) {

        error.printStackTrace();

    }
    public static synchronized Map<String, WebSocketTest> getClients() {

        return clients;

    }
    private void subOnlineCount() {
        onlineCount--;
    }
    private void addOnlineCount() {
        onlineCount++;
    }
}
