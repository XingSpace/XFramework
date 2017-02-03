package com.xing.work.xframework;

/**
 * Created by wangxing on 16/10/25.
 */
public interface RefreshAdapter {

    /**
     * 讲数据添加到列表顶部
     * @param object
     */
    void addItemInHead(Object object);//往列表的头部添加数据

    /**
     * 将数据添加到列表底部
     * @param object
     */
    void addItemInFoot(Object object);//往列表的底部添加数据

    /**
     * 根据指定的position移出数据
     * @param position
     */
    void removeItem(int position);
}
