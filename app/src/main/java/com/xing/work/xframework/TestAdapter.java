package com.xing.work.xframework;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wangxing on 16/10/8.
 * 本类用来测试XListView
 */
public class TestAdapter extends BaseAdapter implements RefreshAdapter{

    private Context context;

    private List list;

    public TestAdapter(Context context){
        this.context = context;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public void addItemInHead(Object object) {
        list.add(0,object);
    }

    @Override
    public void addItemInFoot(Object object) {
        list.add(object);
    }

    @Override
    public void removeItem(int position) {
        if (position < list.size()){
            list.remove(position);
        }
    }


    @Override
    public int getCount() {
        if (list.size()!=0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        ViewHolder viewHolder = new ViewHolder();
//
//        if (convertView == null){
//            convertView = LayoutInflater.from(context).inflate(R.layout.test_item,parent,false);
//            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
//            convertView.setTag(viewHolder);
//        }else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        viewHolder.textView.setText("测试数据   "+ list.get(position));

        if (convertView == null){
            convertView = new SideslipItem(context);
        }

        return convertView;
    }

    public final static class ViewHolder{
        public TextView textView;
    }
}
