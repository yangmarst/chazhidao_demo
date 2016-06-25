package com.yang.httplib;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by my on 2016/6/20.
 * 阻塞队列类
 */
public class RequestQueue {
    //定义阻塞的队列
    private BlockingDeque<Request> requestQueue=new LinkedBlockingDeque<>();
    //定义后台访问网络的线程数的常量
    private static final int MAX_THREAD=3;
    private NetWorkDispatcher[] dispatchers=new NetWorkDispatcher[MAX_THREAD];
    public RequestQueue(){
        initDistQueue();
    }
    //创建一个网络线程访问类
    private void initDistQueue() {
        for(int i=0;i<dispatchers.length;i++){
            dispatchers[i]=new NetWorkDispatcher(requestQueue);
            dispatchers[i].start();
        }
    }
    public void addRequest(Request request){
        requestQueue.add(request);
    }

    private void stop(){
        for(int i=0;i<dispatchers.length;i++) {
            dispatchers[i].flag=false;
            dispatchers[i].isInterrupted();
        }
    }
}
