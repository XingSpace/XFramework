package com.xing.app.xframework;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by wangxing on 16/9/4.
 */
public class SlideDialog extends Dialog{

    private Context mContext;

    private TextView textView,defaultText;

    private View root;

    private FrameLayout frameLayout,titleFrame;

    private LinearLayout linearLayout;

    private Button leftButton,centerButton,rightButton;

    private ProgressBar progressBar;//进度条模式
    private TextView percent;

    private OnProgress onProgress;

    public static final int PROGRESS = R.layout.slide_progress_dialog;

    public static final int DEFAULT = R.layout.slide_default_dialog;

    public SlideDialog(Context context){
        super(context,R.style.SlideDialog);
        this.mContext = context;
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        //先去除掉dialog默认的title 楼上这行

        window.getDecorView().setPadding(0, 0, 0, 0);//重置默认的边距
        LayoutParams lp = window.getAttributes();
        lp.windowAnimations = R.style.SlideDialogAnimation;//设置滑出、滑入动画
        lp.width = LayoutParams.WRAP_CONTENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.slide_dialog_layout);

        root = findViewById(R.id.back_ground);

        textView = (TextView) root.findViewById(R.id.testTitle);
        //设置该dialog的title

        frameLayout = (FrameLayout) root.findViewById(R.id.main);
        //要被添加View的。。。和主内容的地方

        titleFrame = (FrameLayout) root.findViewById(R.id.titles);
        //上方title

        linearLayout = (LinearLayout) root.findViewById(R.id.bottom);
        //下方Action bottom

        leftButton = (Button)root.findViewById(R.id.left_button);
        //默认将有三个按钮，这个代表左边那个

        centerButton = (Button)root.findViewById(R.id.center_button);
        //中间那个

        rightButton = (Button)root.findViewById(R.id.right_button);
        //右边那个

        switch (layoutResID){
            case DEFAULT:
                //默认模式
                frameLayout.addView(LayoutInflater.from(mContext).inflate(layoutResID,null)
                        ,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                defaultText = (TextView)frameLayout.findViewById(R.id.defaultText);
                break;

            case PROGRESS:
                //进度条模式
                frameLayout.addView(LayoutInflater.from(mContext).inflate(layoutResID,null)
                        ,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                progressBar = (ProgressBar)frameLayout.findViewById(R.id.progressBar2);
                percent = (TextView)frameLayout.findViewById(R.id.percent);
                break;

            default:
                //如果以上这些都不是。。。那就直接加载用户传过来的xml了
                frameLayout.addView(LayoutInflater.from(mContext).inflate(layoutResID,null)
                        ,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                break;
        }

//        defaultText.setText("");
//        frameLayout.addView(LayoutInflater.from(mContext).inflate(layoutResID,null)
//                ,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

    }

    /**
     * @param max 设置当前进度条的最大值
     */
    public void setMaxProgress(int max){
        progressBar.setMax(max);
    }

    /**
     * @param progress 设置进度条的当前值
     */
    public synchronized void setProgress(int progress){
        progressBar.setProgress(progress);
        float percent = (float) ((float)progressBar.getProgress()/(float)progressBar.getMax()*100.00);
        setProgressPercent(percent+"");

        if (onProgress!=null){
            onProgress.onProgressChange(progressBar.getMax(),progressBar.getProgress());

            if (progressBar.getMax() <= progressBar.getProgress()){
                onProgress.onProgressComplete(this);
            }
        }
    }

    public void setProgressPercent(String s){
        percent.setText(s+"%");
    }

    /**
     * @param onProgress 一个进度条状态的回调接口
     */
    public void setOnProgress(OnProgress onProgress){
        this.onProgress = onProgress;
    }

    public interface OnProgress{

        /**
         * 进度条发生改变时调用
         * @param max 最大值
         * @param progress 当前值
         */
        void onProgressChange(int max,int progress);

        /**
         * 完成的时候回调
         */
        void onProgressComplete(SlideDialog slideDialog);
    }

    /**
     * @param s 设置要提醒用户的内容（注意哈。。。只有使用了默认模式，才有这个功能
     */
    public void setMessage(String s){
        defaultText.setText(s);
    }

    /**
     * @param color 设置整个dialog的大背景（一般没什么卵用
     */
    public void setBackground(int color){
        root.setBackgroundColor(color);
    }

    /**
     * @param s 设置标题
     */
    public void setTitles(String s){
        textView.setText(s);
    }

    /**
     * @param color 设置标题字体的颜色
     */
    public void setTitleTextColor(int color){
        textView.setTextColor(color);
    }

    /**
     * @param color 设置标题栏的背景色
     */
    public void setTitleColor(int color){
        titleFrame.setBackgroundColor(color);
    }


    /**
     * @param color 设置最左边按钮的字体颜色
     */
    public void setLeftButtonTextColor(int color){
        leftButton.setTextColor(color);
    }

    /**
     * @param color 设置中间按钮的字体颜色
     */
    public void setCenterButtonTextColor(int color){ centerButton.setTextColor(color); }

    /**
     * @param color 设置最右边按钮的字体颜色
     */
    public void setRightButtonTextColor(int color){
        rightButton.setTextColor(color);
    }

    /**
     * @param color 设置最左边按钮的背景色
     */
    public void setLeftButtonColor(int color){
        leftButton.setBackgroundColor(color);
    }

    /**
     * @param color 设置中间按钮的背景色
     */
    public void setCenterButtonColor(int color) {
        centerButton.setBackgroundColor(color);
    }

    /**
     * @param color 设置最右边按钮的背景色
     */
    public void setRightButtonColor(int color){
        rightButton.setBackgroundColor(color);
    }

    /**
     * 给最左边的按钮设置一个文本并加一个点击监听
     * @param s
     * @param onClickListener
     */
    public void setLeftButton(String s, View.OnClickListener onClickListener){
        leftButton.setOnClickListener(onClickListener);
        leftButton.setText(s);
    }

    /**
     * 给中间的按钮设置文本，并添加一个监听
     * @param s
     * @param onClickListener
     */
    public void setCenterButton(String s,View.OnClickListener onClickListener){
        centerButton.setText(s);
        centerButton.setOnClickListener(onClickListener);
    }

    /**
     * 给中间的按钮设置一个文本并添加监听
     * @param s
     * @param onClickListener
     */
    public void setRightButton(String s, View.OnClickListener onClickListener){
        rightButton.setOnClickListener(onClickListener);
        rightButton.setText(s);
    }

    /**
     * 设置可见。。。比如，不需要某个按钮时可以调用，并传入一个View.GONE
     * @param visible
     */
    public void setLeftButtonVisible(int visible){
        leftButton.setVisibility(visible);
    }

    /**
     * 设置可见。。。比如，不需要某个按钮时可以调用，并传入一个View.GONE
     * @param visible
     */
    public void setCenterButtonVisible(int visible){
        centerButton.setVisibility(visible);
    }

    /**
     * 设置可见。。。比如，不需要某个按钮时可以调用，并传入一个View.GONE
     * @param visible
     */
    public void setRightButtonVisible(int visible){
        rightButton.setVisibility(visible);
    }


}
