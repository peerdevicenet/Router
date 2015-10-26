/*
 * Copyright (C) 2011 ZXing authors
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
package com.xconns.peerdevicenet.utils;

import java.util.List;
import java.util.regex.Pattern;

import com.xconns.peerdevicenet.NetInfo;
import com.xconns.peerdevicenet.Router;
import com.xconns.peerdevicenet.core.Transport;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

/**
 * @author Vikram Aggarwal
 * @author Sean Owen
 * @author Yigong Liu
 */
public final class WifiConnector {

	private static final String TAG = WifiConnector.class.getSimpleName();

	private static final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");

	private final WifiManager wifiManager;
	private final Transport.Handler handler;

	public WifiConnector(WifiManager wifiManager, Transport.Handler handler) {
		this.wifiManager = wifiManager;
		this.handler = handler;
	}

	public void connect(NetInfo wifiData) {
		// Start WiFi, otherwise nothing will work
		if (!wifiManager.isWifiEnabled()) {
			Log.i(TAG, "Enabling wi-fi...");
			if (wifiManager.setWifiEnabled(true)) {
				Log.i(TAG, "Wi-fi enabled");
			} else {
				Log.w(TAG, "Wi-fi could not be enabled!");
				wifiData.info = "Wi-fi could not be enabled!".getBytes();
				handler.onNetworkConnectionFailed(wifiData);
				return;
			}
			// This happens very quickly, but need to wait for it to enable. A
			// little busy wait?
			int count = 0;
			while (!wifiManager.isWifiEnabled()) {
				if (count >= 60) {
					Log.i(TAG, "Took too long to enable wi-fi, quitting");
					wifiData.info = "Took too long to enable wi-fi, quitting".getBytes();
					handler.onNetworkConnectionFailed(wifiData);
					return;
				}
				Log.i(TAG, "Still waiting for wi-fi to enable...");
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException ie) {
					// continue
				}
				count++;
			}
		}
		if (wifiData.encrypt == NetInfo.NoPass) {
			Log.d(TAG, "changeNetworkUnEncrypted");
			changeNetworkUnEncrypted(wifiManager, wifiData);
		} else {
			String password = wifiData.pass;
			if (password != null && password.length() != 0) {
				if (wifiData.encrypt == NetInfo.WEP) {
					Log.d(TAG, "changeNetworkWEP");
					changeNetworkWEP(wifiManager, wifiData);
				} else if (wifiData.encrypt == NetInfo.WPA) {
					Log.d(TAG, "changeNetworkWPA");
					changeNetworkWPA(wifiManager, wifiData);
				}
			} else {
				// wifi no passwd, try to find existing configured and make it
				// active
				List<WifiConfiguration> existingConfigs = wifiManager
						.getConfiguredNetworks();
				String name2 = trimQuote(wifiData.name.trim());
				String name1 = null;
				for (WifiConfiguration ec : existingConfigs) {
					name1 = trimQuote(ec.SSID.trim());
					if (name1.equals(name2)) {
						if (wifiManager.enableNetwork(ec.networkId, true)) {
							Log.i(TAG, "Associating to existing network " + ec.SSID);
							wifiManager.saveConfiguration();
						} else {
							String errMsg = "Failed to enable network " + ec.SSID;
							Log.w(TAG, errMsg);
							wifiData.info = errMsg.getBytes();
							handler.onNetworkConnectionFailed(wifiData);
						}
						return;
					}
				}
			}
		}
	}
	
	public void disconnect(NetInfo wifiData) {
		WifiConfiguration config = changeNetworkCommon(wifiData);
		Integer foundNetworkID = findNetworkInExistingConfig(wifiManager,
				config.SSID);
		if (foundNetworkID != null) {
			Log.i(TAG, "Removing old configuration for network " + config.SSID);
			wifiManager.disableNetwork(foundNetworkID);
			wifiManager.removeNetwork(foundNetworkID);
			wifiManager.saveConfiguration();
		}
	}
	
	/**
	 * Update the network: either create a new network or modify an existing
	 * network
	 * 
	 * @param config
	 *            the new network configuration
	 * @return network ID of the connected network.
	 */
	private static void updateNetwork(WifiManager wifiManager,
			WifiConfiguration config) {
		Integer foundNetworkID = findNetworkInExistingConfig(wifiManager,
				config.SSID);
		if (foundNetworkID != null) {
			Log.i(TAG, "Removing old configuration for network " + config.SSID);
			wifiManager.disableNetwork(foundNetworkID);
			wifiManager.removeNetwork(foundNetworkID);
			wifiManager.saveConfiguration();
		}

		int networkId = wifiManager.addNetwork(config);
		if (networkId >= 0) {
			// Try to disable the current network and start a new one.
			if (wifiManager.enableNetwork(networkId, true)) {
				Log.i(TAG, "Associating to network " + config.SSID);
				wifiManager.saveConfiguration();
			} else {
				Log.w(TAG, "Failed to enable network " + config.SSID);
			}
		} else {
			Log.w(TAG, "Unable to add network " + config.SSID);
		}
	}
	


	private static WifiConfiguration changeNetworkCommon(NetInfo wifiResult) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		// Android API insists that an ascii SSID must be quoted to be correctly
		// handled.
		config.SSID = quoteNonHex(wifiResult.name);
		config.hiddenSSID = wifiResult.hidden;
		return config;
	}

	// Adding a WEP network
	private static void changeNetworkWEP(WifiManager wifiManager,
			NetInfo wifiResult) {
		WifiConfiguration config = changeNetworkCommon(wifiResult);
		config.wepKeys[0] = quoteNonHex(wifiResult.pass, 10, 26, 58);
		config.wepTxKeyIndex = 0;
		config.allowedAuthAlgorithms
				.set(WifiConfiguration.AuthAlgorithm.SHARED);
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		updateNetwork(wifiManager, config);
	}

	// Adding a WPA or WPA2 network
	private static void changeNetworkWPA(WifiManager wifiManager,
			NetInfo wifiResult) {
		WifiConfiguration config = changeNetworkCommon(wifiResult);
		// Hex passwords that are 64 bits long are not to be quoted.
		config.preSharedKey = quoteNonHex(wifiResult.pass, 64);
		config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		config.allowedProtocols.set(WifiConfiguration.Protocol.WPA); // For WPA
		config.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // For WPA2
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
		config.allowedPairwiseCiphers
				.set(WifiConfiguration.PairwiseCipher.TKIP);
		config.allowedPairwiseCiphers
				.set(WifiConfiguration.PairwiseCipher.CCMP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		updateNetwork(wifiManager, config);
	}

	// Adding an open, unsecured network
	private static void changeNetworkUnEncrypted(WifiManager wifiManager,
			NetInfo wifiResult) {
		WifiConfiguration config = changeNetworkCommon(wifiResult);
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		updateNetwork(wifiManager, config);
	}

	private static Integer findNetworkInExistingConfig(WifiManager wifiManager,
			String ssid) {
		List<WifiConfiguration> existingConfigs = wifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals(ssid)) {
				return existingConfig.networkId;
			}
		}
		return null;
	}

	private static String quoteNonHex(String value, int... allowedLengths) {
		return isHexOfLength(value, allowedLengths) ? value
				: convertToQuotedString(value);
	}

	/**
	 * Encloses the incoming string inside double quotes, if it isn't already
	 * quoted.
	 * 
	 * @param string
	 *            the input string
	 * @return a quoted string, of the form "input". If the input string is
	 *         null, it returns null as well.
	 */
	private static String convertToQuotedString(String string) {
		if (string == null || string.length() == 0) {
			return null;
		}
		// If already quoted, return as-is
		if (string.charAt(0) == '"'
				&& string.charAt(string.length() - 1) == '"') {
			return string;
		}
		return '\"' + string + '\"';
	}

	/**
	 * @param value
	 *            input to check
	 * @param allowedLengths
	 *            allowed lengths, if any
	 * @return true if value is a non-null, non-empty string of hex digits, and
	 *         if allowed lengths are given, has an allowed length
	 */
	private static boolean isHexOfLength(CharSequence value,
			int... allowedLengths) {
		if (value == null || !HEX_DIGITS.matcher(value).matches()) {
			return false;
		}
		if (allowedLengths.length == 0) {
			return true;
		}
		for (int length : allowedLengths) {
			if (value.length() == length) {
				return true;
			}
		}
		return false;
	}


	private static String trimQuote(String name1) {
		char[] b1 = name1.toCharArray();
		if (b1.length > 2) {
			int start = 0;
			if (b1[0] == '"')
				start = 1;
			int end = b1.length - 1;
			if (b1[end] == '"')
				end = b1.length - 2;
			name1 = new String(b1, start, end - start + 1);
		}
		return name1;
	}

}
