package com.xing.work.xframework;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxing on 16/10/8.
 */

public class ActivityXLV extends ActivityBase implements XListView.OnPullDownListener,AdapterView.OnItemLongClickListener,XListView.OnPullUpListener{

    private static final String TAG = "ActivityXLV";

    private XListView xListView;

    private TextView textView,textView1;

    private TestAdapter testAdapter;

    private List list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlv);

        xListView.setHeader(R.layout.header_listview);//给listview设置一个头部文件
        xListView.setOnPullDownListener(this);//设置下拉监听
        textView = (TextView) xListView.getHeader().findViewById(R.id.text2);

        xListView.setFooter(R.layout.header_listview);
        xListView.setOnPullUpListener(this);
        textView1 = (TextView) xListView.getFooter().findViewById(R.id.text2);
        //状态测试用的textview

        list.add("你吃药了吗");
        list.add("没吃内");
        list.add("哦!回家以后多吃点呀");
        list.add("你吃药了吗");
        list.add("没吃内");
        list.add("哦!回家以后多吃点呀");
        list.add("你吃药了吗");
        list.add("没吃内");
        list.add("哦!回家以后多吃点呀");
        list.add("你吃药了吗");
        list.add("没吃内");
        list.add("哦!回家以后多吃点呀");
        list.add("你吃药了吗");
        list.add("没吃内");
        list.add("哦!回家以后多吃点呀");

        testAdapter.setList(list);
        //这里有个很尴尬的问题,我本来设想通过RefreshAdapter接口
        //把添加数据修改数据的功能全部托管给listview
        //但是发现,实现托管之前必须先完成setAdapter方法,而如果此时setAdapter就必然导致adapter中数据为空而报错
        //反之,如果先setList,也会使xlistview中的adapter实例为空导致报错
        //2016-10-18更新说明:已经将setList方法从接口中移除,对adapter数据的填充由使用者自己设定填充方法
        xListView.setAdapter(testAdapter);
        xListView.setOnItemLongClickListener(this);

    }

    @Override
    public void findViews() {
        xListView = (XListView)findViewById(R.id.xlistview);
        testAdapter = new TestAdapter(getContext());
        list = new ArrayList();
    }

    @Override
    public void init() {
        setTitle("测试XListView");

        setRightVisible(View.GONE);
        setLeftText("返回");
        setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onPullDownDone(View header) {
        Log.d(TAG,"默认状态,onPullDownDone()");
    }

    @Override
    public void onPullDownRefreshing(View header) {
        Log.d(TAG,"刷新中,onPullDownRefreshing()");
        textView.setText("正在玩命加载数据...");

        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                testAdapter.addItemInHead("加到头部");
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                testAdapter.notifyDataSetChanged();
                xListView.onPullDownRefreshComplete();
            }
        }.execute(null, null, null);

    }

    @Override
    public void onPullDownReleaseToRefresh(View Header) {
        Log.d(TAG,"松开可以刷新,onPullDownReleaseToRefresh()");
        textView.setText("松开就能开始加载数据了...");
    }

    @Override
    public void onPullDownToRefresh(View Header) {
        Log.d(TAG,"继续拉才能刷新,onPullDownToRefresh()");
        textView.setText("请用力往下拉...");
    }

    @Override
    public void onPullUpDone(View footer) {

    }

    @Override
    public void onPullUpRefreshing(View footer) {
        textView1.setText("正在玩命加载数据...");

        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                testAdapter.addItemInFoot("加到尾部");
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                testAdapter.notifyDataSetChanged();
                xListView.onPullUpRefreshComplete();
            }
        }.execute(null, null, null);
    }

    @Override
    public void onPullUpReleaseToRefresh(View footer) {
        textView1.setText("松开即可刷新");
    }

    @Override
    public void onPullUpToRefresh(View footer) {
        textView1.setText("继续拉");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if (xListView.getHeader()!=null){
            position--;
            //listview如果添加了一个headerView,那么本方法的position的0值永远指向header
            //相应的,其他的子item的position值就会向后排,也就是加一
            //所以在这种情况下,如果需要position参数指向我们所需的子item,就必须将position值减一
        }

        xListView.removeItem(position);
        testAdapter.notifyDataSetChanged();

        return false;
    }
}
