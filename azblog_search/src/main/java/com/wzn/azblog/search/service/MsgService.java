package com.wzn.azblog.search.service;

import com.wzn.azblog.search.quartz.EsQuartz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableBinding(Sink.class)
public class MsgService {

    @Autowired
    private EsQuartz esQuartz;

    @StreamListener(Sink.INPUT)
    public void getMsg(String msg){
        log.debug(msg);
        System.out.println(msg);
        if(msg != null && msg.equals("syncIndex")){
            esQuartz.syncArticle();
        }
    }
}
