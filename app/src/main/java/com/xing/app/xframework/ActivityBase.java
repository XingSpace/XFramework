package com.xing.app.xframework;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by wangxing on 16/9/2.
 */
public abstract class ActivityBase extends FragmentActivity  {

    private Context context;

    private View root;

    private View topBar;

    private LinearLayout bottomBar,leftLayout,rightLayout;

    private TextView title,leftText,rightText;

    private ImageView leftImage,rightImage;

    private ProgressDialog progressDialog;

    public static int COLOR_GREEN = 0xff99cc00;

    public static int COLOR_RED = 0xffff4444;

    public static int COLOR_ORANGE = 0xffffbb33;

    public static int COLOR_VIOLET = 0xffaa66cc;

    public static int COLOR_BLUE = 0xff33b5e5;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        this.context = ActivityBase.this;

        root = findViewById(R.id.root);

        topBar = root.findViewById(R.id.top_bar);

        bottomBar = (LinearLayout) root.findViewById(R.id.bottom_bar);

        leftLayout = (LinearLayout) topBar.findViewById(R.id.left_layout);

        rightLayout = (LinearLayout) topBar.findViewById(R.id.right_layout);

        title = (TextView) topBar.findViewById(R.id.top_title);

        leftText = (TextView) leftLayout.findViewById(R.id.left_text);

        leftImage = (ImageView) leftLayout.findViewById(R.id.left_image);

        rightText = (TextView) rightLayout.findViewById(R.id.right_text);

        rightImage = (ImageView) rightLayout.findViewById(R.id.right_image);

        LinearLayout linearLayout = (LinearLayout)root.findViewById(R.id.main);

        linearLayout.addView(getLayoutInflater().inflate(layoutResID, null),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        findViews();
        init();


    }

    public abstract void findViews();

    public abstract void init();

    public Context getContext(){
        return context;
    }

    /**
     * @param s 设置本页标题
     */
    public void setTitle(String s){
        title.setText(s);
    }

    /**
     * @param s 设置标题左上角文字
     */
    public void setLeftText(String s){
        leftText.setText(s);
    }

    public void setRightText(String s){
        rightText.setText(s);
    }

    public void setLeftImage(int resId){
        leftImage.setImageResource(resId);
    }

    public void setRightImage(int resId){
        rightImage.setImageResource(resId);
    }

    public void setCenterOnClick(View.OnClickListener onClick){
        title.setOnClickListener(onClick);
    }

    public void setLeftOnClick(View.OnClickListener onClick){
        leftLayout.setOnClickListener(onClick);
    }

    public void setRightOnClick(View.OnClickListener onClick){
        rightLayout.setOnClickListener(onClick);
    }

    public void setLeftVisible(int visible){
        leftLayout.setVisibility(visible);
    }

    public void setRightVisible(int visible){
        rightLayout.setVisibility(visible);
    }

    public void setLeftImageVisible(int visible){
        leftImage.setVisibility(visible);
    }

    public void setRightImageVisible(int visible){
        rightImage.setVisibility(visible);
    }

    /**
     * @param resId 推荐传入一个高度为48dp的，宽度为MATCH_PARENT的布局，作为底部导航栏
     */
    public void setBottomBar(int resId){
        bottomBar.addView(getLayoutInflater().inflate(resId,null)
                , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * @param b 设置底部栏是否可见
     */
    public void setBottomBarVisible(boolean b){
        if (b){
            bottomBar.setVisibility(View.VISIBLE);
        }else {
            bottomBar.setVisibility(View.GONE);
        }
    }

    /**
     * @param b 设置顶部栏是否可见
     */
    public void setTopBarVisible(boolean b){
        if (b){
            topBar.setVisibility(View.VISIBLE);
        }else {
            topBar.setVisibility(View.GONE);
        }
    }

    /**
     * @param color 一个十六位数。。。设置颜色
     */
    public void setTopBarColor(int color){
        topBar.setBackgroundColor(color);
    }

    /**
     * @param color 虽然还没测试。。。但是我猜设置应该也没用，主要是bottomBar设置的时候是覆盖了原本的图层的（只能看到新传入的界面
     */
    public void setBottomBarColor(int color){
        bottomBar.setBackgroundColor(color);
    }

    /**
     * @param max 进度条最大值
     * @param title 标题栏
     * @param message 提示给用户的信息
     */
    public void showProgressDialog(int max,String title,String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(max);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
//        progressDialog.setCancelable(false);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.show();

    }

    /**
     * 关闭ProgressDialog的接口
     */
    public void progressDialogDismiss(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    /**
     * @param progress 用于更新ProgressDialog的进度条。。。必须在ui线程中调用本方法
     */
    public synchronized void setDialogProgress(int progress){
        progressDialog.onStart();//这个脑残的方法必须在setProgress之前调用，否则无效。。。（我真是日了狗了，我就说怎么测试了半天没反应
        progressDialog.setProgress(progress);
    }

}
