package com.pax.order;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.order.constant.GlobalVariable;
import com.pax.order.util.CustomEventBus;
import com.pax.order.util.DialogClass;
import com.pax.order.util.EventBusResponseResult;
import com.paxsz.easylink.api.EasyLinkSdkManager;
import com.paxsz.easylink.api.ResponseCode;
import com.paxsz.easylink.device.DeviceInfo;
import com.paxsz.easylink.listener.SearchDeviceListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

//import com.paxsz.easylink.protocol.DeviceInfo;

/**
 * Created by lixc
 * search bluetooth devices, display the list of devices, connect a device
 */
public class BluetoothActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.bluetoothList)
    ListView bluetoothList;
    @Bind(R.id.bottomScanImage)
    ImageView scanImage;
    @Bind(R.id.bottomScanText)
    TextView scanText;
    @Bind(R.id.scanLinear)
    LinearLayout scanLinear;

    public static boolean isConnect = false;
    public static boolean isDiscoverComplete = true;

    private static int currentPos = 0;
    private EasyLinkSdkManager easyLink;
    private EventBusResponseResult eventBusResponseResult;

    //the list of the bluetooth devices' name
    static ArrayList<String> deviceNameArray = new ArrayList<>();

    //the list of the bluetooth devices' address
    static ArrayList<String> deviceAddressArray = new ArrayList<>();

    // Global Variables
    GlobalVariable global_var;

    String currentMac = "";
    String currentName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);
        ButterKnife.bind(this);
        CustomEventBus.register(this);

        System.out.println("deviceNameArray before init:" + deviceNameArray.toString());
        init();
        System.out.println("deviceNameArray after init:" + deviceNameArray.toString());

        scanLinear.setOnClickListener(this);

        System.out.println("deviceNameArray after scan click in main:" + deviceNameArray.toString());
    }

    private void init() {
        global_var = ((GlobalVariable) getApplicationContext());
        checkPermission();
        eventBusResponseResult = new EventBusResponseResult();
        easyLink = EasyLinkSdkManager.getInstance(this);
        updateBluetoothList();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scanLinear) {
            isDiscoverComplete = false;
            easyLink.searchDevices(new MyDeviceSearchListener(), 10000);
            scanImage.setImageResource(R.drawable.btn_stop_n);
            scanText.setText(R.string.stop_scan);
            scanLinear.setEnabled(false);

            System.out.println("deviceNameArray after scan click:" + deviceNameArray.toString());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBusResponseResult.ResultEvent event) {
        switch (event) {
            case SUCCESS:
                isConnect = true;
                updateBluetoothList();
                break;
            case FAILED:
                Toast.makeText(this, eventBusResponseResult.getResultMsg(), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    public void updateBluetoothList() {
        final BluetoothAdapter adapter = new BluetoothAdapter(this);
        bluetoothList.setAdapter(adapter);
        bluetoothList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        bluetoothList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                currentPos = pos;
                currentName = parent.getItemAtPosition(pos).toString();
                System.out.println("currentName:" + currentName);
                currentMac = deviceAddressArray.get(pos);
                connectDevice();
            }
        });
    }

    private void connectDevice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isConnect = false;
                DeviceInfo deviceInfo = new DeviceInfo(DeviceInfo.CommType.valueOf("BLUETOOTH_BLE"), currentName, currentMac);
