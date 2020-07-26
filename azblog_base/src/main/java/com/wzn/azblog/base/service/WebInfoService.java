package com.wzn.azblog.base.service;

import com.wzn.ablog.common.entity.WebInfo;
import com.wzn.azblog.base.dao.WebInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WebInfoService {

    @Autowired
    private WebInfoDao webInfoDao;

    public List<WebInfo> showInfo(){
        List<WebInfo> list = webInfoDao.findAll();
        return list;
    }

    public void updateInfo(String content){
        webInfoDao.updateInfo(content);
    }
}
