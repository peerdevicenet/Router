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

package com.xconns.peerdevicenet;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.xconns.peerdevicenet.IRouterConnectionHandler;
import com.xconns.peerdevicenet.IRouterConnectionService;

/**
 * RouterConnectionClient wrapper class is the preferred way to access the asynchronous one-way AIDL api of Router Connection Service.
 * It enables two-way asynchronous messaging between clients and ConnectionService, and performs some extra book keeping:
 * <ul>
 * <li> bind to (later unbind) Router ConnectionService to start a new session and hold session id.
 * <li> expose api methods which invoke ConnectionService one-way asynchronous messaging api
 *    as defined by IRouterConnectionService.aidl; perform operations such as
 *    retrieving network info, activating network, connecting or disconnecting device.
 * <li> register a ConnectionHandler which exposes client side one-way asynchronous messaging api to allow ConnectionService
 *    call back to notify events such as network detachment, device connection and disconnection.
 * <li> buffering api calls when the binding with ConnectionService is not ready,
 *    and resend buffered calls when the binding is ready.
 * </ul>
 * <p>
 * It exposes the following groups of methods in accordance with ConnectionService AIDL api:
 * <ul>
 * <li>network detection and management:
 * <pre>
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#getNetworks%28%29">getNetworks</a>()
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#getActiveNetwork%28%29">getActiveNetwork</a>()
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#activateNetwork%28com.xconns.peerdevicenet.NetInfo%29">activateNetwork</a>(<a href="../../../com/xconns/peerdevicenet/NetInfo.html" title="class in com.xconns.peerdevicenet">NetInfo</a>&nbsp;net)
 * </pre>
 * <li>peer discovery:
 * <pre>
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#startPeerSearch%28com.xconns.peerdevicenet.DeviceInfo,%20int%29">startPeerSearch</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;groupLeader, int&nbsp;timeout)
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#stopPeerSearch%28%29">stopPeerSearch</a>()
 * </pre>
 * <li>peer device connection:
 * <pre>
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#acceptConnection%28com.xconns.peerdevicenet.DeviceInfo%29">acceptConnection</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;peer)
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#connect%28com.xconns.peerdevicenet.DeviceInfo,%20byte[],%20int%29">connect</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;peerInfo, byte[]&nbsp;token, int&nbsp;timeout)
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#denyConnection%28com.xconns.peerdevicenet.DeviceInfo,%20int%29">denyConnection</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;peer, int&nbsp;rejectCode)
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#disconnect%28com.xconns.peerdevicenet.DeviceInfo%29">disconnect</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;peerInfo)
 * </pre>
 * <li>info query:
 * <pre>
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#setConnectionInfo%28java.lang.String,%20boolean,%20int,%20int,%20int%29">setConnectionInfo</a>(<a href="file:///home/dev/tools/adt-bundle-linux-x86_64-20140702/sdk/docs/reference/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</a>&nbsp;devName, boolean&nbsp;useSSL, int&nbsp;liveTime,<a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#getDeviceInfo%28%29">getDeviceInfo</a>() int&nbsp;connTime, int&nbsp;searchTime)
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#getConnectionInfo%28%29">getConnectionInfo</a>()
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#getDeviceInfo%28%29">getDeviceInfo</a>()
 * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.html#getPeerDevices%28%29">getPeerDevices</a>()
 * </pre>
 * </ul>
 * <p>
 * A sample scenario of using RouterConnectionClient to interact with ConnectionService is as following:
 * <ol>
 * <li> Initialization at onCreate() or onResume():
 <pre>
 //first bind to Router ConnectionService, and register ConnectionHandler.
 connClient = new RouterConnectionClient(this, connHandler);
 connClient.bindService();
 </pre>
 * <li> call service async api to perform operations:
 <pre>
 connClient.connect(device, securityToken.getBytes(), connTimeout);
 </pre>
 * <li> cleanup at onPause() or onDestroy():
 <pre>
 connClient.unbindService();
 </pre>
 * </ol>
 * <p>
 * For detailed tutorial on how to use RouterConnectionClient to talk to ConnectionService
 * and set up peer device connections, please check out <a href="http://github.com/peerdevicenet/sample_Connector_wifi_aidl">github sample project</a>.
 */
public class RouterConnectionClient {
	// Debugging
	private static final String TAG = "RouterConnectionClient";

