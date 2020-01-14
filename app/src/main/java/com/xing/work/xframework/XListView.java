package com.xing.work.xframework;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by wangxing on 16/9/18.
 * 下拉刷新的宝宝
 */
public class XListView extends ListView implements AbsListView.OnScrollListener,RefreshAdapter {

    private Context context;

    private View header;//下拉的头文件（翻译成人话就是，就是一个下拉的提示那个view

    private View footer;//上拉时的底部View

    private boolean down_isRefreshable;//判断是否可以开始下拉刷新

    private boolean up_isRefreshable;//判断是否可以开始上拉加载

    private int down_state;//记录当前的下拉状态

    private int up_state;//记录上拉刷新时的状态

    private int startY;//手指的位置

    private int headerHeight;//头部下拉的高度

    private int footerHeight;

    private OnPullDownListener onPullDownListener;//监听下拉数据的监听,一共四个回调方法

    private OnPullUpListener onPullUpListener;//监听好上拉加载更多

    private RefreshAdapter refreshAdapter;//为管理adapter数据的接口(将adapter的功能集成到XListView中来

    private final static int RATIO = 3;//手指滑动和屏幕像素值的实际差值

    private final static int DOWN_RELEASE_To_REFRESH = 0;//已经达到可刷新状态，但手指仍未松开
    private final static int DOWN_PULL_To_REFRESH = 1;//正在下拉的状态，但是还不足刷新的阀值
    private final static int DOWN_REFRESHING = 2;//正在刷新的状态
    private final static int DOWN_DONE = 3;//默认状态

    private final static int UP_RELEASE_To_REFRESH = 0;//已经达到可刷新状态，但手指仍未松开
    private final static int UP_PULL_To_REFRESH = 1;//正在下拉的状态，但是还不足刷新的阀值
    private final static int UP_REFRESHING = 2;//正在刷新的状态
    private final static int UP_DONE = 3;//默认状态


    private final static String TAG = "XListView";

    /**
     * 构造方法从这里开始
     * @param context
     */
    public XListView(Context context) {
        super(context, null);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        down_state = DOWN_DONE;
        up_state = UP_DONE;
        setOnScrollListener(this);
    }

    public void setRefreshAdapter(RefreshAdapter refreshAdapter){
        this.refreshAdapter = refreshAdapter;
    }

    public void setHeader(int resID) {
        View view = LayoutInflater.from(context).inflate(resID, null);
        setHeader(view);
    }

    public void setHeader(View view) {
        header = view;
        measureView(header);//测量出这个View所需要的大小
        headerHeight = header.getMeasuredHeight();//将测量以后的高度作为依据
        header.setPadding(0, -headerHeight, 0, 0);//通过设置padding来隐藏header
        header.invalidate();//设置玩padding之后要重绘一次
        Log.d(TAG, headerHeight + " ");
        addHeaderView(header, null, false);//将header添加到listview
    }

    public View getHeader() {
        return this.header;
    }

    public void setFooter(int resID){
        View view = LayoutInflater.from(context).inflate(resID,null);
        setFooter(view);
    }

    public void setFooter(View view){
        footer = view;
        measureView(footer);
        footerHeight = footer.getMeasuredHeight();
        footer.setPadding(0,0,0,-footerHeight);
        footer.invalidate();
        addFooterView(footer,null,false);
    }

    public View getFooter(){
        return this.footer;
    }

