package com.wzn.ablog.admin.service;

import com.wzn.ablog.admin.dao.PortDao;
import com.wzn.ablog.common.entity.Port;
import com.wzn.ablog.common.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;

@Service
@Transactional
public class PortService {

    @Autowired
    private PortDao portDao;

    @Autowired
    private IdWorker idWorker;

    //接口列表
    public Page<Port> list(int page,int limit){
        Page<Port> portPage = portDao.findAll(PageRequest.of(page - 1, limit));
        return portPage;
    }

    //搜索接口
    public Page<Port> search(String keyword, int page, int limit) {
        Specification<Port> specification = new Specification<Port>() {
            @Override
            public Predicate toPredicate(Root<Port> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //要模糊查询的字段
                Path description = root.get("description");
                //criteriaBuilder.like模糊查询，第一个参数是上一行的返回值，第二个参数是like('%xxx%')中，xxx的值
                Predicate predicate = criteriaBuilder.like(description, "%" + keyword + "%");
                return predicate;
            }
        };
        Page<Port> ports = portDao.findAll(specification, PageRequest.of(page - 1, limit));
        return ports;
    }
}
