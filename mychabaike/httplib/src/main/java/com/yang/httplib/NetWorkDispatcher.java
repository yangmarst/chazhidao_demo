package com.yang.httplib;

import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

/**
 * Created by my on 2016/6/20.
 */
public class NetWorkDispatcher extends Thread{
        //Class.getName();以String的形式，返回Class对象的‘实体’名称
    private  static final String TAG=NetWorkDispatcher.class.getSimpleName();
    private BlockingDeque<Request> mQueue;
    public boolean flag=true;
    NetWorkDispatcher(BlockingDeque<Request> queue){
      mQueue=queue;
    }

    @Override
    public void run() {
        while (flag&&!isInterrupted()){
            try {
                final Request req=mQueue.take();
                byte[] result=null;
                try {
                    result=getNetworkResponse(req);
                    if(result!=null){
                        req.dispatchContent(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    req.onError(e);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
               flag=false;
            }
        }


    }

    public byte[] getNetworkResponse(Request request) throws Exception{
        if(TextUtils.isEmpty(request.getUrl())){
            throw new Exception("url is null");
        }
        if(request.getMethod()== Request.Method.GET){
            return getResponseByGet(request);
        }
        if(request.getMethod()==Request.Method.POST){
            return getResponseByPost(request);
        }
        return null;
    }

    public byte[] getResponseByPost(Request request) throws Exception{
        InputStream is=null;
        OutputStream os=null;
       URL  url=new URL(request.getUrl());
        HttpURLConnection connection= (HttpURLConnection) url.getContent();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(4000);
        connection.setDoInput(true);
        String str=getPostEncodeString(request);
        byte[] buffer=null;
        if(str!=null){
            buffer=str.getBytes();
            connection.setRequestProperty("buffer-length", "" + buffer.length);
        }
        os=connection.getOutputStream();
        if(buffer!=null){
            os.write(buffer);
            os.flush();
        }
        int code=connection.getResponseCode();
        if(code!=200){
            Log.i(TAG, "getNetworkResponse() returned: response code error code=" + code);
                throw new Exception("code error");
        }
        is=connection.getInputStream();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        int length=0;
        byte[] byt=new byte[2048];
        if((length=is.read(byt))!=-1){
            baos.write(byt, 0, byt.length);

        }
        is.close();;
        os.close();
        return baos.toByteArray();
    }


    private byte[] getResponseByGet(Request request) throws Exception{
        InputStream is=null;
        URL  url=new URL(request.getUrl());
        HttpURLConnection connection= (HttpURLConnection) url.getContent();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(4000);
        connection.setDoInput(true);
        int code=connection.getResponseCode();
        if(code!=200){
            Log.i(TAG, "getNetworkResponse() returned: response code error code=" + code);
            throw new Exception("code error");
        }
        is=connection.getInputStream();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        int length=0;
        byte[] byt=new byte[2048];
        if((length=is.read(byt))!=-1){
            baos.write(byt, 0, byt.length);
        }
        is.close();
        return baos.toByteArray();
    }
    private String getPostEncodeString(Request request) {
        HashMap<String ,String> params=request.getPostParams();
        StringBuilder builder=new StringBuilder();
        if(params!=null){
            Iterator<Map.Entry<String,String>> iterator=params.entrySet().iterator();
            int i=0;
            while (iterator.hasNext()){
                if(i>0){
                    builder.append("&");
                }
                Map.Entry<String, String> value = iterator.next();
                String str = value.getKey() + "=" + value.getValue();
                builder.append(str);
                i++;
            }
            return builder.toString();
        }
        return null;
    }
}
