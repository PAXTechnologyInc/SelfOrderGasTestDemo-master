package com.pax.order.settings;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.pax.order.BluetoothActivity;
import com.pax.order.BuildConfig;
import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.adapter.DeviceAdapter;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.db.TransData;
import com.pax.order.menu.MenuActivity;
import com.pax.order.payment.PaymentActivity;
import com.pax.order.util.CustomEventBus;
import com.pax.order.util.DialogClass;
import com.pax.order.util.EventBusResponseResult;
import com.paxsz.easylink.api.EasyLinkSdkManager;
import com.paxsz.easylink.api.ResponseCode;
import com.paxsz.easylink.device.DeviceInfo;
import com.paxsz.easylink.listener.SearchDeviceListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

import butterknife.Bind;

import static android.content.Context.MODE_PRIVATE;

public class BluetoothSettingFragment extends SettingsBaseFragment {

    private ProgressDialog progressDialog;
    private DeviceAdapter mDeviceAdapter;
    private ImageView img_loading;
    private TransData transData = new TransData();
    private String sanStatus = "start_scan";

    @Bind(R.id.bluetooth_list)
    ListView bluetoothList;

    private GlobalVariable global_var;

    public static boolean isConnect = false;
    public static boolean isDiscoverComplete = true;

    //the list of the bluetooth devices' name
    static ArrayList<String> deviceNameArray = new ArrayList<>();

    //the list of the bluetooth devices' address
    static ArrayList<String> deviceAddressArray = new ArrayList<>();

    private EasyLinkSdkManager easyLink;
    private EventBusResponseResult eventBusResponseResult;

    SpotsDialog progress;


    private static int currentPos = 0;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;


    String currentMac = "";
    String currentName = "";


    ListView listview2;
    SettingsParamActivity mActivity;

    //@Override
    protected void setupSimplePreferencesScreen() {
        System.out.println("value is ------- value");

        //listView = findPreference(getString(R.string.bluetooth_setting_list));
       // preferenceScreen = getPreferenceScreen();
       // preferenceScreen.bind(listview2);
        /*
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        CharSequence[] entries = new CharSequence[1];
        CharSequence[] entryValues = new CharSequence[1];
        entries[0] = "No Devices";
        entryValues[0] = "";
        if(pairedDevices.size() > 0){
            entries = new CharSequence[pairedDevices.size()];
            entryValues = new CharSequence[pairedDevices.size()];
            int i=0;
            for(BluetoothDevice device : pairedDevices){
                entries[i] = device.getName();
                entryValues[i] = device.getAddress();
                i++;
            }
        }
        listPreference.setEntries(entries);
        listPreference.setEntryValues(entryValues);
        */
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        global_var = ((GlobalVariable) getActivity().getApplicationContext());
        System.out.println("BluetoothSettingFragment Activity: " + getActivity());
        System.out.println("BluetoothSettingFragment Created");
        System.out.println(listview2 == null);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }
//        initBlueToothView();
//        setScanRule();
//
//
//        startScan();
//        easyLinkConnect();

        super.onCreate(savedInstanceState);




    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = (SettingsParamActivity) getActivity();
        mActivity.setHeaderTitle(getResources().getString(R.string.bluetooth_setting));

        initBlueToothView();
        System.out.println("InitBlueToothView");
        setScanRule();
        System.out.println("setscanrule");

