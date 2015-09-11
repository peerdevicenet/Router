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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.xconns.peerdevicenet.DeviceInfo;
import com.xconns.peerdevicenet.NetInfo;
import com.xconns.peerdevicenet.Router;
import com.xconns.peerdevicenet.Router.MsgId;
import com.xconns.peerdevicenet.utils.RouterConfig;
import com.xconns.peerdevicenet.utils.Utils;


public class IntentingAPIPeer implements Peer {
	private final static String TAG = "IntentingAPIPeer";

	private Context context = null;
	private CoreAPI router = null;
	
	private int sessionId = -1;
	
	// use handler for async processing in service
	private HandlerThread myThread;
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgId.START_SEARCH:
				Bundle data = msg.getData();
				int timeout = data.getInt(Router.MsgKey.SEARCH_TIMEOUT);
				String name = data.getString(Router.MsgKey.PEER_NAME);
				String addr = data.getString(Router.MsgKey.PEER_ADDR);
				String port = data.getString(Router.MsgKey.PEER_PORT);
                DeviceInfo leader = null;
                if (addr!=null && addr.length()>0 && port!=null && port.length()>0) {
                    leader = new DeviceInfo(name, addr, port);
                }
				router.startPeerSearch(sessionId, leader, timeout);
				break;
			case MsgId.STOP_SEARCH:
				router.stopPeerSearch(sessionId);
				break;
			case MsgId.ACCEPT_CONNECTION:
				data = msg.getData();
				name = data.getString(Router.MsgKey.PEER_NAME);
				addr = data.getString(Router.MsgKey.PEER_ADDR);
				port = data.getString(Router.MsgKey.PEER_PORT);
				router.acceptConnection(sessionId, new DeviceInfo(name, addr, port));
				break;
			case MsgId.DENY_CONNECTION:
				data = msg.getData();
				name = data.getString(Router.MsgKey.PEER_NAME);
				addr = data.getString(Router.MsgKey.PEER_ADDR);
				port = data.getString(Router.MsgKey.PEER_PORT);
				int denyCode = data.getInt(Router.MsgKey.CONN_DENY_CODE);
				router.denyConnection(sessionId, new DeviceInfo(name, addr, port), denyCode);
				break;
			case MsgId.CONNECT:
				data = msg.getData();
				name = data.getString(Router.MsgKey.PEER_NAME);
				addr = data.getString(Router.MsgKey.PEER_ADDR);
				port = data.getString(Router.MsgKey.PEER_PORT);
				byte[] token = data.getByteArray(Router.MsgKey.AUTHENTICATION_TOKEN);
				timeout = data.getInt(Router.MsgKey.CONNECT_TIMEOUT);
				router.connectPeer(sessionId, new DeviceInfo(name, addr, port), token, timeout);
				break;
			case MsgId.DISCONNECT:
				data = msg.getData();
				name = data.getString(Router.MsgKey.PEER_NAME);
				addr = data.getString(Router.MsgKey.PEER_ADDR);
				port = data.getString(Router.MsgKey.PEER_PORT);
				router.disconnectPeer(sessionId, new DeviceInfo(name, addr, port));
				break;
			case MsgId.JOIN_GROUP:
				String groupId = msg.getData().getString(Router.MsgKey.GROUP_ID);
				router.joinGroup(groupId, null, new MyGroupHandler(groupId));
				break;
			case MsgId.LEAVE_GROUP:
				groupId = msg.getData().getString(Router.MsgKey.GROUP_ID);
				router.leaveGroup(groupId);
				break;
			case MsgId.SEND_MSG:
				// send msg to peer thru socket
				data = msg.getData();
				name = data.getString(Router.MsgKey.PEER_NAME);
				addr = data.getString(Router.MsgKey.PEER_ADDR);
				port = data.getString(Router.MsgKey.PEER_PORT);
				groupId = data.getString(Router.MsgKey.GROUP_ID);
                DeviceInfo targetDevice = null;
                if (addr!=null&&addr.length()>0&&port!=null&port.length()>0) {
                    targetDevice = new DeviceInfo(name, addr, port);
                }
				router.sendMsg(groupId, targetDevice, msg.getData());
				// now for testing, just bounce back
				// recvedMsg("sent: " + msg.getData().getString(MSG_DATA));
				break;
			case MsgId.SET_CONNECTION_INFO:
				data = msg.getData();
				name = data.getString(Router.MsgKey.DEVICE_NAME);
				int liveTime = data.getInt(Router.MsgKey.LIVENESS_TIMEOUT, -1);
				int connTime = data.getInt(Router.MsgKey.CONNECT_TIMEOUT, -1);
				int searchTime = data.getInt(Router.MsgKey.SEARCH_TIMEOUT, -1);
				boolean useSSL = data.getBoolean(Router.MsgKey.USE_SSL, RouterConfig.DEF_USE_SSL);
				router.setConnectionInfo(sessionId, name, useSSL, liveTime, connTime, searchTime);
				break;
			case MsgId.GET_CONNECTION_INFO:
				router.getConnectionInfo(sessionId);
				break;
			case MsgId.GET_DEVICE_INFO:
				router.getDeviceInfo(sessionId);
				break;
			case MsgId.GET_CONNECTED_PEERS:
				data = msg.getData();
				groupId = null;
				if (data != null)
					groupId = data.getString(Router.MsgKey.GROUP_ID);
				router.getConnectedPeers(groupId, sessionId);
				break;
			case MsgId.GET_NETWORKS:
				router.getNetworks(sessionId);
				break;
			case MsgId.GET_ACTIVE_NETWORK:
				router.getActiveNetwork(sessionId);
				break;
			case MsgId.ACTIVATE_NETWORK:
				data = msg.getData();
				int type = data.getInt(Router.MsgKey.NET_TYPE);
				name = data.getString(Router.MsgKey.NET_NAME);
				int encrypt = data.getInt(Router.MsgKey.NET_ENCRYPT);
				String pass = data.getString(Router.MsgKey.NET_PASS);
				boolean hidden = data.getBoolean(Router.MsgKey.NET_HIDDEN);
				byte[] info = data.getByteArray(Router.MsgKey.NET_INFO);
				String intfName = data.getString(Router.MsgKey.NET_INTF_NAME);
				addr = data.getString(Router.MsgKey.NET_ADDR);
				boolean mcast = data.getBoolean(Router.MsgKey.NET_INTF_MCAST);
				router.activateNetwork(sessionId, new NetInfo(type, name, encrypt, pass, hidden, info, intfName, addr, mcast));
				break;
			case MsgId.CONNECT_NETWORK:
				data = msg.getData();
				type = data.getInt(Router.MsgKey.NET_TYPE);
				name = data.getString(Router.MsgKey.NET_NAME);
				encrypt = data.getInt(Router.MsgKey.NET_ENCRYPT);
				pass = data.getString(Router.MsgKey.NET_PASS);
				hidden = data.getBoolean(Router.MsgKey.NET_HIDDEN);
				info = data.getByteArray(Router.MsgKey.NET_INFO);
				intfName = data.getString(Router.MsgKey.NET_INTF_NAME);
				addr = data.getString(Router.MsgKey.NET_ADDR);
				mcast = data.getBoolean(Router.MsgKey.NET_INTF_MCAST);
				router.connectNetwork(sessionId, new NetInfo(type, name, encrypt, pass, hidden, info, intfName, addr, mcast));
				break;
			case MsgId.DISCONNECT_NETWORK:
				data = msg.getData();
				type = data.getInt(Router.MsgKey.NET_TYPE);
				name = data.getString(Router.MsgKey.NET_NAME);
				encrypt = data.getInt(Router.MsgKey.NET_ENCRYPT);
				pass = data.getString(Router.MsgKey.NET_PASS);
				hidden = data.getBoolean(Router.MsgKey.NET_HIDDEN);
				info = data.getByteArray(Router.MsgKey.NET_INFO);
				intfName = data.getString(Router.MsgKey.NET_INTF_NAME);
				addr = data.getString(Router.MsgKey.NET_ADDR);
				mcast = data.getBoolean(Router.MsgKey.NET_INTF_MCAST);
				router.disconnectNetwork(sessionId, new NetInfo(type, name, encrypt, pass, hidden, info, intfName, addr, mcast));
				break;

			
			default:
			}
		}
	}
	
	public IntentingAPIPeer(Context cc, CoreAPI c) {
		context = cc;
		router = c;
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		myThread = new HandlerThread("ServiceStartArguments",
				Process.THREAD_PRIORITY_BACKGROUND);
		myThread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = myThread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		
		sessionId = router.getNextSessionId();
		
		router.registerConnHandler(sessionId, mConnHandler);
	}

	@TargetApi(5)
	public void stop() {
		router.unregisterConnHandler(sessionId);
		myThread.quit();
	}
	
	//no binding allowed for this, so return null
	public IBinder getBinder() {
		return null;
	}
	
	ConnHandler mConnHandler = new ConnHandler() {

		public void onError(String errInfo) {
			Intent i = new Intent(Router.Intent.ACTION_ERROR);
			i.putExtra(Router.MsgKey.MSG_DATA, errInfo);
			context.sendBroadcast(i);	
		}

		public void onConnected(DeviceInfo dev) {
			Intent i = new Intent(Router.Intent.ACTION_CONNECTED);
			i.putExtras(Utils.device2Bundle(dev));
			context.sendBroadcast(i);	
		}

		public void onDisconnected(DeviceInfo dev) {
			Intent i = new Intent(Router.Intent.ACTION_DISCONNECTED);
			i.putExtras(Utils.device2Bundle(dev));
			context.sendBroadcast(i);	
		}

		public void onGetDeviceInfo(DeviceInfo device) {
			Intent i = new Intent(Router.Intent.ACTION_GET_DEVICE_INFO);
			i.putExtras(Utils.device2Bundle(device));
			context.sendBroadcast(i);	
		}

		public void onGetPeerDevices(DeviceInfo[] devices) {
			Intent i = new Intent(Router.Intent.ACTION_GET_CONNECTED_PEERS);
			i.putExtras(Utils.deviceArray2Bundle(devices));
			context.sendBroadcast(i);	
		}
		
		public void onSearchStart(DeviceInfo leader) {
			Intent i = new Intent(Router.Intent.ACTION_SEARCH_START);
			i.putExtras(Utils.device2Bundle(leader));
			context.sendBroadcast(i);				
		}

		public void onSearchFoundDevice(DeviceInfo device, boolean useSSL) {
			Intent i = new Intent(Router.Intent.ACTION_SEARCH_FOUND_DEVICE);
			Bundle b = Utils.device2Bundle(device);
			b.putBoolean(Router.MsgKey.USE_SSL, useSSL);
			i.putExtras(b);
			context.sendBroadcast(i);	
		}

		public void onSearchComplete() {
			Intent i = new Intent(Router.Intent.ACTION_SEARCH_COMPLETE);
			context.sendBroadcast(i);				
		}

		public void onConnecting(DeviceInfo device, byte[] token) {
			Intent i = new Intent(Router.Intent.ACTION_CONNECTING);
			i.putExtras(Utils.device2Bundle(device));
			i.putExtra(Router.MsgKey.AUTHENTICATION_TOKEN, token);
			context.sendBroadcast(i);	
		}

		public void onConnectionFailed(DeviceInfo device, int rejectCode) {
			Intent i = new Intent(Router.Intent.ACTION_CONNECTION_FAILED);
			i.putExtras(Utils.device2Bundle(device));
			i.putExtra(Router.MsgKey.CONN_DENY_CODE, rejectCode);
			context.sendBroadcast(i);	
		}

		@Override
		public void onGetNetworks(NetInfo[] nets) {
			Intent i = new Intent(Router.Intent.ACTION_GET_NETWORKS);
			if (nets != null && nets.length > 0) {
				i.putExtras(Utils.netArray2Bundle(nets));
			}
			context.sendBroadcast(i);	
		}

		@Override
		public void onGetActiveNetwork(NetInfo net) {
			Intent i = new Intent(Router.Intent.ACTION_GET_ACTIVE_NETWORK);
			if (net != null) {
				i.putExtras(Utils.net2Bundle(net));
			}
			context.sendBroadcast(i);	
		}

		@Override
		public void onNetworkConnected(NetInfo net) {
			Intent i = new Intent(Router.Intent.ACTION_NETWORK_CONNECTED);
			if (net != null) {
				i.putExtras(Utils.net2Bundle(net));
			}
			context.sendBroadcast(i);	
		}

		@Override
		public void onNetworkDisconnected(NetInfo net) {
			Intent i = new Intent(Router.Intent.ACTION_NETWORK_DISCONNECTED);
			i.putExtras(Utils.net2Bundle(net));
			context.sendBroadcast(i);	
		}

		@Override
		public void onNetworkActivated(NetInfo net) {
			Intent i = new Intent(Router.Intent.ACTION_ACTIVATE_NETWORK);
			i.putExtras(Utils.net2Bundle(net));
			context.sendBroadcast(i);	
		}

		@Override
		public void onSetConnectionInfo() {
				Intent i = new Intent(Router.Intent.ACTION_SET_CONNECTION_INFO);
				context.sendBroadcast(i);	
		}

		@Override
		public void onGetConnectionInfo(String devName, boolean useSSL, int liveTime,
				int connTime, int searchTime) {
			Intent i = new Intent(Router.Intent.ACTION_GET_CONNECTION_INFO);
			i.putExtra(Router.MsgKey.DEVICE_NAME, devName);
			i.putExtra(Router.MsgKey.LIVENESS_TIMEOUT, liveTime);
			i.putExtra(Router.MsgKey.CONNECT_TIMEOUT, connTime);
			i.putExtra(Router.MsgKey.SEARCH_TIMEOUT, searchTime);
			i.putExtra(Router.MsgKey.USE_SSL, useSSL);
			context.sendBroadcast(i);	
		}

		@Override
		public void onNetworkConnecting(NetInfo net) {
			// TODO Auto-generated method stub
			Intent i = new Intent(Router.Intent.ACTION_NETWORK_CONNECTING);
			i.putExtras(Utils.net2Bundle(net));
			context.sendBroadcast(i);				
		}

		@Override
		public void onNetworkConnectionFailed(NetInfo net) {
			// TODO Auto-generated method stub
			Intent i = new Intent(Router.Intent.ACTION_NETWORK_CONNECTION_FAILED);
			i.putExtras(Utils.net2Bundle(net));
			context.sendBroadcast(i);				
		}

	};
	
	class MyGroupHandler implements GroupHandler {
		String groupId = null;
		
		public MyGroupHandler(String g) {
			groupId = g;
		}

		public void onError(String errInfo) {
			Intent i = new Intent(Router.Intent.ACTION_ERROR);
			i.putExtra(Router.MsgKey.GROUP_ID, groupId);
			i.putExtra(Router.MsgKey.MSG_DATA, errInfo);
			context.sendBroadcast(i);	
		}

		public void onSelfJoin(DeviceInfo[] devices) {
			Intent i = new Intent(Router.Intent.ACTION_SELF_JOIN);
			Bundle b = Utils.deviceArray2Bundle(devices);
			b.putString(Router.MsgKey.GROUP_ID, groupId);
			i.putExtras(b);
			context.sendBroadcast(i);	
		}

		public void onPeerJoin(DeviceInfo dev) {
			Intent i = new Intent(Router.Intent.ACTION_PEER_JOIN);
			Bundle b = Utils.device2Bundle(dev);
			b.putString(Router.MsgKey.GROUP_ID, groupId);
			i.putExtras(b);
			context.sendBroadcast(i);	
		}

		public void onSelfLeave() {
			Intent i = new Intent(Router.Intent.ACTION_SELF_LEAVE);
			i.putExtra(Router.MsgKey.GROUP_ID, groupId);
			context.sendBroadcast(i);	
		}

		public void onPeerLeave(DeviceInfo dev) {
			Intent i = new Intent(Router.Intent.ACTION_PEER_LEAVE);
			Bundle b = Utils.device2Bundle(dev);
			b.putString(Router.MsgKey.GROUP_ID, groupId);
			i.putExtras(b);
			context.sendBroadcast(i);	
		}

		public void onReceive(DeviceInfo src, Bundle msg) {
			Intent i = new Intent(Router.Intent.ACTION_RECV_MSG);
			msg.putString(Router.MsgKey.GROUP_ID, groupId);
			msg.putString(Router.MsgKey.PEER_NAME, src.name);
			msg.putString(Router.MsgKey.PEER_ADDR, src.addr);
			msg.putString(Router.MsgKey.PEER_PORT, src.port);
			i.putExtras(msg);
			context.sendBroadcast(i);	
		}

		public void onGetPeerDevices(DeviceInfo[] devices) {
			Intent i = new Intent(Router.Intent.ACTION_GET_CONNECTED_PEERS);
			Bundle b = Utils.deviceArray2Bundle(devices);
			b.putString(Router.MsgKey.GROUP_ID, groupId);
			i.putExtras(b);
			context.sendBroadcast(i);	
		}
		
	};
	
	//pass intenting msgs outward
	public void sendMsg(Intent intent) {
		Message msg = mServiceHandler.obtainMessage();
		final String action = intent.getAction();
		//normal handle router contrl msgs
		if (Router.Intent.ACTION_START_SEARCH.equals(action)) {
			msg.what = MsgId.START_SEARCH;
		} 
		else if (Router.Intent.ACTION_STOP_SEARCH.equals(action)) {
			msg.what = MsgId.STOP_SEARCH;
		} 
		else if (Router.Intent.ACTION_ACCEPT_CONNECTION.equals(action)) {
			msg.what = MsgId.ACCEPT_CONNECTION;
		} 
		else if (Router.Intent.ACTION_DENY_CONNECTION.equals(action)) {
			msg.what = MsgId.DENY_CONNECTION;
		} 
		else if (Router.Intent.ACTION_CONNECT.equals(action)) {
			msg.what = MsgId.CONNECT;
		} else if (Router.Intent.ACTION_DISCONNECT.equals(action)) {
			msg.what = MsgId.DISCONNECT;
		} else if (Router.Intent.ACTION_JOIN_GROUP.equals(action)) {
			msg.what = MsgId.JOIN_GROUP;
		} else if (Router.Intent.ACTION_LEAVE_GROUP.equals(action)) {
			msg.what = MsgId.LEAVE_GROUP;
		} else if (Router.Intent.ACTION_SEND_MSG.equals(action)) {
			msg.what = MsgId.SEND_MSG;
		} else if (Router.Intent.ACTION_SET_CONNECTION_INFO.equals(action)) {
			msg.what = MsgId.SET_CONNECTION_INFO;
		} else if (Router.Intent.ACTION_GET_CONNECTION_INFO.equals(action)) {
			msg.what = MsgId.GET_CONNECTION_INFO;
		} else if (Router.Intent.ACTION_GET_DEVICE_INFO.equals(action)) {
			msg.what = MsgId.GET_DEVICE_INFO;
		} else if (Router.Intent.ACTION_GET_CONNECTED_PEERS.equals(action)) {
			msg.what = MsgId.GET_CONNECTED_PEERS;
		} else if (Router.Intent.ACTION_GET_NETWORKS.equals(action)) {
			msg.what = MsgId.GET_NETWORKS;
		} else if (Router.Intent.ACTION_GET_ACTIVE_NETWORK.equals(action)) {
			msg.what = MsgId.GET_ACTIVE_NETWORK;
		} else if (Router.Intent.ACTION_ACTIVATE_NETWORK.equals(action)) {
			msg.what = MsgId.ACTIVATE_NETWORK;
		} else {
			Log.e(TAG, "not handled intent msg action: "+action);
			return;
		}
		
		msg.setData(intent.getExtras());
		// pack msg data
		mServiceHandler.sendMessage(msg);			
	}
	
}
