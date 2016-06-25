package com.yang.mychabaike.ui.activity.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yang.httplib.HttpHelper;
import com.yang.httplib.Request;
import com.yang.httplib.StringRequest;
import com.yang.mychabaike.adapter.InfoListAdapter;
import com.yang.mychabaike.beans.Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import yang.mychabaike.R;

/**
 * Created by my on 2016/6/22.
 */
public class ContentFragment extends Fragment {
    private ListView listView;
    private int class_id;
    private PtrClassicFrameLayout refreshView;
    private InfoListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                              Bundle bundle) {
        View view=View.inflate(getActivity(), R.layout.fragment_content,null);
        initView(view);
        class_id=getArguments().getInt("id");
        if(bundle!=null){
            //Parcelable保存对象的属性到本地文件、数据库、网络流、rmi以方便数据传输
         Parcelable[] ps= bundle.getParcelableArray("cache");
         Info[] inf= (Info[]) bundle.getParcelableArray("cache");
            if(inf!=null&&inf.length!=0){
                infos= Arrays.asList(inf);
                adapter=new InfoListAdapter(infos);
                listView.setAdapter(adapter);
            }else {
                getDataFromNetWork();
            }
        }else {
            getDataFromNetWork();
        }
        System.out.println("onCreateView==============ContentFragment");
        return view;

    }



    private void initView(View view) {
        listView= (ListView) view.findViewById(R.id.frag_content_lv);
        refreshView= (PtrClassicFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
        refreshView.setResistance(1.7f);
        refreshView.setRatioOfHeaderHeightToRefresh(1.2f);
        refreshView.setDurationToClose(200);
        refreshView.setDurationToCloseHeader(1000);
        refreshView.setPullToRefresh(true);
        refreshView.setKeepHeaderWhenRefresh(true);
        refreshView.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getDataFromNetWork();
            }
        });

    }
    private List<Info> infos=new ArrayList<>();
    //从网络获取（字符串）数据
    private void getDataFromNetWork() {
        String url="http://www.tngou.net/api/info/list?id="+class_id;
         StringRequest srq=new StringRequest(url, Request.Method.GET, new Request.Callback<String>() {
            @Override
            public void onEror(Exception e) {

            }

            @Override
            public void onResonse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    System.out.println(response);
                    List<Info> listinfo=parseJson2List(jsonObject);
                    if(listinfo!=null){
                        infos.clear();
                        infos.addAll(listinfo);
                        if(adapter==null){
                            adapter=new InfoListAdapter(infos);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(adapter);
                                }
                            });
                        }else {
                            //在主线程更新数据
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.refreshComplete();
                    }
                });
            }
        });
        HttpHelper.adddRequest(srq);
        System.out.println("getDataFromNetWork============ContentFragment");
    }

 private List<Info> parseJson2List(JSONObject jsonObject) throws JSONException{
     System.out.println(jsonObject);
     if(jsonObject==null)return null;
     JSONArray jsonArray=jsonObject.getJSONArray("tngou");
     if(jsonArray==null||jsonArray.length()==0)return null;
     List<Info> list=new ArrayList<>();
     int leng=jsonArray.length();
     JSONObject obj=null;
     Info info=null;
     for(int i=0;i<leng;i++){
         obj=jsonArray.getJSONObject(i);
         info=new Info();
         info.setDescription(obj.optString("description"));
         info.setRcount(obj.optInt("count"));
         long time=obj.getLong("time");
         String str=new SimpleDateFormat("yyMMdd:hhmmss").format(time);
         info.setTime(str);
         info.setImg(obj.optString("img"));
             info.setId(obj.optInt("Id"));
         list.add(info);
     }
     System.out.println("parseJson2List================ContentFragment");
     return list;
 }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(infos==null||infos.size()==0) return;
        Info[] parce=new Info[infos.size()];
        infos.toArray(parce);
        outState.putParcelableArray("cache",parce);
    }
}
