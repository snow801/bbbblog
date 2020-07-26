package com.wzn.ablog.article.service;

import com.wzn.ablog.article.dao.CategoryDao;
import com.wzn.ablog.common.entity.Category;
import com.wzn.ablog.common.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    //查找全部
    public Page<Category> list(Integer page, Integer limit){
        return categoryDao.findAll(PageRequest.of(page-1,limit));
    }

    //根据id查找
    public String findNameById(String id){
        String name = (String) redisTemplate.opsForValue().get(id);
        if(name == null){
            name =  categoryDao.findNameById(id);
            redisTemplate.opsForValue().set(id,name);
        }else {
            return name;
        }
        return name;
    }

    //根据id删除
    public void del(String[] ids) {
        for(String id : ids){
            redisTemplate.delete(id);
            categoryDao.deleteById(id);
        }
    }

    //添加
    public void add(Category category) {
        category.setId(idWorker.nextId()+"");
        category.setStatus("1");
        categoryDao.save(category);
    }

    //更新
    public void update(Category category) {
        redisTemplate.delete(category.getId());
        categoryDao.update(category.getName(),category.getId());
    }

    //不分页查找全部
    public List<Category> list(){
        return categoryDao.findAll();
    }
}
