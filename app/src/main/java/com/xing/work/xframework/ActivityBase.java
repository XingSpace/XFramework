package com.xing.work.xframework;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

/**
 * Created by wangxing on 16/9/2.
 * 本类是一个抽象类
 * 它的主要作用在于自带了界面顶部的标题栏
 * 在实际使用过程中，就无需再去手动搭建一个标题栏（毕竟这样太麻烦不符合程序员偷懒的风格
 *
 * 使用方法：
 *         0.首先继承ActivityBase
 *         1.然后，重写onCreate方法（这一步操作和一般直接继承于Activity没有区别）
 *         2.setContentView（R.layout.xxx），这一步内还是平时一样，你传入一个你自己建好的布局就行了。
 *           这个被传入的布局会被加入到整个屏幕的中间位置（也就是activity_base.xml中id为main的位置）
 *         3.本类有两个抽象方法需要子类自己实现 findviews() 和 init() ，顾名思义。。。一个是用来专门绑定View的，
 *           另一个用于初始化View的各项基本设置和Activity的基本设置（当然，如果你喜欢反着用我也没意见，你开心），
 *           还有就是这个两个抽象方法无需在子类中手动调用，因为在ActivityBase中已经完成了调用（setContentView中调用的）
 *           所以子类只需要实现就好了，并且要知道，这两个抽象方法是在setContentView方法完成之后调用的
 *         4.以上几步完成之后，就实现了本类的基本使用，随后需要注意的就是本类自带的各种控件绑定
 *
 * 本类自带的标题栏提供了几个方法用于更改的标题栏的属性
 *
 * 比如：setTitle(String s);。。。用于设置标题栏的标题
 *      setLeftText(String s);。。。用于设置标题栏左边的文字，比如设置一个“返回”
 *      setLeftImage(int resID);。。。用于设置标题栏左边的图片，比如设置一个“<"这样的图标
 *      需要注意的是楼上两个方法都是用来设置左边的文本和图标，所以其功能呢应该是一样的，
 *      就像“返回”和"<"在日常中都是代表了离开本界面的意思
 *      所以文本和图标，公用了一个onClick监听事件，setLeftOnClick(View.OnClickListener);
 *      由于文本和图标作用一致，所以在实际开发过程中，为了界面的简洁性无需两个同时使用，你可以选择只使用图标或者只使用文本
 */
public abstract class ActivityBase extends FragmentActivity {

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

        bottomBar = root.findViewById(R.id.bottom_bar);

        leftLayout = topBar.findViewById(R.id.left_layout);

        rightLayout = topBar.findViewById(R.id.right_layout);

        title = topBar.findViewById(R.id.top_title);

        leftText = leftLayout.findViewById(R.id.left_text);

        leftImage = leftLayout.findViewById(R.id.left_image);

        rightText = rightLayout.findViewById(R.id.right_text);

        rightImage = rightLayout.findViewById(R.id.right_image);

        LinearLayout linearLayout = root.findViewById(R.id.main);

        linearLayout.addView(getLayoutInflater().inflate(layoutResID, null),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        findViews();
        init();


    }

    /**
     * 推荐大家用这个方法在子类中绑定控件
     */
    public abstract void findViews();

    /**
     * 用这个方法可以对控件进行一些设置，比如setOnClickListener（）等等
     */
    public abstract void init();

    /**
     * @return 返回一个本类的context实例，无需多说。。。
     */
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

    /**
     * @param s 设置右上角文本
     */
    public void setRightText(String s){
        rightText.setText(s);
    }

    /**
     * @param resId 传入一个资源id。。。用于标题左边的图标
     */
    public void setLeftImage(int resId){
        leftImage.setImageResource(resId);
    }

    /**
     * @param resId 传入一个资源id。。。用于标题右边的图标
     */
    public void setRightImage(int resId){
        rightImage.setImageResource(resId);
    }

    /**
     * @param onClick 给标题栏中间的文本添加单击监听（个人感觉。。。没什么卵用，但是我还是把功能实现了
     */
    public void setCenterOnClick(View.OnClickListener onClick){
        title.setOnClickListener(onClick);
    }

    /**
     * @param onClick 标题栏左边的监听，无论用户点的是文本还是图标，都会触发
     */
    public void setLeftOnClick(View.OnClickListener onClick){
        leftLayout.setOnClickListener(onClick);
    }

    /**
     * @param onClick 标题栏右边的监听，无论用户点的是文本还是图标，都会触发
     */
    public void setRightOnClick(View.OnClickListener onClick){
        rightLayout.setOnClickListener(onClick);
    }

    /**
     * @param visible 有的时候内，我们其实不需要左右两边有啥东西，所以我提供了这个方法，
     *                在你不需要它的时候，可以直接setLeftVisible（View.GONE）;
     */
    public void setLeftVisible(int visible){
        leftLayout.setVisibility(visible);
    }

    /**
     * @param visible 有的时候内，我们其实不需要左右两边有啥东西，所以我提供了这个方法，
     *                在你不需要它的时候，可以直接setLeftVisible（View.GONE）;
     */
    public void setRightVisible(int visible){
        rightLayout.setVisibility(visible);
    }

    /**
     * @param visible 这个和上面两个方法差不多意思吧。。。但是这里只针对左右两边的图标，而文本还在
     */
    public void setLeftImageVisible(int visible){
        leftImage.setVisibility(visible);
    }

    /**
     * @param visible 这个和上面两个方法差不多意思吧。。。但是这里只针对左右两边的图标，而文本还在
     */
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
     * @param b 设置底部栏是否可见，其实关于底部栏的问题。。。我还没做太多的优化。。。所以我只是提供方法
     *          你们如果硬要实现也是可行的。。。如果不需要，直接传一个false进来就好
     */
    public void setBottomBarVisible(boolean b){
        if (b){
            bottomBar.setVisibility(View.VISIBLE);
        }else {
            bottomBar.setVisibility(View.GONE);
        }
    }

    /**
     * @param b 设置顶部栏是否可见，和楼上意思一样。。。不需要的时候传个false
     */
    public void setTopBarVisible(boolean b){
        if (b){
            topBar.setVisibility(View.VISIBLE);
        }else {
            topBar.setVisibility(View.GONE);
        }
    }

    /**
     * @param color 一个十六位数。。。设置颜色（本类有提供几个默认的色彩值，如果要自己设置 格式就像这样 ： 0xffffffff
     *                                      0x表示16进制，前两位表示色彩透明度，后六位就是具体的RGB色值了
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
     * 调用以后。。。就会出现一个加载提示框。。。
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
        //楼上这句是用来 不轻易的狗带的 没错就是不会这么轻易的狗带
        progressDialog.setProgressNumberFormat(null);
        progressDialog.show();

    }

    /**
     * @param onCancelListener 加载框被干掉之后回调（也有可能是自己加载完成的时候回调
     */
    public void setProgressDialogCancelListener(DialogInterface.OnCancelListener onCancelListener){
        progressDialog.setOnCancelListener(onCancelListener);
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
     * @param b 传个false进来。。。就不会被back键消灭了
     */
    public void setProgressDialogCancel(boolean b){
        progressDialog.setCancelable(b);
    }

    /**
     * @param progress 用于更新ProgressDialog的进度条。。。
     */
    public synchronized void setDialogProgress(int progress){
        progressDialog.onStart();//这个脑残的方法必须在setProgress之前调用，否则无效。。。（我真是日了狗了，我就说怎么测试了半天没反应
        progressDialog.setProgress(progress);
        if (progress >= progressDialog.getMax()){
            progressDialogDismiss();
        }
    }

}