	/**
	 * ConnectionHandler exposes client side async messaging one-way AIDL api to allow ConnectionService call back to
     * notify events such as network attachment and detachment, device connection and disconnection. It implements IConnectionHandler.aidl.
	 * <p>
	 * It exposes the following groups of callback methods in
     * accordance with ConnectionHandler AIDL api:
     * <ul>
     * <li>network detection:
     * <pre>
     * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onGetActiveNetwork%28com.xconns.peerdevicenet.NetInfo%29">onGetActiveNetwork</a>(<a href="../../../com/xconns/peerdevicenet/NetInfo.html" title="class in com.xconns.peerdevicenet">NetInfo</a>&nbsp;net)
     * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onGetNetworks%28com.xconns.peerdevicenet.NetInfo[]%29">onGetNetworks</a>(<a href="../../../com/xconns/peerdevicenet/NetInfo.html" title="class in com.xconns.peerdevicenet">NetInfo</a>[]&nbsp;nets)
     * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onNetworkActivated%28com.xconns.peerdevicenet.NetInfo%29">onNetworkActivated</a>(<a href="../../../com/xconns/peerdevicenet/NetInfo.html" title="class in com.xconns.peerdevicenet">NetInfo</a>&nbsp;net)
     * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onNetworkConnected%28com.xconns.peerdevicenet.NetInfo%29">onNetworkConnected</a>(<a href="../../../com/xconns/peerdevicenet/NetInfo.html" title="class in com.xconns.peerdevicenet">NetInfo</a>&nbsp;net)
     * void <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onNetworkDisconnected%28com.xconns.peerdevicenet.NetInfo%29">onNetworkDisconnected</a>(<a href="../../../com/xconns/peerdevicenet/NetInfo.html" title="class in com.xconns.peerdevicenet">NetInfo</a>&nbsp;net)
     * </pre>
     * <li>peer discovery:
     * <pre>
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onSearchStart%28com.xconns.peerdevicenet.DeviceInfo%29">onSearchStart</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;groupLeader)
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onSearchFoundDevice%28com.xconns.peerdevicenet.DeviceInfo,%20boolean%29">onSearchFoundDevice</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;device, boolean&nbsp;useSSL)
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onSearchComplete%28%29">onSearchComplete</a>()
     * </pre>
     * <li>peer device connection:
     * <pre>
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onConnected%28com.xconns.peerdevicenet.DeviceInfo%29">onConnected</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;peerInfo)
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onConnecting%28com.xconns.peerdevicenet.DeviceInfo,%20byte[]%29">onConnecting</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;device, byte[]&nbsp;token)
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onConnectionFailed%28com.xconns.peerdevicenet.DeviceInfo,%20int%29">onConnectionFailed</a>(<ahref="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;device, int&nbsp;rejectCode)
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onDisconnected%28com.xconns.peerdevicenet.DeviceInfo%29">onDisconnected</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;peerInfo)
     * </pre>
     * <li>info query:
     * <pre>
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onSetConnectionInfo%28%29">onSetConnectionInfo</a>()
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onGetConnectionInfo%28java.lang.String,%20boolean,%20int,%20int,%20int%29">onGetConnectionInfo</a>(<a href="file:///home/dev/tools/adt-bundle-linux-x86_64-20140702/sdk/docs/reference/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</a>&nbsp;devName, boolean&nbsp;useSSL, int&nbsp;liveTime, int&nbsp;connTime, int&nbsp;searchTime)
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onGetDeviceInfo%28com.xconns.peerdevicenet.DeviceInfo%29">onGetDeviceInfo</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>&nbsp;device)
     * <a href="../../../com/xconns/peerdevicenet/RouterConnectionClient.ConnectionHandler.html#onGetPeerDevices%28com.xconns.peerdevicenet.DeviceInfo[]%29">onGetPeerDevices</a>(<a href="../../../com/xconns/peerdevicenet/DeviceInfo.html" title="class in com.xconns.peerdevicenet">DeviceInfo</a>[]&nbsp;devices)
     * </pre>
     * </ul>
	 */
	public interface ConnectionHandler {
        /**
         * Notify clients about two kinds of errors:
         * <ul>
         * <li> errors happening at server processing.
         * <li> errors happening at client invoking ConnectionService api.
         *</ul>
         * @param errInfo the detailed error message
         */
		void onError(String errInfo);

        /**
         * Pass to clients info about currently attached networks detected by ConnectionService.
         * This is the response to getNetworks() api call.
         * @param nets info about current networks to which the device is attached.
         */
		void onGetNetworks(NetInfo[] nets);

