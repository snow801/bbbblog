package com.wzn.azblog.Statistics.webSocket;

import com.wzn.ablog.common.entity.Visitor;
import com.wzn.azblog.Statistics.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Service
@ServerEndpoint("/webSocket")
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();

    private static RedisTemplate redisTemplate;

    private static VisitorService visitorService;

    @Autowired
    public void setRabbitAdmin(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setVisitorService(VisitorService visitorService){
        this.visitorService = visitorService;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        InetSocketAddress address = WebsocketUtil.getRemoteAddress(session);
        webSockets.add(this);
        log.debug("有新的连接，总数" + webSockets.size());
        String host = session.getRequestURI().getHost();
        Visitor visitor = new Visitor();
        visitor.setHost(host);
        visitorService.save(visitor);
        redisTemplate.opsForValue().set("onlineCount",webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        webSockets.remove(this);
        System.out.println("有新的断开，总数" + webSockets.size());
        redisTemplate.opsForValue().set("onlineCount",webSockets.size());
    }


}
