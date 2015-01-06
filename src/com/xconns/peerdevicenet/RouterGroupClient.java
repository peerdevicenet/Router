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

import com.xconns.peerdevicenet.IRouterGroupHandler;
import com.xconns.peerdevicenet.IRouterGroupService;

/**
 * RouterGroupClient wrapper class is the preferred way to access the asynchronous one-way AIDL api of Router Group Service.
 * It enables two-way asynchronous messaging between clients and GroupService.
 * <ul>
 *     <li>bind to (later unbind) Router Group Service and automatically join the group named in constructor.</li>
 *     <li>expose api methods to invoke GroupService one-way asynchronous api
 *     as defined by IRouterGroupService.aidl; perform operations
 *     such as sending a message.</li>
 *     <li>register a GroupHandler which exposes client side one-way asynchronous messaging api
 *     to allow GroupService call back to notify events such as peer device leaving group, or receiving a message</li>
 *     <li>buffering api calls when the binding with GroupService is not ready yet,
 *     and resend buffered calls when the binding is ready.</li>
 * </ul>
 * <p>
 * A sample scenario of using RouterGroupClient to interact with GroupService is as following:
 * <ol>
 * <li> Initialization at onCreate() or onResume():
 <pre>
 //bind to GroupService, join the group named by "groupId" and register GroupHandler 
 //as client side async messaging api for GroupService to call back.
 mGroupClient = new RouterGroupClient(this, groupId, null, mGroupHandler);
 mGroupClient.bindService(); 
 </pre>
 * <li> call service async api to perform operations:
 <pre>
 //send a message to peers.
 RotateMsg m = new RotateMsg(RotateMsg.INIT_ORIENT_REQ, 0, 0); //req init orientation
 mGroupClient.send(null, m.marshall());
 </pre>
 * <li> cleanup at onPause() or onDestroy():
 <pre>
 //leave group and unbind from GroupService
 mGroupClient.unbindService();
 </pre>
 * </ol>
 * <p>
 * For detailed tutorial on how to use RouterGroupClient to talk to GroupService and send/receive messages to peers,
 * please check out <a href="http://github.com/peerdevicenet/sample_Rotate">github sample project</a>.
 */
public class RouterGroupClient {
	// Debugging
	private static final String TAG = "RouterGroupClient";

    /**
     * GroupHandler exposes client side async messaging one-way AIDL api to allow GroupService notify
     * events such as peer devices joining group, peer devices leaving a group or receiving a message.
     */
	public interface GroupHandler {
        /**
         * Notify clients about two kinds of errors:
         * <ul>
         * <li> errors happening at server processing.
         * <li> errors happening at client invoking GroupService api.
         *</ul>
         * @param errInfo the detailed error message
         */
		void onError(String errInfo);

        /**
         * notify clients that it has joined the group successfully; and returning the info
         * of peer devices in this group.
         * @param peersInfo info of peer devices in this group.
         */
		void onSelfJoin(DeviceInfo[] peersInfo);

        /**
         * notify client that a peer device joined the group.
         *
         * @param peerInfo info of peer device which just joined.
         */
		void onPeerJoin(DeviceInfo peerInfo);

        /**
         * notify client that it finished leaving the group.
         */
		void onSelfLeave();

        /**
         * notify client that a peer device has left the group.
         *
         * @param peerInfo info of peer device which just left.
         */
		void onPeerLeave(DeviceInfo peerInfo);

        /**
         * notify client that a message arrived from a peer device.
         *
         * @param src info of device sending the message.
         * @param msg message received.
         */
		void onReceive(DeviceInfo src, byte[] msg);

        /**
         * Reply to client info of peer devices in group; this is the response to getPeerDevices() api call.
         *
         * @param devices
         */
		void onGetPeerDevices(DeviceInfo[] devices);
	}

	private Context context = null;
	private IRouterGroupService mGroupService = null;
	private String groupId = null;
	private DeviceInfo[] peers = null;
	private GroupHandler registeredHandler = null;
	private List<Message> sentMsgBuf = new ArrayList<Message>();

    /**
     * RouterGroupClient constructor; register a GroupHandler to expose client side one-way asynchronous messaging api for GroupService to call back.
     *
     * @param c context in which to bind to GroupService.
     * @param grp string name of group.
     * @param p
     * @param h register a GroupHandler to expose client side one-way asynchronous messaging api for GroupService to call back.
     */
	public RouterGroupClient(Context c, String grp, DeviceInfo[] p, GroupHandler h) {
		context = c;
		groupId = grp;
		peers = p;
		registeredHandler = h;
	}