        /**
         * Pass to clients info about the network used to connect to peer devices.
         * This is the response to getActiveNetwork() call.
         *
         * @param net info about the network actively used to communicate with peer devices.
         */
		void onGetActiveNetwork(NetInfo net);

        /**
         * Notify clients that ConnectionService detects a new network.
         *
         * @param net info about a network this device is newly attached.
         */
		void onNetworkConnected(NetInfo net);

        /**
         * Notify clients that a network is disconnected.
         *
         * @param net info about disconnected network.
         */
		void onNetworkDisconnected(NetInfo net);

        /**
         * Pass to clients info about network activated.
         * This is the response to activateNetwork() api call.
         *
         * @param net info of the network activated.
         */
		void onNetworkActivated(NetInfo net);

        /**
         * Notify client a search session started; It is the response to startSearch() api call.
         *
         * @param groupLeader info of leader device which may run hotspot or own wifi
         *                    direct group. It is the enabler for a group of devices
         *                    to discover each other.
         */
		void onSearchStart(DeviceInfo groupLeader);

        /**
         * Notify clients in a search session a device is found.
         *
         * @param device info of found device (name, address, port number).
         * @param useSSL does connection to this device use TLS/SSL or not.
         */
		void onSearchFoundDevice(DeviceInfo device, boolean useSSL);

        /**
         * Notify clients current active search session completes, either because clients
         * called stopSearch() or search session timeout.
         */
		void onSearchComplete();

        /**
         * Notify client a peer device is trying to connect to this device.
         *
         * @param device peer device info which try to connect
         * @param token binary data passed by peer device as authentication token.
         */
		void onConnecting(DeviceInfo device, byte[] token);

        /**
         * Notify client its attempt to connect to peer device failed.
         *
         * @param device target peer device the client try to connect
         * @param rejectCode reason of connection failure.
         */
		void onConnectionFailed(DeviceInfo device, int rejectCode);

        /**
         * Notify client that a peer device successfully connected to this device;
         * either because the client accept peer connection request or peer accept the client's
         * connection request.
         *
         * @param peerInfo info of connected device.
         */
		void onConnected(DeviceInfo peerInfo);

        /**
         * Notify client a peer device disconnected;
         * either because the client or its peer called disconnect() api; or
         * because network failed.
         *
         * @param peerInfo info of disconnected device.
         */
		void onDisconnected(DeviceInfo peerInfo);

        /**
         * Notify client its call to setConnectionInfo() api call succeed and
         * connection related settings (device name, use SSL, connection timeout, etc.)
         * have been updated successfully at Connection Service.
         */
		void onSetConnectionInfo();

        /**
         * Reply to client connection related settings currently active at Connection Service;
         * This is the response to getConnectionInfo() api call.
         *
         * @param devName name of this device
         * @param useSSL does this device use TLS/SSL connect to peer devices or not.
         * @param liveTime the period between successive verifications of liveness of peer device.
         * @param connTime peer device connection timeout.
         * @param searchTime search session timeout.
         */
		void onGetConnectionInfo(String devName, boolean useSSL, int liveTime, int connTime, int searchTime);

        /**
         * Reply to client info about this device; This is the response to getDeviceInfo() api call.
         *
         * @param device info of this device (name, address, port).
         */
		void onGetDeviceInfo(DeviceInfo device);

        /**
         * Reply to client info about peer devices connected to this device.
         *
         * @param devices peer devices currently connected.
         */
		void onGetPeerDevices(DeviceInfo[] devices);
	}

	private Context context = null;
	private int sessionId = -1;
	private IRouterConnectionService mConnService = null;
	private ConnectionHandler registeredHandler = null;
	private List<Message> sentMsgBuf = new ArrayList<Message>();

    /**
     * RouterConnectionClient constructor; register a ConnectionHandler to expose client side one-way asynchronous messaging api for ConnectionService to call back.
     *
     * @param c context (activity or service) in which to bind to Connection Service.
     * @param h register a ConnectionHandler to allow ConnectionService call back.
     */
	public RouterConnectionClient(Context c, ConnectionHandler h) {
		context = c;
		registeredHandler = h;
	}