//               DeviceInfo deviceInfo = new DeviceInfo();
//               deviceInfo.name = currentName;
//               deviceInfo.identifier = currentMac;
                int ret = easyLink.connect(deviceInfo);
                global_var.setEasylink(easyLink);
                Log.i("device name:",deviceInfo.getDeviceName());
                Log.i("device identifier:", deviceInfo.getIdentifier());
                Log.i("device ret kkk:", String.valueOf(ret));
                if (ret == ResponseCode.EL_RET_OK) {
                    CustomEventBus.doEvent(EventBusResponseResult.ResultEvent.SUCCESS);
                    threadMsg("SUCCESS");

                    //save BT device info
                    SharedPreferences sharedPreferences = getSharedPreferences("BTdevice", MODE_PRIVATE);
                    //get SharedPreferences.Editor object ï¼Œand save data into object
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("BTname", deviceInfo.getDeviceName());
                    editor.putString("BTid", deviceInfo.getIdentifier());
                    //commit
                    editor.commit();
                } else {
                    eventBusResponseResult.setResultMsg(ResponseCode.getRespCodeMsg(ret));
                    CustomEventBus.doEvent(EventBusResponseResult.ResultEvent.FAILED);
                    threadMsg("FAIL");
                }
            }

            private void threadMsg(String msg) {

                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                }
            }

            // Define the Handler that receives messages from the thread and update the progress
            private final Handler handler = new Handler() {

                public void handleMessage(Message msg) {

                    String aResponse = msg.getData().getString("message");

                    if ((null != aResponse)) {
                        if (aResponse.equals("SUCCESS")){
                            // toast MESSAGE
                            if(!isFinishing()){
                                DialogClass.showPromptDialog(BluetoothActivity.this,
                                        "Bluetooth Connection Success");
                            }

                        }else{
                            if(!isFinishing()){
                                DialogClass.showPromptDialog(BluetoothActivity.this,
                                        "Bluetooth Connection Fail, Please Try Again Later");
                            }

                        }


                    }

                }
            };



        }).start();

    }

    private class MyDeviceSearchListener implements SearchDeviceListener {
        public void discoverOneDevice(DeviceInfo deviceInfo) {
            //avoid add the same device
            boolean flag = true;
            for (int i = 0; i < deviceNameArray.size(); i++) {
                if (deviceInfo.getDeviceName().equals(deviceNameArray.get(i))) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                deviceNameArray.add(deviceInfo.getDeviceName());
                deviceAddressArray.add(deviceInfo.getIdentifier());
                updateBluetoothList();
            }
        }

        public void discoverComplete() {
            isDiscoverComplete = true;
            scanImage.setImageResource(R.drawable.btn_search_p);
            scanText.setText(R.string.scan);
            scanLinear.setEnabled(true);
            Log.d(">>>", "call discoverComplete");
        }
    }

    private class BluetoothAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private BluetoothAdapter(Context context) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return deviceNameArray.size();
        }

        @Override
        public Object getItem(int position) {
            return deviceNameArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Log.e("TEST", "refresh once");

            convertView = inflater.inflate(R.layout.bluetooth_items, null, false);
            ImageView img = (ImageView) convertView.findViewById(R.id.image);// show image
            TextView tv = (TextView) convertView.findViewById(R.id.title);// display text
            tv.setText(getItem(position).toString());
            tv.setTextSize(20.0f);
            img.setImageResource(R.drawable.icn_bluetooth);

            // if the current line is the selected line, modify the display mode.
            if ((position == currentPos) && (isConnect)) {
                // modify the background color of the whole line
                convertView.setBackgroundColor(Color.LTGRAY);
                // modify the text color
                tv.setTextColor(Color.RED);
            }
            return convertView;
        }
    }

    private void checkPermission() {

        Log.d(TAG, "checkPermission");

        int currentapiVersion = Build.VERSION.SDK_INT;
        Log.d(TAG, "currentapiVersion=" + currentapiVersion);

        if (currentapiVersion >= Build.VERSION_CODES.M) {//API LEVEL 18
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "checkPermission - requestPermissions");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            } else {
                Log.d(TAG, "checkPermission - no need requestPermissions");
            }
        } else {
            Log.d(TAG, "checkPermission - SDK Vesion < 23");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO 请求权限成功
                Log.d(TAG, "requestPermissions success");
            } else {
                //TODO 提示权限已经被禁用
                Log.d(TAG, "requestPermissions fail");
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomEventBus.unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK) && (!isDiscoverComplete);
    }

}
