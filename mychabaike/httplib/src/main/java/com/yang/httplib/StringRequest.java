package com.yang.httplib;

/**
 * Created by my on 2016/6/22.
 */
public class StringRequest extends Request<String>{

    public StringRequest(String url, Method method, Callback callback) {
        super(url, method, callback);
    }

    @Override
    public void dispatchContent(byte[] content) {
        onResponse(new String(content));
    }

}