	private IRouterConnectionHandler mConnHandler = new IRouterConnectionHandler.Stub() {
		public void onError(String errInfo) {
			registeredHandler.onError(errInfo);
		}

		public void onGetNetworks(NetInfo[] nets) {
			Log.d(TAG, "onGetNetworks callback");
			registeredHandler.onGetNetworks(nets);
		}
		public void onGetActiveNetwork(NetInfo net) {
			Log.d(TAG, "onGetActiveNetwork callback");
			registeredHandler.onGetActiveNetwork(net);
		}
		public void onNetworkConnected(NetInfo net) {
			Log.d(TAG, "onNetworkConnected callback");
			registeredHandler.onNetworkConnected(net);
		}
		public void onNetworkDisconnected(NetInfo net) {
			Log.d(TAG, "onNetworkDisconnected callback");
			registeredHandler.onNetworkDisconnected(net);
		}
		public void onNetworkActivated(NetInfo net) {
			Log.d(TAG, "onNetworkActivated callback");
			registeredHandler.onNetworkActivated(net);
		}

		@Override
		public void onSearchStart(DeviceInfo groupLeader)
				throws RemoteException {
			Log.d(TAG, "onSearchStart callback");
			Log.d(TAG, "onSearchStart: "+groupLeader);
			registeredHandler.onSearchStart(groupLeader);
		}
		
		public void onSearchFoundDevice(DeviceInfo device, boolean useSSL) {
			registeredHandler.onSearchFoundDevice(device, useSSL);
		}
		public void onSearchComplete() {
			registeredHandler.onSearchComplete();
		}
		public void onConnecting(DeviceInfo device, byte[] token) {
			registeredHandler.onConnecting(device, token);
		}
		public void onConnectionFailed(DeviceInfo device, int rejectCode) {
			registeredHandler.onConnectionFailed(device, rejectCode);
		}
		
		public void onConnected(DeviceInfo peerInfo) {
			registeredHandler.onConnected(peerInfo);
		}

		public void onDisconnected(DeviceInfo peerInfo) {
			registeredHandler.onDisconnected(peerInfo);
		}

		public void onGetDeviceInfo(DeviceInfo device) throws RemoteException {
			registeredHandler.onGetDeviceInfo(device);
		}

		public void onGetPeerDevices(DeviceInfo[] devices)
				throws RemoteException {
			registeredHandler.onGetPeerDevices(devices);
		}

		@Override
		public void onSetConnectionInfo() throws RemoteException {
			registeredHandler.onSetConnectionInfo();
		}

		@Override
		public void onGetConnectionInfo(String devName, boolean useSSL, int liveTime,
				int connTime, int searchTime)
				throws RemoteException {
			registeredHandler.onGetConnectionInfo(devName, useSSL, liveTime, connTime, searchTime);
		}

	};

