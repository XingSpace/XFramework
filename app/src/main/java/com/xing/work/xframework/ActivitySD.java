package com.xing.work.xframework;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmtfp20.DmtFPFinger;
import com.dmtfp20sdk.DmtFPFingerIntf;
import com.huashi.otg.sdk.HSIDCardInfo;
import com.huashi.otg.sdk.HandlerMsg;
import com.huashi.otg.sdk.HsOtgApi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import print.Print;

/**
 * Created by wangxing on 16/10/8.
 */

public class ActivitySD extends ActivityBase implements View.OnClickListener {

    private Button button, button1, button2,button3,reader;
    private ImageView imageView;
    private TextView textView;
    private UsbManager usbManager;
    private UsbDevice device;
    HsOtgApi api;
    private static final String ACTION_USB_PERMISSION = "com.USB_PERMISSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sd);
    }

    @Override
    public void findViews() {
        button = (Button) findViewById(R.id.button1);
        button1 = (Button) findViewById(R.id.button2);
        button2 = (Button) findViewById(R.id.print);
        button3 = (Button) findViewById(R.id.fingger);
        imageView = (ImageView)findViewById(R.id.image);
        reader = (Button) findViewById(R.id.reader);
        textView = (TextView) findViewById(R.id.text);

        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        reader.setOnClickListener(this);
    }

    @Override
    public void init() {
        setTitle("测试SlideDialog");

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                SlideDialog slideDialog = new SlideDialog(getContext());

                slideDialog.setContentView(SlideDialog.PROGRESS);

                slideDialog.setTitles("进度条对话框");

                slideDialog.setMaxProgress(100);
                slideDialog.setProgress(87);

                slideDialog.show();
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

            case R.id.print:

                try {

                    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                    registerReceiver(receiver, filter);

                    usbManager = (UsbManager) getContext().getSystemService(Context.USB_SERVICE);
                    HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
                    Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
                    while(deviceIterator.hasNext()){
                        Log.d("test open->","有usb设备");
                        device = deviceIterator.next();
                        for (int i = 0; i<device.getInterfaceCount();i++){
                            UsbInterface intf = device.getInterface(i);
                            if (intf.getInterfaceClass() == 7){
                                if (!usbManager.hasPermission(device)){
                                    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                                    usbManager.requestPermission(device,mPermissionIntent);
                                }
                                printing(device);
                            }
                        }
                    }


                } catch (Exception e) {
                    Log.e("test",e+"");
                    e.printStackTrace();
                }

                break;

            case R.id.fingger:

                DmtFPFingerIntf dmtFPFinger = DmtFPFinger.getInstance(getContext());
                int isInt = dmtFPFinger.LIVESCAN_Init();

                if (isInt != 1){
                    Log.d("test finger init ->","error "+isInt);
                }

                isInt = dmtFPFinger.FP_Begin();
                if (isInt == 1){
                    byte[] data = new byte[90*1024];
                    isInt = dmtFPFinger.LIVESCAN_GetFPRawData(0,data);
                    if (isInt == 1){

                        byte[] score = {1};
                        int sco = dmtFPFinger.FP_GetQualityScore(data, score);

                        if (sco == 1){
                            Log.d("idreader score->",Arrays.toString(score));
                        }else {
                            Log.d("idreader score->",sco+"");
                        }

                        Log.d("test finger ->","success getFPRawData "+isInt);
                        Log.d("test getFPRawData ->", Arrays.toString(data));
                        Bitmap bitmap = DmtFPFinger.DmtBitmapFromRaw(data,256,360);
                        imageView.setImageBitmap(bitmap);
                    }
                }

                dmtFPFinger.LIVESCAN_Close();
                break;

            case R.id.reader:

                api = new HsOtgApi(handler,getContext());
                int ret = api.init();
                Log.d("test reader->","ret "+ret);
                if (ret == 1){

                    ret = api.NotAuthenticate(200,200);
                    if (ret != 1){
                        textView.setText("卡认证失败"+ret);
                        return;
                    }


                    HSIDCardInfo info = new HSIDCardInfo();
                    ret = api.ReadCard(info,200,1300);
                    if(ret == 1){
                        textView.setText(info.getPeopleName());
                        api.Unpack(Environment.getExternalStorageDirectory().getAbsolutePath()+"/wltlib",info.getwltdata());
                        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/wltlib/zp.bmp");
                        imageView.setImageBitmap(bitmap);
                        Log.d("url",Environment.getExternalStorageDirectory().getAbsolutePath()+"/wltlib");
                    }else {
                        textView.setText("读取身份证错误："+ret);
                    }
                }

                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == HandlerMsg.READ_SUCCESS){
                HSIDCardInfo info = (HSIDCardInfo) msg.obj;
                textView.setText(info.getPeopleName());
            }

        }
    };

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)){
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //user choose YES for your previously popup window asking for grant perssion for this usb device
                        if(null != usbDevice){
                            printing(usbDevice);
                        }
                    }
                }
            }
        }
    };

    private void printing(UsbDevice usbDevice){
        int isopen = 0;
        try {
            isopen = Print.PortOpen(getContext(),usbDevice);
            Log.d("test product id ->",""+usbDevice.getProductId());
            Log.d("test vendor id ->",""+usbDevice.getVendorId());


            Log.d("test",String.valueOf(Print.IsOpened())+"");
            if (!Print.IsOpened()){
                return;
            }

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.gsyhss);
            Print.PrintBitmap(bitmap,(byte) 0,(byte) 0,203);
            Print.PrintText("",0,1,0x00);//这里表示其实就表示换行了
////                    Print.PrintText("\n");
//                    Print.PrintText("您的票号：");
//                    Print.PrintText("2127",0,55,0xee);
////                    Print.PrintText("这是一个测试",0,55,0xee);
//                    Print.PrintText("",0,1,0x00);
////                    Print.PrintText("\n");
//                    Print.PrintText("您办理的业务：");
//                    Print.PrintText("综合业务",1,55,0xee);
//                    Print.PrintText("",0,1,0x00);
////                    Print.PrintText("\n");
//                    Print.PrintText("所有等待人数：");
//                    Print.PrintText("060",0,55,0xee);
//                    Print.PrintText("",0,1,0x00);
////                    Print.PrintText("\n");
//                    Print.PrintText("业务等待人数：");
//                    Print.PrintText("059",0,55,0xee);
//                    Print.PrintText("",0,1,0x00);
            Print.PrintText("\r\n\r\n");
            int cut = Print.CutPaper(1);
            Log.d("test cut->"," "+cut);


            Log.d("test","打印流程走完");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test open->",""+isopen);
    }
}
