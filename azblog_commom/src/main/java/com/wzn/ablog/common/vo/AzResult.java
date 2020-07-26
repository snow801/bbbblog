package com.wzn.ablog.common.vo;

import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.contants.AzStatus;
import lombok.Data;

@Data
public class AzResult {

    private Integer status;
    private String message;
    private Object data;

    private AzResult() {
    }

    public static AzResult ok(){
        return ok(AzContants.SUCCESS_MSG);
    }

    public static AzResult ok(Integer status){
        return ok(status,AzStatus.RESP_MSG.get("status"));
    }

    public static AzResult ok(String msg){
        return ok(200,msg);
    }

    public static AzResult ok(Integer status, String msg) {
        AzResult azResult = new AzResult();
        azResult.setStatus(status);
        azResult.setMessage(msg);
        return azResult;
    }

    public static AzResult err(){
        return err(AzContants.ERR_MSG);
    }

    public static AzResult err(Integer status){
        return err(status,AzStatus.RESP_MSG.get("status"));
    }

    public static AzResult err(String msg){
        return err(500,msg);
    }

    public static AzResult err(Integer status, String msg) {
        AzResult azResult = new AzResult();
        azResult.setStatus(status);
        azResult.setMessage(msg);
        return azResult;
    }

    public AzResult data(Object obj){
        this.data = obj;
        return this;
    }


}