	/**
	 * Class for interacting with the main interface of the service.
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.d(TAG, "Conn Service onServiceConnected() called");
			mConnService = IRouterConnectionService.Stub.asInterface(service);
			try {
				if (registeredHandler != null)
					sessionId = mConnService.startSession(mConnHandler);
				else
					sessionId = mConnService.startSession(null);
			} catch (RemoteException e) {
				Log.e(TAG,
						"failed at registerConnectionHandler: "
								+ e.getMessage());
			}
			Log.d(TAG, "Conn service finish setup, connHandler registered");
			sendBufferedMsgs();
		}

		public void onServiceDisconnected(ComponentName className) {
			mConnService = null;
		}
	};

    /**
     * bind to Router Connection Service.
     */
	public void bindService() {
		Intent intent = new Intent("com.xconns.peerdevicenet.ConnectionService");
		context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

    /**
     * unbind from Router Connection Service.
     */
	public void unbindService() {
		Log.d(TAG, "unbindService");
		if (mConnService != null) {
			try {
				Log.d(TAG, "connService.stopSession");
				mConnService.stopSession(sessionId);
			} catch (RemoteException e) {
				Log.e(TAG,
						"failed at unregisterConnectionHandler: "
								+ e.getMessage());
			}
		}
		// Detach our existing connection.
		Log.d(TAG, "context.unbindService(mConnection)");
		context.unbindService(mConnection);
	}
	
	static final class ConnInfo {
		String name;
		int liveTime;
		int connTime;
		int searchTime;
		boolean useSSL;
		public ConnInfo(String n, boolean us, int l, int c, int s) {
			name = n; liveTime = l; connTime = c; searchTime = s; 
			useSSL = us;
		}
	}

	void sendBufferedMsgs() {
		int sz = sentMsgBuf.size();
		if (sz > 0) {
			Log.d(TAG, "send buffered cmds");
			for (int i = 0; i < sz; i++) {
				Message m = sentMsgBuf.get(i);
				try {
					switch (m.what) {
					case Router.MsgId.START_SEARCH:
						mConnService.startPeerSearch(sessionId, (DeviceInfo)m.obj, m.arg1);
						break;
					case Router.MsgId.STOP_SEARCH:
						mConnService.stopPeerSearch(sessionId);
						break;
					case Router.MsgId.ACCEPT_CONNECTION:
						mConnService.acceptConnection(sessionId, (DeviceInfo) m.obj);
						break;
					case Router.MsgId.DENY_CONNECTION:
						mConnService.denyConnection(sessionId, (DeviceInfo) m.obj, m.arg1);
						break;
					case Router.MsgId.CONNECT:
						Object[] dd = (Object[]) m.obj;
						mConnService.connect(sessionId, (DeviceInfo)dd[0], (byte[])dd[1], m.arg1);
						break;
					case Router.MsgId.DISCONNECT:
						mConnService.disconnect(sessionId, (DeviceInfo) m.obj);
						break;
					case Router.MsgId.SET_CONNECTION_INFO:
						ConnInfo ci = (ConnInfo) m.obj;
						mConnService.setConnectionInfo(sessionId, ci.name, ci.useSSL, ci.liveTime, ci.connTime, ci.searchTime);
						break;
					case Router.MsgId.GET_CONNECTION_INFO:
						mConnService.getConnectionInfo(sessionId);
						break;
					case Router.MsgId.GET_DEVICE_INFO:
						mConnService.getDeviceInfo(sessionId);
						break;
					case Router.MsgId.GET_CONNECTED_PEERS:
						mConnService.getPeerDevices(sessionId);
						break;
					case Router.MsgId.GET_NETWORKS:
						mConnService.getNetworks(sessionId);
						break;
					case Router.MsgId.GET_ACTIVE_NETWORK:
						mConnService.getActiveNetwork(sessionId);
						break;
					case Router.MsgId.ACTIVATE_NETWORK:
						mConnService.activateNetwork(sessionId, (NetInfo) m.obj);
						break;
					default:
						break;
					}
				} catch (RemoteException re) {
					Log.e(TAG,
							"failed to call RouterConnectionService: "
									+ re.getMessage());
                    registeredHandler.onError(re.getMessage());
				}
			}
			sentMsgBuf.clear();
		}
	}

    /**
     * start a new search session to find peer devices.
     *
     * @param groupLeader info of leader device which may run hotspot or own wifi
     *                    direct group. It is the enabler for a group of devices
     *                    to discover each other.
     * @param timeout search session timeout in seconds; if set to 0, search forever until
     *                stopped explicitly; if set to negative, use preset search timeout (default 30 seconds).
     */
	public void startPeerSearch(DeviceInfo groupLeader, int timeout) {
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.START_SEARCH;
			m.obj = groupLeader;
			m.arg1 = timeout;
			sentMsgBuf.add(m);
			return;
		}
		try {
			mConnService.startPeerSearch(sessionId, groupLeader, timeout);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to startPeerSearch: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}		
	}

