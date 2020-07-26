package com.wzn.azblog.base.service;

import com.wzn.ablog.common.entity.Link;
import com.wzn.ablog.common.utils.IdWorker;
import com.wzn.azblog.base.dao.LinkDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LinkService {

    @Autowired
    private LinkDao linkDao;

    @Autowired
    private IdWorker idWorker;

    public Page<Link> list(int page,int limit){
        Page<Link> pageInfo = linkDao.findAll(PageRequest.of(page - 1, limit));
        return pageInfo;
    }

    public void add(Link link){
        link.setId(idWorker.nextId()+"");
        link.setStatus(1);
        linkDao.save(link);
    }

    public void update(Link link){
        linkDao.update(link.getTitle(),link.getSummary(),link.getUrl(),link.getId());
    }

    public void del(String[] ids){
        for (String id: ids){
            linkDao.deleteById(id);
        }
    }

    public Link findById(String id) {
        Link link = linkDao.findById(id).get();
        return link;
    }

    public Page<Link> search(String keywords,int page,int limit){
        //查询条件存在这个对象中
        Specification<Link> specification = new Specification<Link>() {
            @Override
            public Predicate toPredicate(Root<Link> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //要模糊查询的字段
                Path title = root.get("title");
                //criteriaBuilder.like模糊查询，第一个参数是上一行的返回值，第二个参数是like('%xxx%')中，xxx的值
                Predicate predicate = criteriaBuilder.like(title, "%" + keywords + "%");
                return predicate;
            }
        };
        Page<Link> links = linkDao.findAll(specification, PageRequest.of(page - 1, limit));
        return links;
    }
}
