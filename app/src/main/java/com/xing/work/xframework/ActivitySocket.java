package com.xing.work.xframework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xing.app.myutils.Utils.LogUtil;
import com.xing.app.myutils.Utils.StringUtil;
import com.xing.app.myutils.Utils.ThreadUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ActivitySocket extends ActivityBase implements View.OnClickListener {

    EditText mEditText;
    Button mButton;
    TextView mTextView;

    Socket mSocket;

    boolean isClose = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
    }

    @Override
    public void findViews() {
        mEditText = findViewById(R.id.edit);
        mButton = findViewById(R.id.commit);
        mTextView = findViewById(R.id.receiver);
    }

    @Override
    public void init() {
        mButton.setOnClickListener(this);

        connectSocket();
        receiverSocket();
    }

    public void connectSocket(){
        ThreadUtil.runOnChildThread(new Runnable() {
            @Override
            public void run() {
                if (mSocket == null){
                    try {
                        mSocket = new Socket("192.168.3.89",10012);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (mSocket.isClosed() || !mSocket.isConnected()){
                    try {
                        mSocket.close();
                        mSocket = new Socket("192.168.3.89",10012);
                    } catch (Exception e) {
                        e.printStackTrace();
                        resetSocket();
                    }
                }
            }
        });
    }

    public void receiverSocket(){
        ThreadUtil.runOnChildThread(new Runnable() {
            @Override
            public void run() {
                while (!isClose){
                    InputStreamReader reader = null;
                    try {
                        if (!StringUtil.isEmpty(mEditText.getText().toString())
                                && mSocket != null
                                && mSocket.isConnected()
                                && !mSocket.isClosed()){

                            reader = new InputStreamReader(mSocket.getInputStream());
                            char[] chars = new char[1536];
                            int readLen;
                            final StringBuilder bul = new StringBuilder();
                            while ( (readLen = reader.read(chars, 0, chars.length)) > 0) {
                                bul.append(new String(chars, 0, readLen));
                                if (bul.toString().endsWith("<end>")){
                                    break;
                                }
                            }

                            if (bul.toString().length() == 0){
                                continue;
                            }
                            LogUtil.d("读取socket成功-》"+bul);
                            ThreadUtil.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTextView.setText(bul.toString());
                                }
                            });

                        }
                        Thread.sleep(1000L);
                    } catch (Exception e){
                        e.printStackTrace();
                        resetSocket();
                    } finally {
//                        if (reader != null){
//                            try {
////                                reader.close();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
////                        resetSocket();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!StringUtil.isEmpty(mEditText.getText().toString())
                && mSocket != null
                && mSocket.isConnected()
                && !mSocket.isClosed()){

            ThreadUtil.runOnChildThread(new Runnable() {
                @Override
                public void run() {
                    OutputStreamWriter writer = null;
                    try{
                        writer = new OutputStreamWriter(mSocket.getOutputStream(), StandardCharsets.UTF_8);

                        writer.write(mEditText.getText().toString()+"\n");
                        writer.flush();
                        LogUtil.d("写入socket成功-》"+mEditText.getText().toString()+"\n");
                    } catch (Exception e){
                        e.printStackTrace();
                        resetSocket();
                    } finally {
//                        try {
//                            if (writer != null){
//                                writer.close();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        resetSocket();
                    }
                }
            });
        }
    }

    public void resetSocket(){
        connectSocket();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isClose = true;
    }
}