        initBlue();
        System.out.println("InitBlue");
        startScan();
        System.out.println("startscan");
        easyLinkConnect();
        System.out.println("easyLinkConnect");
    }

    // this is init for bluetooth
    public void initBlue() {
//        checkPermission();
        eventBusResponseResult = new EventBusResponseResult();
        easyLink = EasyLinkSdkManager.getInstance(getActivity());
    }

    public void easyLinkConnect(){
        updateBluetoothList();

        System.out.println("deviceNameArray:" + deviceNameArray.toString());

        isDiscoverComplete = false;

//         progress = ProgressDialog.show(getActivity(), "Searching device ...",  "", true);

        progress = new SpotsDialog(getActivity(), R.style.BTSearch);
        progress.show();

        easyLink.searchDevices(true, new MyDeviceSearchListener(), 10000);

        // delay the process dialog in the Bluetooth search
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (progress != null) {
                    progress.dismiss();
                    progress = null;
                }
            }
        }, 1000);
    }
    public void updateBluetoothList() {
        final BluetoothSettingFragment.BluetoothAdapter adapter = new BluetoothSettingFragment.BluetoothAdapter(getActivity().getBaseContext());
        System.out.println("listview2: " + listview2);
        System.out.println("Adaptor: " + adapter);
        listview2.setAdapter(adapter);
        listview2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    public void connectDevice() {


        progress = new SpotsDialog(getActivity(), R.style.BTConn);
        progress.show();

        new Thread(new Runnable() {

            @Override
            public void run() {
                isConnect = false;
                DeviceInfo deviceInfo = new DeviceInfo(DeviceInfo.CommType.valueOf("BLUETOOTH_BLE"), currentName, currentMac);
                //  deviceInfo.name = Name;
                // deviceInfo.identifier = Mac;
                System.out.println("easylink before connect:" +deviceInfo.getDeviceName());
                int ret = easyLink.connect(deviceInfo);
                global_var.setEasylink(easyLink);
                Log.i("device name:", deviceInfo.getDeviceName());
                Log.i("device identifier:", deviceInfo.getIdentifier());
                Log.i("device ret kkk:", String.valueOf(ret));
                if (ret == ResponseCode.EL_RET_OK || String.valueOf(ret).contains("1001")) {
                    CustomEventBus.doEvent(EventBusResponseResult.ResultEvent.SUCCESS);
                    threadMsg("SUCCESS");

                    //save BT device info
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BTdevice", MODE_PRIVATE);
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
                        if (aResponse.equals("SUCCESS")) {
                            // toast MESSAGE
                            if(!getActivity().isFinishing()){
                                DialogClass.showPromptDialog(getActivity(),
                                        "Bluetooth Connection Success");

                            }

                            // todo change back delete this line of code
                            BluetoothActivity.isConnect = true;
                        } else {
                            if(!getActivity().isFinishing()){
                                DialogClass.showPromptDialog(getActivity(),
                                        "Bluetooth Connection Fail, Please Try Again Later");
                            }

                        }
                        progress.dismiss();
//                        Intent intent = new Intent(getActivity(), SettingsParamActivity.class);
//                        startActivity(intent);
                    }

                }
            };

        }).start();
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
//            scanImage.setImageResource(R.drawable.btn_search_p);
//            scanText.setText(R.string.scan);
//            scanLinear.setEnabled(true);
            Log.d(">>>", "call discoverComplete");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //global_var = ((GlobalVariable) getApplicationContext());
        System.out.println("OnCreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        View btView = inflater.inflate(R.layout.setting_bluetooth_list, container, false);
        System.out.println("Before View Created");
        listview2 = (ListView) btView.findViewById(R.id.bluetooth_list);
        listview2.setAdapter(mDeviceAdapter);
        System.out.println("After List adapter set.");
        return btView;
    }
    /*
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View contentView = View.inflate(getActivity(), R.layout.settings_version_layout, null);
            TextView mTvVersion = (TextView) contentView.findViewById(R.id.tv_version);
            //    mTvVersion.setText("VER：" + FinancialApplication.getVersion());
            mTvVersion.setText("VER：" + BuildConfig.VERSION_NAME);

            TextView mTvBuildTime = (TextView) contentView.findViewById(R.id.tv_updatetime);
            mTvBuildTime.setText(BuildConfig.BUILD_TIME);

            return contentView;
        }
    */
    //@Override
    protected int getViewId() {
       //return R.xml.setting_bluetooth;
        // for demo
        return R.xml.setting_bluetooth_demo;
    }


    private void initBlueToothView(){

        BleManager.getInstance().init(FinancialApplication.getApp());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);

        if(!BleManager.getInstance().isBlueEnable()){
            BleManager.getInstance().enableBluetooth();
        }
        progressDialog = new ProgressDialog(getActivity());

        System.out.println("The initBlue tooth view");
        mDeviceAdapter = new DeviceAdapter(getActivity());
        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                    transData.setBleDevice(bleDevice);
                    // SET GLOBAL
                    global_var.setBleDevice(bleDevice);
                    System.out.println("Bluetooth Device Set");
                }
            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().disconnect(bleDevice);
                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                // todo copy this into

//                transData.setBleDevice(bleDevice);
//                if (BleManager.getInstance().isConnected(bleDevice)) {
//                    Intent intent = new Intent(MainActivity.this, CardActivity.class);
//                    intent.putExtra(OperationActivity.KEY_DATA, bleDevice);
//                    startActivity(intent);
//                }
            }
        });
       // listview2.setAdapter(mDeviceAdapter);
        System.out.println("after init list view2");

    }
    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
//                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
//                btn_scan.setText(getString(R.string.start_scan));
                progressDialog.dismiss();
                //Toast.makeText(this, getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.addDeviceFirst(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                //global_var.setBleDevice(bleDevice);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (isActiveDisConnected) {
                    //Toast.makeText(MainActivity.this, getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(MainActivity.this, getString(R.string.disconnected), Toast.LENGTH_LONG).show();
                    //ObserverManager.getInstance().notifyObserver(bleDevice);
                }
                //global_var.setBleDevice(null);


            }
        });
    }
    private void setScanRule() {
        String[] uuids;
//        String str_uuid = "49535343-1E4D-4BD9-BA61-23C647249616";
        String str_uuid= "";
        if (TextUtils.isEmpty(str_uuid)) {
            uuids = null;
        } else {
            uuids = str_uuid.split(",");
        }
        UUID[] serviceUuids = null;
        if (uuids != null && uuids.length > 0) {
            serviceUuids = new UUID[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                String name = uuids[i];
                String[] components = name.split("-");
                if (components.length != 5) {
                    serviceUuids[i] = null;
                } else {
                    serviceUuids[i] = UUID.fromString(uuids[i]);
                }
            }
        }

        String[] names;
        String str_name = "";
        if (TextUtils.isEmpty(str_name)) {
            names = null;
        } else {
            names = str_name.split(",");
        }

        String mac =  "";  //"00:81:F9:9E:D9:5A";

        boolean isAutoConnect = true;

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(isAutoConnect)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }
    private void startScan() {
        BleManager.getInstance().enableBluetooth();
        System.out.println("The scan stauts:" + sanStatus);
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
//                img_loading.startAnimation(operatingAnim);
//                img_loading.setVisibility(View.VISIBLE);
                sanStatus = "start_scan";
                System.out.println("The onScanStarted:");
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
                System.out.println("The onLeScan:");
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                System.out.println("The bleDevice:" + bleDevice.getName());
                System.out.println("The bleDevice:" + bleDevice.getKey());
                System.out.println("The bleDevice:" + bleDevice.getMac());
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
//                img_loading.clearAnimation();
//                img_loading.setVisibility(View.INVISIBLE);
                sanStatus = "start_scan";
                System.out.println("The onScanFinished:");
                System.out.println("The onScanFinished:" + mDeviceAdapter.getCount());


            }
        });
    }
}