    /**
     * 自己都看不太懂的代码。。。
     *
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidth = ViewGroup.getChildMeasureSpec(MeasureSpec.UNSPECIFIED, 0, params.width);
        //未指定 MeasureSpec.UNSPECIFIED表示不确定view的实际大小

        int temp = params.height;

        int childHeight;

        if (temp > 0) {
            childHeight = MeasureSpec.makeMeasureSpec(temp, MeasureSpec.EXACTLY);//最后一个参数表示：适合、匹配
        } else {
            childHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);//未指定
        }

        child.measure(childWidth, childHeight);
    }

    /**
     * @param adapter 考虑到下拉刷新功能,最好是有一个refresh功能的adapter进行适配
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof RefreshAdapter){
            //判断adapter是否实现了RefreshAdapter接口
            setRefreshAdapter((RefreshAdapter) adapter);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * 主要作用在于,判断是否已经抵达界面顶部,如果到达就可以开始下拉刷新操作
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        down_isRefreshable = firstVisibleItem == 0 & getHeader() != null;

        up_isRefreshable = firstVisibleItem + visibleItemCount >= totalItemCount - 1 & getFooter() != null;
        Log.d("测试上拉刷新",": "+up_isRefreshable);
    }

    private int initX,initY;
    private boolean isSideslip;//判断是否可以开始侧滑
    private boolean isSliding = false;//判断是否正在滑动过程中
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //未完。。。在此处设置好判断,看看是否启用侧滑(将touch事件交给子item
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                initX = (int) ev.getX();
                initY = (int) ev.getY();
                downPull(ev);
                upPull(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                if (Math.abs((float) initY-ev.getY()) < 10 ){
                    break;//防止手指一放上去就开始刷新操作
                }
                //正常上下滑动。。。
                if (down_isRefreshable) {
                    downPull(ev);
                }

                if (up_isRefreshable){
                    upPull(ev);
                    Log.d("测试上拉刷新"," 执行到了第一步");
                }
                break;
            case MotionEvent.ACTION_UP:
                if (down_isRefreshable){
                    downPull(ev);
                }
                if (up_isRefreshable){
                    upPull(ev);
                    Log.d("测试上拉刷新", "执行到了手指移开 + onTouchEvent");
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void upPull(MotionEvent ev){

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int)ev.getY();
                break;

            case MotionEvent.ACTION_UP:
                switch (up_state){
                    case UP_RELEASE_To_REFRESH:
                        up_state = UP_REFRESHING;
                        changeFooterViewState();
                        break;

                    case UP_PULL_To_REFRESH:
                        up_state = UP_DONE;
                        changeFooterViewState();
                        break;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d("测试上拉刷新"," 执行到了upPull的MOVE中来");
                int tempY = (int) ev.getY();
                int temp = (startY-tempY)/RATIO;
                Log.d("测试上拉刷新",temp+ " "+tempY+" "+startY);
                switch (up_state){

                    case UP_DONE:
                        Log.d("测试上拉刷新"," 执行到了upPull的DONE中来");
                        Log.d("测试上拉刷新",-footerHeight+" "+temp);
                        if (temp > 0){
                            up_state = UP_PULL_To_REFRESH;
                            changeFooterViewState();
                        }
                        break;

                    case UP_RELEASE_To_REFRESH:
                        footer.setPadding(0,0,0,-footerHeight +temp);
                        Log.d("测试上拉刷新"," 执行到了upPull的RELEASH To REFRESH中来");
                        Log.d("测试上拉刷新",-footerHeight+" "+temp);
                        if (temp < footerHeight && temp != 0){
                            up_state = UP_PULL_To_REFRESH;
                            changeFooterViewState();
                        }

                        if (temp <= 0){
                            up_state = UP_DONE;
                            changeFooterViewState();
                        }


                        break;

                    case UP_PULL_To_REFRESH:
                        footer.setPadding(0,0,0,-footerHeight +temp);
                        Log.d("测试上拉刷新"," 执行到了upPull的PULL To REFRESH中来");
                        Log.d("测试上拉刷新",-footerHeight+" "+temp);
                        if (temp > 0){
                            up_state = UP_PULL_To_REFRESH;
                            changeFooterViewState();
                        }else {
                            up_state = UP_DONE;
                            changeFooterViewState();
                        }

                        if (temp > footerHeight){
                            up_state = UP_RELEASE_To_REFRESH;
                            changeFooterViewState();
                        }

                        break;

                }

                break;
        }

    }

    /**
     * 下拉刷新过程中,对MotionEvent的主要控制工作
     * @param ev
     */
    private void downPull(MotionEvent ev){
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                Log.d("测试下拉刷新"," "+startY);
                break;

            case MotionEvent.ACTION_UP:

                switch (down_state) {
                    case DOWN_RELEASE_To_REFRESH:
                        down_state = DOWN_REFRESHING;
                        changeHeaderViewState();
                        break;

                    case DOWN_PULL_To_REFRESH:
                        down_state = DOWN_DONE;
                        changeHeaderViewState();
                        break;
                }

                break;

            case MotionEvent.ACTION_MOVE:

                int tempY = (int) ev.getY();
                int temp = (tempY - startY) / RATIO;
                switch (down_state) {

                    case DOWN_DONE:
                        if (temp> 0) {
                            down_state = DOWN_PULL_To_REFRESH;
                            changeHeaderViewState();
                        }
                        break;

                    case DOWN_RELEASE_To_REFRESH:
                        header.setPadding(0, temp - headerHeight, 0, 0);

                        if (temp < headerHeight && (tempY - startY) != 0) {
                            down_state = DOWN_PULL_To_REFRESH;
                            changeHeaderViewState();
                        }

                        if (temp <= 0) {
                            down_state = DOWN_DONE;
                            changeHeaderViewState();
                        }
                        break;

                    case DOWN_PULL_To_REFRESH:
                        header.setPadding(0, temp - headerHeight, 0, 0);

                        if (temp > 0) {
                            down_state = DOWN_PULL_To_REFRESH;
                            changeHeaderViewState();
                        } else {
                            down_state = DOWN_DONE;
                            changeHeaderViewState();
                        }

                        if (temp > headerHeight) {
                            down_state = DOWN_RELEASE_To_REFRESH;
                            changeHeaderViewState();
                        }

                        break;

                }
                break;

        }
    }

    /**
     * 下拉刷新时,实时变更listview的数据
     * 改变头部文件的状态
     */
    private void changeHeaderViewState() {
        switch (down_state) {
            case DOWN_DONE:
                header.setPadding(0, -1 * headerHeight, 0, 0);
//                textView.setText("下拉刷新");
                if (onPullDownListener != null) {
                    onPullDownListener.onPullDownDone(header);
                }
                break;

            case DOWN_REFRESHING:
                header.setPadding(0, 0, 0, 0);
//                textView.setText("正在刷新");

                if (onPullDownListener != null) {
                    onPullDownListener.onPullDownRefreshing(header);
                }
                break;

            case DOWN_RELEASE_To_REFRESH:
//                textView.setText("松开刷新");
                if (onPullDownListener != null) {
                    onPullDownListener.onPullDownReleaseToRefresh(header);
                }
                break;

            case DOWN_PULL_To_REFRESH:
//                textView.setText("下啦刷新");
                if (onPullDownListener != null) {
                    onPullDownListener.onPullDownToRefresh(header);
                }
                break;
        }
    }

    private void changeFooterViewState(){
        switch (up_state){

            case UP_DONE:
                footer.setPadding(0,0,0,-footerHeight);
                if (onPullUpListener!=null){
                    onPullUpListener.onPullUpDone(footer);
                }
                break;

            case UP_REFRESHING:
                footer.setPadding(0,0,0,0);
                if (onPullUpListener!=null){
                    onPullUpListener.onPullUpRefreshing(footer);
                }
                break;

            case UP_RELEASE_To_REFRESH:
                if (onPullUpListener!=null){
                    onPullUpListener.onPullUpReleaseToRefresh(footer);
                }
                break;

            case UP_PULL_To_REFRESH:
                if (onPullUpListener!=null){
                    onPullUpListener.onPullUpToRefresh(footer);
                }
                break;

        }
    }

    //刷新完成之后需要调用本方法使头文件归位
    public void onPullDownRefreshComplete() {
        down_state = DOWN_DONE;
        down_isRefreshable = false;
        changeHeaderViewState();
    }

    public void onPullUpRefreshComplete(){
        up_state = UP_DONE;
        up_isRefreshable = false;
        changeFooterViewState();
    }

    public void setOnPullDownListener(OnPullDownListener onPullDownListener) {
        this.onPullDownListener = onPullDownListener;
    }

    public void setOnPullUpListener(OnPullUpListener onPullUpListener){
        this.onPullUpListener = onPullUpListener;
    }

    @Override
    public void addItemInHead(Object object) {
        if (this.refreshAdapter != null) {
            refreshAdapter.addItemInHead(object);
        }
    }

    @Override
    public void addItemInFoot(Object object) {
        if (this.refreshAdapter != null) {
            refreshAdapter.addItemInFoot(object);
        }
    }

    @Override
    public void removeItem(int position) {
        if (this.refreshAdapter != null){
            refreshAdapter.removeItem(position);
        }
    }

    /**
     * 针对下拉刷新过程中的监听
     */
    public interface OnPullDownListener {
        void onPullDownDone(View header);//默认状态,就是看不到header的时候

        void onPullDownRefreshing(View header);//到这一步就表示已经可以才刷新数据了

        void onPullDownReleaseToRefresh(View Header);//到这里表示只要松开手就会开始刷新

        void onPullDownToRefresh(View Header);//这里表示松开手就会回到DONE默认状态,需要继续拉
    }

    /**
     * 针对上拉加载数据过程的监听
     */
    public interface OnPullUpListener{
        void onPullUpDone(View footer);//默认状态,就是看不到header的时候

        void onPullUpRefreshing(View footer);//到这一步就表示已经可以才刷新数据了

        void onPullUpReleaseToRefresh(View footer);//到这里表示只要松开手就会开始刷新

        void onPullUpToRefresh(View footer);//这里表示松开手就会回到DONE默认状态,需要继续拉
    }

}
