package com.xing.app.xframework;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActivityBase implements View.OnClickListener{

    private Button button,button2;

    private TextView textView;

    private HeartProgress heartProgress;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void findViews() {
        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        textView = (TextView)findViewById(R.id.textview);
        heartProgress = (HeartProgress)findViewById(R.id.testView);

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void init() {

        setLeftVisible(View.GONE);

        setRightVisible(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:

//                SlideDialog slideDialog = new SlideDialog(getContext());
//
//                slideDialog.setContentView(SlideDialog.PROGRESS);
//
//                slideDialog.setTitles("进度条对话框");
//
//                slideDialog.setMaxProgress(100);
//                slideDialog.setProgress(87);
//
//                slideDialog.show();

                heartProgress.setProgress(i++);

                heartProgress.setSize(400);


                break;
            case R.id.button2:

                final SlideDialog slideDialog1 = new SlideDialog(getContext());

                slideDialog1.setContentView(SlideDialog.DEFAULT);

                slideDialog1.setTitles("默认的对话框");
                slideDialog1.setMessage("默认情况下弹出的对话框");

                slideDialog1.setCenterButtonVisible(View.GONE);

                slideDialog1.setRightButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slideDialog1.dismiss();
                    }
                });

                slideDialog1.show();

                break;
        }
    }
}

