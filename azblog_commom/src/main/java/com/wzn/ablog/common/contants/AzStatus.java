package com.wzn.ablog.common.contants;

import java.util.HashMap;
import java.util.Map;

public class AzStatus {
    public static final Integer PAGE = 0;
    public static final Integer SUCCESS = 200;
    public static final Integer ERR = 500;
    public static final Integer NOT_LOGIN = 501;
    public static final Integer ERR_TOKEN = 502;
    public static final Integer TOKEN_EXPIRE = 503;
    public static final Integer ERR_ROUTE = 503;
    public static final Integer HAS_USERNAME = 504;
    public static final Integer ROOT_ISNOT_EDIT = 505;
    public static final Integer ROOT_ISNOT_DELETE = 506;
    public static final Integer UNABLE_GET_USERNSAME = 507;

    public static final Map<Integer, String> RESP_MSG = new HashMap<>();
    static {
        RESP_MSG.put(SUCCESS,"操作成功");
        RESP_MSG.put(ERR,"操作失败");
        RESP_MSG.put(NOT_LOGIN,"请登陆后操作");
        RESP_MSG.put(ERR_TOKEN,"token错误");
        RESP_MSG.put(TOKEN_EXPIRE,"身份过期请重新登录");
        RESP_MSG.put(ERR_ROUTE,"网关异常");
        RESP_MSG.put(HAS_USERNAME,"用户名已存在");
        RESP_MSG.put(ROOT_ISNOT_EDIT,"root用户不可编辑");
        RESP_MSG.put(ROOT_ISNOT_DELETE,"root用户不可删除");
        RESP_MSG.put(UNABLE_GET_USERNSAME,"无法获取用户名");
    }
}
