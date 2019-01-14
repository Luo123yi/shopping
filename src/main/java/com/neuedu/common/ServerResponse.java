package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 响应前端的高复用对象
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) //排除null值
public class ServerResponse<T> {

    private Integer status;//状态码 0:成功
    private T data; //当status=0时，data对应的接口响应的数据
    private String msg; // 提示信息

    private ServerResponse(){}
    private ServerResponse(Integer status){
        this.status=status;
    }
    private ServerResponse(Integer status,String msg){
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(Integer status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }

    /**
     * 判断接口是否访问成功
     */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS;
    }

    //调用接口成功时回调
    /**
     *{"status":0}
     */
    public static ServerResponse createServerResponseBySuccess(){
        return new ServerResponse(ResponseCode.SUCCESS);
    }
    /**
     *{"status":0，"msg":"aaa"}
     */
    public static ServerResponse createServerResponseBySuccess(String msg){
        return new ServerResponse(ResponseCode.SUCCESS,msg);
    }
    /**
     *{"status":0，"msg":"aaa"，"date":{}}
     */
    public static <T> ServerResponse createServerResponseBySuccess(String msg,T data){
        return new ServerResponse(ResponseCode.SUCCESS,msg,data);
    }

    //调用接口失败时回调
    /**
     *{"status":1}
     */
    public static <T> ServerResponse createServerResponseByError(){
        return new ServerResponse(ResponseCode.ERROR);
    }
    /**
     *{"status":自定义}
     */
    public static <T> ServerResponse createServerResponseByError(Integer status){
        return new ServerResponse(status);
    }
    /**
     *{"status":1，"msg":"aaa"}
     */
    public static ServerResponse createServerResponseByError(String msg){
        return new ServerResponse(ResponseCode.ERROR,msg);
    }
    /**
     *{"status":自定义，"msg":"aaa"}
     */
    public static ServerResponse createServerResponseByError(Integer status,String msg){
        return new ServerResponse(status,msg);
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
