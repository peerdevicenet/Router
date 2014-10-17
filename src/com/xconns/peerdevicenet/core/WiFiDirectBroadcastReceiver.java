/*
 * Copyright (C) 2013 Yigong Liu, XCONNS, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xconns.peerdevicenet.core;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

/**
 * A BroadcastReceiver that notifies of important wifi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	private WifiP2pManager manager;
	private Channel channel;
	private WifiDirectTransport wifiDirect = null;

	/**
	 * @param manager
	 *            WifiP2pManager system service
	 * @param channel
	 *            Wifi p2p channel
	 * @param wifiD
	 *            transport associated with the receiver
	 */
	@SuppressLint("NewApi")
	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
			WifiDirectTransport wifiD) {
		this.manager = manager;
		this.channel = channel;
		this.wifiDirect = wifiD;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

			// UI update to indicate wifi p2p status.
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				// Wifi Direct mode is enabled
				wifiDirect.onEnabledStatus(true);
				Log.d(WifiDirectTransport.TAG, "P2P state enabled");
			} else {
				wifiDirect.onEnabledStatus(false);
				wifiDirect.reset(false);
				Log.d(WifiDirectTransport.TAG, "P2P state disabled");
			}
			Log.d(WifiDirectTransport.TAG, "P2P state changed - " + state);
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

			Log.d(WifiDirectTransport.TAG, "P2P peers changed");
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
				.equals(action)) {

			if (manager == null) {
				return;
			}

			NetworkInfo networkInfo = (NetworkInfo) intent
					.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

			if (networkInfo.isConnected()) {

				// we are connected with the other device, request connection
				// info to find group owner IP

				Log.d(WifiDirectTransport.TAG,
						"one P2P peer connected or group formed");

			} else {
				// It's a disconnect
				Log.d(WifiDirectTransport.TAG,
						"one P2P peer disconnected or group removed");
			}
			manager.requestConnectionInfo(channel, wifiDirect.connInfoHandler);
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
				.equals(action)) {
			Log.d(WifiDirectTransport.TAG, "my device changed");
		}
	}
}
