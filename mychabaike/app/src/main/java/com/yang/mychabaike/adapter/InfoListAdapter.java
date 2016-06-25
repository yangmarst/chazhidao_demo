package com.yang.mychabaike.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.httplib.BitmapRequest;
import com.yang.httplib.HttpHelper;
import com.yang.httplib.Request;
import com.yang.mychabaike.beans.Info;

import java.util.List;

import yang.mychabaike.R;

/**
 * Created by my on 2016/6/22.
 * 自定义的信息处理适配器的类
 */
public class InfoListAdapter extends BaseAdapter{
    private static final String TAG=InfoListAdapter.class.getSimpleName();
    private  List<Info> infoList;

    public InfoListAdapter(List<Info> infoList) {
        this.infoList = infoList;
    }

    @Override
    public int getCount() {
        //判断如果list为空则返回0，否则返回list的长度
        return infoList==null?0:infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= View.inflate(parent.getContext(), R.layout.content_lv_item,null);
            holder.tv_desc= (TextView) convertView.findViewById(R.id.item_tv_desc);
            holder.tv_res= (TextView) convertView.findViewById(R.id.item_tv_res);
            holder.tv_time= (TextView) convertView.findViewById(R.id.item_tv_time);
            holder.iv_content= (ImageView) convertView.findViewById(R.id.item_iv);
            convertView.setTag(holder);
        }
        Info info= (Info) getItem(position);
        holder= (ViewHolder) convertView.getTag();
        holder.tv_desc.setText(info.getDescription());
        holder.tv_res.setText(info.getRcount()+"");
        holder.tv_time.setText(info.getTime());
        holder.iv_content.setImageResource(R.drawable.ic_launcher);
        LoadImage(holder.iv_content, "http://tnfs.tngou.net/image" + info.getImg() + "_100x100");
        System.out.println("getView=================InfoListAdapter");
        return convertView;
    }
    //定义适配器
    public class ViewHolder{
        public TextView tv_desc;
        public TextView tv_res;
        public TextView tv_time;
        public ImageView iv_content;

    }
    public void LoadImage(final ImageView iv,final String url){
        Log.d(TAG, "loadImage() returned: url=" + url);
        iv.setTag(url);
        BitmapRequest br=new BitmapRequest(url, Request.Method.GET, new Request.Callback<Bitmap>() {
            @Override
            public void onEror(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResonse(final Bitmap response) {
                if(iv!=null&&response!=null&&((String)iv.getTag()).equals(url));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageBitmap(response);
                        System.out.println("response=============LoadImage");
                    }
                });
            }
        });
        HttpHelper.adddRequest(br);
        System.out.println("LoadImage===============InfoListAdapter");
    }
}
