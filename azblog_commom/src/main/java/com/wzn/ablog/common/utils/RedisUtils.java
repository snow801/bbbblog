package com.wzn.ablog.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class RedisUtils {

    //清除全部缓存
    public static void clearRedis(Integer totaPage, String userId, RedisTemplate redisTemplate) {
        log.debug(userId );
        for (int i = 1; i <= totaPage; i++) {
            log.debug(userId + i);
            redisTemplate.delete("articles" + userId + i);
        }
    }
}
