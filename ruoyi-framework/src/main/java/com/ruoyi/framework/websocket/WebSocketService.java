package com.ruoyi.framework.websocket;

import com.ruoyi.framework.websocket.ServerEncoder;
import com.ruoyi.framework.websocket.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @desc: WebSocketService实现
 * @author: LiuCh
 * @since: 2021/8/16
 */

//ServerEncoder 是为了解决编码异常，如果不需要使用sendObject()方法，这个可以忽略，只写value即可
@ServerEndpoint(value = "/websocket/{userName}",encoders = {ServerEncoder.class})
@Component
public class WebSocketService {
    @Autowired
    private RedisTemplate<Object,Object>redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);

    /**
     *     静态变量，用来记录当前在线连接数
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
     */
    private static ConcurrentHashMap<String, WebSocketClient> webSocketMap = new ConcurrentHashMap<>();


    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userName 用来区别不同的用户*/
    private String userName="";
    /**
     * 连接建立成功调用的方法 可根据自己的业务需求做不同的处理*/
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {

        if(!webSocketMap.containsKey(userName))
        {
            addOnlineCount(); // 在线数 +1
        }
        this.session = session;
        this.userName= userName;
        WebSocketClient client = new WebSocketClient();
        client.setSession(session);
        client.setUri(session.getRequestURI().toString());
        webSocketMap.put(userName, client);
//        log.info("用户连接:"+userName+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userName)){
            webSocketMap.remove(userName);
            if(webSocketMap.size()>0)
            {
                //从set中删除
                subOnlineCount();
            }
        }
        log.info(userName+"用户退出,当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到用户消息:"+userName+",报文:"+message);
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userName+",原因:"+error.getMessage());

        error.printStackTrace();
    }

    /**
     * 连接服务器成功后主动推送
     */
    public void sendMessage(String message) throws IOException {
        synchronized (session){
            this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 向指定客户端发送消息（字符串形式）
     * @param userName
     * @param message
     */
    public static void sendMessage(String userName,String message){
        try {
            WebSocketClient webSocketClient = webSocketMap.get(userName);
            if(webSocketClient!=null){
                webSocketClient.getSession().getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 向指定客户端发送消息(对象的形式)
     * @param userName
     * @param object
     */
    public static void sendMessage(String userName,Object object){
        try {
            WebSocketClient webSocketClient = webSocketMap.get(userName);
            if(webSocketClient!=null){
                webSocketClient.getSession().getBasicRemote().sendObject(object);
            }
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
