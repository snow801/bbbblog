package com.wzn.azblog.Statistics.service;

import com.wzn.ablog.common.entity.Visitor;
import com.wzn.ablog.common.utils.IdWorker;
import com.wzn.azblog.Statistics.dao.VisitorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VisitorService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private VisitorDao visitorDao;

    //保存访客信息
    public void save(Visitor visitor){
        Visitor byHost = visitorDao.findByHost(visitor.getHost());
        if(byHost == null){
            visitor.setId(idWorker.nextId()+"");
            visitorDao.save(visitor);
        }
    }

    //获取访客数量
    public long getVisitorCount(){
        long count = visitorDao.count();
        return count;
    }
}