    /**
     * stop current search session.
     */
	public void stopPeerSearch() {
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.STOP_SEARCH;
			sentMsgBuf.add(m);
			return;
		}
		try {
			mConnService.stopPeerSearch(sessionId);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to stopPeerSearch: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

    /**
     * accept the connection request from a peer device.
     *
     * @param peer info of connecting peer device.
     */
	public void acceptConnection(DeviceInfo peer) {
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.ACCEPT_CONNECTION;
			m.obj = peer;
			sentMsgBuf.add(m);
			return;
		}
		try {
			mConnService.acceptConnection(sessionId, peer);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to accept_connect: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

    /**
     * deny the connection request from a peer device and tell the reason.
     *
     * @param peer info of connecting peer device.
     * @param rejectCode reason of denial.
     */
	public void denyConnection(DeviceInfo peer, int rejectCode) {
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.DENY_CONNECTION;
			m.arg1 = rejectCode;
			m.obj = peer;
			sentMsgBuf.add(m);
			return;
		}
		try {
			mConnService.denyConnection(sessionId, peer, rejectCode);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to accept_connect: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

    /**
     * send connection request to peer device.
     *
     * @param peerInfo info of peer device.
     * @param token binary data for authentication.
     * @param timeout connection timeout in seconds; if set to 0, wait forever;
     *                if set to negative, use preset value (default to 5 seconds).
     */
	public void connect(DeviceInfo peerInfo, byte[] token, int timeout) {
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.CONNECT;
			m.arg1 = timeout;
			m.obj = new Object[]{peerInfo, token};
			sentMsgBuf.add(m);
			return;
		}
		try {
			mConnService.connect(sessionId, peerInfo, token, timeout);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to connect: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

    /**
     * disconnect a connected peer device.
     *
     * @param peerInfo info of device to be disconnected.
     */
	public void disconnect(DeviceInfo peerInfo) {
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.DISCONNECT;
			m.obj = peerInfo;
			sentMsgBuf.add(m);
			return;
		}
		try {
			mConnService.disconnect(sessionId, peerInfo);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to disconnect: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

    /**
     * set connection related parameters at Connection Service; they will be used as default hereafter.
     *
     * @param devName name of this device appearing to peers.
     * @param useSSL if use TLS/SSL for peer connections.
     * @param liveTime the period between successive verifications of peer devices aliveness.
     * @param connTime socket connection attempt timeout.
     * @param searchTime peer device search timeout.
     */
	public void setConnectionInfo(String devName, boolean useSSL, int liveTime, int connTime, int searchTime) {
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.SET_CONNECTION_INFO;
			m.obj = new ConnInfo(devName, useSSL, liveTime, connTime, searchTime);
			sentMsgBuf.add(m);
			return;
		}
		try {
			mConnService.setConnectionInfo(sessionId, devName, useSSL, liveTime, connTime, searchTime);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to setConnectionInfo: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

    /**
     * request current connection related parameters at Connection Service.
     * the response is returned at ConnectionHandler.onGetConnectionInfo().
     */
	public void getConnectionInfo() {
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.GET_CONNECTION_INFO;
			sentMsgBuf.add(m);
			return;
		}
		try {
			Log.d(TAG, "start getConnectionInfo()");
			mConnService.getConnectionInfo(sessionId);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to getConnectionInfo: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

    /**
     * request current device info (name, address, port) known at Connection Service.
     * the response is returned at ConnectionHandler.onGetDeviceInfo().
     */
	public void getDeviceInfo() {
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.GET_DEVICE_INFO;
			sentMsgBuf.add(m);
			return;
		}
		try {
			Log.d(TAG, "start getDeviceInfo()");
			mConnService.getDeviceInfo(sessionId);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to getDeviceInfo: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

    /**
     * retrieve info of connected peer devices;
     * the response is returned at ConnectionHandler.onGetPeerDevices().
     */
	public void getPeerDevices() {
		Log.d(TAG, "bef wait for Conn service");
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.GET_CONNECTED_PEERS;
			sentMsgBuf.add(m);
			return;
		}
		try {
			Log.d(TAG, "start getPeerDevices()");
			mConnService.getPeerDevices(sessionId);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to getPeerDevices: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

    /**
     * retrieve info of attached networks;
     * the response is returned at ConnectionHandler.onGetNetworks().
     */
	public void getNetworks() {
		Log.d(TAG, "getNetworks bef wait for Conn service");
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.GET_NETWORKS;
			sentMsgBuf.add(m);
			return;
		}
		try {
			Log.d(TAG, "start getNetworks()");
			mConnService.getNetworks(sessionId);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to getNetworks: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
		
	}

    /**
     * request info of the network currently used to connect peer devices.
     * the response is at ConnectionHandler.onGetActiveNetwork().
     */
	public void getActiveNetwork() {
		Log.d(TAG, "getActiveNetwork bef wait for Conn service");
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.GET_ACTIVE_NETWORK;
			sentMsgBuf.add(m);
			return;
		}
		try {
			Log.d(TAG, "start getActiveNetwork()");
			mConnService.getActiveNetwork(sessionId);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to getActiveNetwork: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
		
	}

    /**
     * choose network as the network used to connect peer devices hereafter.
     *
     * @param net info of the network to be activated.
     */
	public void activateNetwork(NetInfo net) {
		Log.d(TAG, "activateNetwork bef wait for Conn service");
		if (mConnService == null) {
			Message m = Message.obtain();
			m.what = Router.MsgId.ACTIVATE_NETWORK;
			m.obj = net;
			sentMsgBuf.add(m);
			return;
		}
		try {
			Log.d(TAG, "start activateNetwork()");
			mConnService.activateNetwork(sessionId, net);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to activateNetwork: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

}
