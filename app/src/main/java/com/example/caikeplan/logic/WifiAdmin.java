package com.example.caikeplan.logic;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import static com.umeng.socialize.utils.Log.TAG;

/**
 * Created by dell on 2017/3/7.
 */

public class WifiAdmin {

    private static final String TAG = WifiAdmin.class.getSimpleName();
    // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID;
    }

    //定义WifiManager对象
    private WifiManager mWifiManager;
    //定义WifiInfo对象
    private WifiInfo mWifiInfo;
    //扫描网络连接列表
    private List<ScanResult> mWifiList;
    //网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;
    //定义一个WifiLock
    private WifiManager.WifiLock mWifiLock;

    public WifiAdmin(Context context){
        //取得WifiManager对象
        mWifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        //取得WifiInfo对象
        mWifiInfo=mWifiManager.getConnectionInfo();
    }

    //打开WIFI
    public void openWifi(){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }
    }

    //关闭WIFI
    public void closeWifi(){
        if(mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }
    }

    //检查当前WIFI状态
    public int checkState(){
        return mWifiManager.getWifiState();
    }

    //锁定WifiLock
    public void acquireWifiLock(){
        mWifiLock.acquire();
    }

    //解锁WifiLock
    public void releaseWifiLock(){
        //判断是否锁定
        if(mWifiLock.isHeld()){
            mWifiLock.acquire();
        }
    }

    //创建一个WifiLock
    public void createWifiLock(){
        mWifiLock= mWifiManager.createWifiLock("caike");
    }

    //得到配置好的网络
    public List<WifiConfiguration> getConfiguration(){
        return mWifiConfiguration;
    }

    // 提供一个外部接口，传入要连接的无线网
    public void connect(String ssid, String password, WifiCipherType type) {
        Thread thread = new Thread(new ConnectRunnable(ssid, password, type));
        thread.start();
    }

    //指定配置好的网络进行连接
    public void connectionConfiguration(int index){
        //索引大于配置好的网络索引返回
        if(index > mWifiConfiguration.size()){
            return;
        }
        //连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,true);
    }

    public void startScan(){
        mWifiManager.startScan();
        //得到扫描后的结果
        mWifiList=mWifiManager.getScanResults();
        //得到配置好的网络连接
        mWifiConfiguration=mWifiManager.getConfiguredNetworks();
    }

    //得到网络列表
    public List<ScanResult> getWifiList(){
        return mWifiList;
    }

    //查看扫描结果
    public StringBuilder lookUpScan(){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<mWifiList.size();i++){
            stringBuilder.append("index_"+new Integer(i+1).toString()+":");
            //将ScanResult信息转换成一个字符串
            //其中包括:BASSID,SSID,capabilities,frequency,level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    //得到Mac地址
    public String getMacAddress(){
        return (mWifiInfo==null) ? "NULL":mWifiInfo.getMacAddress();
    }

    //得到接入点的BASSID
    public String getBASSID(){
        return (mWifiInfo==null) ? "NULL":mWifiInfo.getBSSID();
    }

    //得到IP地址
    public int getIPAddress(){
        return (mWifiInfo==null) ? 0:mWifiInfo.getIpAddress();
    }

    //得到连接的ID
    public int getNetWorkId(){
        return (mWifiInfo==null) ? 0:mWifiInfo.getNetworkId();
    }

    //得到WifiInfo的所有信息包
    public String getWifiInfo(){
        return (mWifiInfo==null) ? "NULL":mWifiInfo.toString();
    }

    //添加一个网络并连接
    public boolean addNetWork(WifiConfiguration wcg){
        int wcgID=mWifiManager.addNetwork(wcg);
        boolean b=mWifiManager.enableNetwork(wcgID,true);
        return b;
    }

    //断开指定ID的网络
    public void disconnectWifi(int netId){
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    public WifiConfiguration createWifiInfo(String SSID,String Password,WifiCipherType  Type){
        WifiConfiguration configuration=new WifiConfiguration();
        configuration.allowedAuthAlgorithms.clear();
        configuration.allowedGroupCiphers.clear();
        configuration.allowedKeyManagement.clear();
        configuration.allowedPairwiseCiphers.clear();
        configuration.allowedProtocols.clear();
        configuration.SSID="\""+SSID+"\"";
        WifiConfiguration tempConfig=this.IsExsits(SSID);
        if(tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if(Type ==  WifiCipherType.WIFICIPHER_NOPASS) //WIFICIPHER_NOPASS
        {
            configuration.wepKeys[0] = "";
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        }
        if(Type == WifiCipherType.WIFICIPHER_WEP) //WIFICIPHER_WEP
        {
            if (!TextUtils.isEmpty(Password)) {
                if (isHexWepKey(Password)) {
                    configuration.wepKeys[0] = Password;
                } else {
                    configuration.wepKeys[0] = "\"" + Password + "\"";
                }
            }
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        }
        if(Type == WifiCipherType.WIFICIPHER_WPA) //WIFICIPHER_WPA
        {
            configuration.preSharedKey = "\"" + Password + "\"";
            configuration.hiddenSSID = true;
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // 此处需要修改否则不能自动重联
            // configuration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            configuration.status = WifiConfiguration.Status.ENABLED;
        }
        return configuration;
    }



    private WifiConfiguration IsExsits(String SSID)
    {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs)
        {
            if (existingConfig.SSID.equals("\""+SSID+"\""))
            {
                return existingConfig;
            }
        }
        return null;
    }

    class ConnectRunnable implements Runnable {
        private String ssid;

        private String password;

        private WifiCipherType type;

        public ConnectRunnable(String ssid, String password, WifiCipherType type) {
            this.ssid = ssid;
            this.password = password;
            this.type = type;
        }

        @Override
        public void run() {
            // 打开wifi
            openWifi();
            // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
            // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
            while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                try {
                    // 为了避免程序一直while循环，让它睡个100毫秒检测……
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
            }
            WifiConfiguration wifiConfig = createWifiInfo(ssid, password, type);
//
            if (wifiConfig == null) {
                Log.d(TAG, "wifiConfig is null!");
                return;
            }

            WifiConfiguration tempConfig = IsExsits(ssid);

            if (tempConfig != null) {
                mWifiManager.removeNetwork(tempConfig.networkId);
            }

            int netID = mWifiManager.addNetwork(wifiConfig);
            boolean enabled = mWifiManager.enableNetwork(netID, true);
            Log.d(TAG, "enableNetwork status enable=" + enabled);
            boolean connected = mWifiManager.reconnect();
            Log.d(TAG, "enableNetwork connected=" + connected);
        }
    }

    private static boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();
        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }
        return isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f')) {
                return false;
            }
        }
        return true;
    }

}