	private IRouterGroupHandler mGroupHandler = new IRouterGroupHandler.Stub() {
		public void onError(String errInfo) {
			registeredHandler.onError(errInfo);
		}

		public void onSelfJoin(DeviceInfo[] peersInfo) throws RemoteException {
			registeredHandler.onSelfJoin(peersInfo);
		}

		public void onPeerJoin(DeviceInfo peerInfo) throws RemoteException {
			registeredHandler.onPeerJoin(peerInfo);
		}

		public void onSelfLeave() throws RemoteException {
			registeredHandler.onSelfLeave();
		}

		public void onPeerLeave(DeviceInfo peerInfo) throws RemoteException {
			registeredHandler.onPeerLeave(peerInfo);
		}

		public void onReceive(DeviceInfo src, byte[] msg) throws RemoteException {
			registeredHandler.onReceive(src, msg);
		}

		public void onGetPeerDevices(DeviceInfo[] devices)
				throws RemoteException {
			registeredHandler.onGetPeerDevices(devices);
		}

	};

	/*
	 * Class for interacting with the main interface of the service.
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mGroupService = IRouterGroupService.Stub.asInterface(service);
			try {
				if (groupId != null && mGroupHandler != null)
					mGroupService.joinGroup(groupId, peers, mGroupHandler);
			} catch (RemoteException e) {
				Log.e(TAG, "failed at joinGroup: " + e.getMessage());
			}
			sendBufferedMsgs();
		}

		public void onServiceDisconnected(ComponentName className) {
			mGroupService = null;
		}
	};

    /**
     * bind to Router Group Service.
     */
	public void bindService() {
		Intent intent = new Intent("com.xconns.peerdevicenet.GroupService");
		context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

    /**
     * unbind from Router Group Service.
     */
	public void unbindService() {
		Log.e(TAG, "unbindService() called for "+groupId);
		if (mGroupService == null)
			return;
		try {
			mGroupService.leaveGroup(groupId, mGroupHandler);
		} catch (RemoteException e) {
			Log.e(TAG, "failed at leaveGroup: " + e.getMessage());
		}
		// Detach our existing connection.
		context.unbindService(mConnection);
	}

	void sendBufferedMsgs() {
		int sz = sentMsgBuf.size();
		if (sz > 0) {
			for (int i = 0; i < sz; i++) {
				try {
					Message m = (Message) sentMsgBuf.get(i);
					switch (m.what) {
					case Router.MsgId.SEND_MSG:
						Object[] data = (Object[])m.obj;
						mGroupService.send(groupId, (DeviceInfo)data[0], (byte[])data[1]);
						break;
					case Router.MsgId.GET_CONNECTED_PEERS:
						mGroupService.getPeerDevices(groupId);
						break;
					default:
						break;
					}
				} catch (RemoteException e) {
					Log.e(TAG + ":" + groupId,
							"failed to send msg: " + e.getMessage());
				}
			}
			sentMsgBuf.clear();
		}
	}

    /**
     * send a message to all peers in a group, or to a specific device.
     *
     * @param dest the target device to send message; if set to null, the message is broadcast to all peers in group.
     * @param msg the message to send.
     */
	public void send(DeviceInfo dest, byte[] msg) {
		// send my msg
		if (mGroupService == null) {
			Message m = Message.obtain(null, Router.MsgId.SEND_MSG);
			Object[] data = new Object[2];
			data[0] = dest;
			data[1] = msg;
			m.obj = data;
			sentMsgBuf.add(m);
			return;
		}
		try {
			mGroupService.send(groupId, dest, msg);
		} catch (RemoteException e) {
			Log.e(TAG + ":" + groupId, "failed to send msg: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}

    /**
     * request the info of peer devices in this group;
     * the response is returned at GroupHandler.onGetPeerDevices().
     */
	public void getPeerDevices() {
		if (mGroupService == null) {
			Message m = Message.obtain(null, Router.MsgId.GET_CONNECTED_PEERS);
			sentMsgBuf.add(m);
			return;
		}
		try {
			mGroupService.getPeerDevices(groupId);
		} catch (RemoteException e) {
			Log.e(TAG, "failed to getPeerDevices: " + e.getMessage());
            registeredHandler.onError(e.getMessage());
		}
	}
}
