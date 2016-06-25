package com.yang.httplib;

/**
 * Created by my on 2016/6/20.
 */
public class HttpHelper {
    private static HttpHelper instance;
    private RequestQueue mQueue;

    public static HttpHelper getInstance() {
        if(instance==null) {
            synchronized (HttpHelper.class) {
                if (instance == null) {
                    instance = new HttpHelper();
                }

            }
        }
        return instance;
    }
    private HttpHelper(){
        mQueue=new RequestQueue();
    }
    public static void adddRequest(Request request){
        getInstance().mQueue.addRequest(request);
    }

}
