package com.xing.work.xframework;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ActivityRoundImage extends ActivityBase {

    private ImageView imageView, blur, round, border, multi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riv);
    }

    @Override
    public void findViews() {
        imageView = findViewById(R.id.riv);
        blur = findViewById(R.id.blur);
        round = findViewById(R.id.round);
        border = findViewById(R.id.border);
        multi = findViewById(R.id.multi);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.testsss);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        drawable.setCornerRadius(300f);
        imageView.setImageDrawable(drawable);


        /**
         * 实现模糊效果
         * {@link BlurTransformation(radius,sampling)}
         * radius 模糊半径
         * sampling 缩小图片再模糊，该值越大则模糊运算所需要计算量越小，速度也越快
         */
        Glide.with(this)
                .load(R.drawable.testsss)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(3, 5)))
                .into(blur);

        /**
         * 圆角图片
         * {@link RoundedCornersTransformation(radius,margin,cornerType)}
         * radius 圆角半径
         * margin 边距
         * cornerType 指定角圆角化
         */
        Glide.with(this)
                .load(R.drawable.testsss)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(100,30, RoundedCornersTransformation.CornerType.TOP_LEFT)))
                .into(round);

        /**
         * 圆形加边框 只能运用于长宽相等的View
         * {@link CropCircleWithBorderTransformation(size,color)}
         * size 边框大小
         * color 颜色
         */
        Glide.with(this)
                .load(R.drawable.testsss)
                .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(5,0xfff0f000)))
                .into(border);

        /**
         * 多种效果叠加的特效
         */
        MultiTransformation<Bitmap> multiTrans = new MultiTransformation<>(new BlurTransformation(25),new RoundedCornersTransformation(300,10));
        Glide.with(this)
                .load(R.drawable.testsss)
                .apply(RequestOptions.bitmapTransform(multiTrans))
                .into(multi);

    }

    @Override
    public void init() {

    }
}
