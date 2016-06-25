package com.yang.httplib;

import java.util.HashMap;

/**
 * Created by my on 2016/6/20.
 */
 abstract public class Request<T> {
    private String url;
    private Callback callback;
    private Method method;


    public Request(String url,Method method,Callback callback) {
        this.callback = callback;
        this.method = method;
        this.url = url;
    }
    public enum  Method{
        GET,POST
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
    public void onError(Exception e) {
        callback.onEror(e);
    }

    protected void onResponse(T res) {
        callback.onResonse(res);
    }
    public  interface Callback<T>{

        void onEror(Exception e);
        void onResonse(T response);
    }
    public HashMap<String,String> getPostParams(){
        return null;
    }
    abstract public void dispatchContent(byte[] content);
}
