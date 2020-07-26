package com.wzn.blog.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.vo.Result;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.net.NoRouteToHostException;

@Slf4j
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        log.info("路径："+serverWebExchange.getRequest().getPath()+"  异常信息："+throwable);
        Result result = new Result();
        if (throwable instanceof NullPointerException) {
            result.setStatus(AzStatus.NOT_LOGIN).setMessage(AzStatus.RESP_MSG.get(AzStatus.NOT_LOGIN));
        } else if (throwable instanceof SignatureException) {
            result.setStatus(AzStatus.ERR_TOKEN).setMessage(AzStatus.RESP_MSG.get(AzStatus.ERR_TOKEN));
        } else if(throwable instanceof ExpiredJwtException){
            result.setStatus(AzStatus.TOKEN_EXPIRE).setMessage(AzStatus.RESP_MSG.get(AzStatus.TOKEN_EXPIRE));
        }else if(throwable instanceof NoRouteToHostException){
            result.setStatus(AzStatus.ERR_ROUTE).setMessage(AzStatus.RESP_MSG.get(AzStatus.ERR_ROUTE));
        }
        else{
            result.setStatus(AzStatus.ERR).setMessage(AzContants.ERR_SERVICE);
        }

        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        return serverWebExchange.getResponse().writeWith(
                Flux.just(bufferFactory.wrap(JSON.toJSONString(result).getBytes())));
    }
}
