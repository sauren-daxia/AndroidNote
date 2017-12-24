package com.dinghong.studyviewdemo1.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Jason Chen on 2017/11/2.
 */

public class WiFiConnectManager {
    private static WiFiConnectManager manager;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private Context con;

    private static final int WIFICIPHER_NOPASS = 0;     //无密码
    private static final int WIFICIPHER_WEP = 1;        //WEP类型加密
    private static final int WIFICIPHER_WPA = 2;        //WPA/WPA2类型加密

    public static WiFiConnectManager getInstance() {
        if (manager == null) {
            synchronized (WifiManager.class) {
                if (manager == null) {
                    manager = new WiFiConnectManager();
                }
            }
        }
        return manager;
    }

    private WiFiConnectManager() {

    }

    /**
     * 获取WIFI管理类
     * @return
     */
    public WifiManager getWifiManager() {
        return wifiManager;
    }

    /**
     * 初始化
     *
     * @param con
     */
    public static void init(Context con) {
        getInstance().con = con;
    }

    /**
     * 打开WiFi
     */
    private void openWiFi() {
        //判断是否打开WiFi，如果没有则打开
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public List<ScanResult> startScan() {
        wifiManager = (WifiManager) con.getApplicationContext().getSystemService(WIFI_SERVICE);
        //打开WiFi
        openWiFi();
        //获取当前链接详情
        wifiInfo = wifiManager.getConnectionInfo();
        Log.d("CHEN", "initWifi: wifiinfo" + wifiInfo.toString());
        //扫描附近的WiFi
        wifiManager.startScan();
        //获取扫描结果
        List<ScanResult> scanResults = wifiManager.getScanResults();
        //创建一个新的集合用于存储去重后的集合
        List<ScanResult> list = new ArrayList<>();
        //去重操作
        for (ScanResult s : scanResults) {
            Log.d("CHEN", "initWifi: wifi = " + s.toString() + "\n");
            if (!repeatWIFI(s, list)) {
                list.add(s);
            }
        }
        return list;
    }

    /**
     * 除去重复WiFi
     *
     * @param s
     * @param list
     * @return
     */
    private boolean repeatWIFI(ScanResult s, List<ScanResult> list) {
        if (s.SSID == null || s.SSID.length() == 0 || s.capabilities.contains("[IBSS]")) {
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).SSID.equals(s.SSID) && list.get(i).capabilities.equals(s.capabilities)) || list.get(i).BSSID.equals(s.BSSID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建WIFI对象
     *
     * @param s
     * @param password
     * @param type
     * @return
     */
    private WifiConfiguration createWifiConfig(ScanResult s, String password, int type) {
        //打开WiFi
        openWiFi();
        Log.d("CHEN", "createWifiConfig: ssid = " + s.SSID + " \t password = " + password + "\t " +
                "type = " +
                "" + type);
        WifiConfiguration config = null;

        if (wifiManager != null) {
            List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig == null) {
                    continue;
                }
                if (existingConfig.SSID.equals("\"" + s.SSID + "\"")) {
                    config = existingConfig;
                    break;
                }
            }
        }
        if (config == null) {
            config = new WifiConfiguration();
        }
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + s.SSID + "\"";

        WifiConfiguration tempConfig = isExist(s.SSID);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }

        if (type == WIFICIPHER_NOPASS) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else if (type == WIFICIPHER_WEP) {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    /**
     * 判断之前是否连接过该WIFI，用于连接过重置参数和未连接上重置参数
     *
     * @param ssid
     * @return
     */
    private WifiConfiguration isExist(String ssid) {
        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            if (config.SSID.equals("\"" + ssid + "\"")) {
                return config;
            }
        }
        return null;
    }

    /**
     * 连接WIFI
     *
     * @param s        需要连接的对象
     * @param password 密码
     */
    public void connectWifi(ScanResult s, String password) {
        //打开WiFi
        openWiFi();
        int type = 0;
        if (s.capabilities.contains("WPA")) {
            type = 2;
        } else if (s.capabilities.contains("WEP")) {
            type = 1;
        } else {
            type = 0;
        }
        addNetwork(createWifiConfig(s, password, type));
    }

    /**
     * 添加一个网络
     *
     * @param wcg
     */
    private void addNetwork(WifiConfiguration wcg) {
        //断开所有连接的WiFi
        for (WifiConfiguration c : wifiManager.getConfiguredNetworks()) {
            wifiManager.disableNetwork(c.networkId);
        }
        int netId = wcg.networkId;
        if (netId == -1) {
            netId = wifiManager.addNetwork(wcg);
        }
        boolean enableNetWork = wifiManager.enableNetwork(netId, true);     //连接此WIFI，是否断开其他WiFi
        Log.d("CHEN", "addNetwork: wcgID = " + netId);
        Log.d("CHEN", "addNetwork: enableNetWork = " + enableNetWork);
        //可选操作，让Wifi重新连接最近使用过的接入点
        //如果上文的enableNetwork成功，那么reconnect同样连接netId对应的网络
        //若失败，则连接之前成功过的网络
        boolean reconnect = wifiManager.reconnect();
    }
}
