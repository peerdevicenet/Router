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

/**
 * Messaging api for clients to communicate with Router services using intents
 * and messenger. define the following:
 * <ul>
 * <li>intent action names for Router services and its life-cyle: Startup,
 * Reset, Shutdown.
 * <li>intent action names for communication with Connection Services and Group
 * Services.
 * <li>message ids or tags (corresponding to intent action names) for using
 * Messenger messages to communicate with Router ConnectionService and GroupService.
 * <li>standard key names for setting/retrieving message content in message
 * bundle.
 * </ul>
 */
public class Router {
    /**
     * Intenting api allows clients communicate with Router services using intents;
     * clients will send intents/messages to Router services by calling startService() and
     * received intents/messages from Router services by registering BroadcastReceivers.
     * All application message data are passed as
     * "extra" data items in intents and all extra data key names are defined in {@link com.xconns.peerdevicenet.Router.MsgKey Router.MsgKey}.
     * <p>
     * the api includes the following groups of intent actions:
     * <ol>
     * <li> action names for starting and binding to Router services (ConnectionService, GroupService, MessengerService):
     * ACTION_SERVICE, ACTION_CONNECTION_SERVICE, ACTION_GROUP_SERVICE, ACTION_MESSENGER_SERVICE.
     * <li> action names to control and notify event about their life cycles:
     * <ol type="A">
     * <li> commands: ACTION_ROUTER_STARTUP, ACTION_ROUTER_RESET, ACTION_ROUTER_SHUTDOWN
     * <li> events: ACTION_ROUTER_UP, ACTION_ROUTER_CLEAR, ACTION_ROUTER_DOWN
     * </ol>
     * <li> action names for starting Connector or ConnectionManagement apps which interact with ConnectionService:
     * ACTION_CONNECTOR, ACTION_CONNECTION_MANAGEMENT, ACTION_CONNECTION_SETTINGS
     * <li> action names for communicating with ConnectionService:
     * <ol type="A">
     * <li> network detection, connection and disconnection: ACTION_CONNECT_NETWORK, ACTION_DISCONNECT_NETWORK, ACTION_GET_NETWORKS, ACTION_GET_ACTIVE_NETWORK, ACTION_ACTIVATE_NETWORK, ACTION_NETWORK_CONNECTING, ACTION_NETWORK_CONNECTED, ACTION_NETWORK_DISCONNECTED, ACTION_NETWORK_CONNECTION_FAILED.
     * <li> peer discovery: ACTION_START_SEARCH, ACTION_SEARCH_FOUND_DEVICE, ACTION_SEARCH_COMPLETE.
     * <li> peer device connection: ACTION_CONNECT, ACTION_DISCONNECT, ACTION_ACCEPT_CONNECTION, ACTION_DENY_CONNECTION, ACTION_CONNECTING, ACTION_CONNECTION_FAILED, ACTION_CONNECTED, ACTION_DISCONNECTED.
     * <li> info retrieval: ACTION_SET_CONNECTION_INFO, ACTION_GET_CONNECTION_INFO, ACTION_GET_DEVICE_INFO
     * </ol>
     * <li> action names for communicating with GroupService:
     * <ol type="A">
     * <li>group membership: ACTION_JOIN_GROUP, ACTION_LEAVE_GROUP, ACTION_SELF_JOIN, ACTION_PEER_JOIN, ACTION_SELF_LEAVE, ACTION_PEER_LEAVE</li>
     * <li>message passing: ACTION_SEND_MSG, ACTION_RECV_MSG</li>
     * <li>info retrieval: ACTION_GET_CONNECTED_PEERS</li>
     * </ol>
     * </ol>
     * <p>
     * A sample scenario of using intenting api to interact with GroupService consists of the following:
     * <ol>
     * <li> initialization at onCreate() or onResume():
     <pre>
     //first we register to handle broadcast intents for receiving
     //messages and events from Router services:
     IntentFilter filter = new IntentFilter();
     filter.addAction(Router.ACTION_RECV_MSG);
     filter.addAction(Router.ACTION_PEER_JOIN);
     ......
     registerReceiver(mReceiver, filter);
     //then send intents to Router services to do initialization:
     Intent intent = new Intent(Router.ACTION_JOIN_GROUP);
     intent.putExtra(Router.GROUP_ID, groupId);
     startService(intent);
     </pre>
     * <li> send intents to Router services to perform operations:
     * <pre>
     Intent intent = new Intent(Router.ACTION_SEND_MSG);
     intent.putExtra(Router.GROUP_ID, groupId);
     intent.putExtra(Router.MSG_DATA, msg.getBytes());
     startService(intent);
     * </pre>
     * <li> cleanup at onPause() or onDestroy():
     * <pre>
     //first we send intent to Router services to inform we are leaving:
     Intent intent = new Intent(Router.ACTION_LEAVE_GROUP);
     intent.putExtra(Router.GROUP_ID, groupId);
     startService(intent);
     //at last unregister BroadcastReceiver and stop receiving messages and events from Router services.
     unregisterReceiver(mReceiver);
     * </pre>
     * </ol>
     * <p>
     * For detailed tutorial on how to use intents to talk to ConnectionService and set up peer device
     * connections, please check out <a href="http://github.com/peerdevicenet/sample_Connector_wif_intent">the following github sample project</a>.
     * <p>
     * For detailed tutorial on how to use intents talk to GroupService and send/receive messages to peers,
     * please check out <a href="http://github.com/peerdevicenet/sample_Chat">the github sample project</a>.
     */
    public final static class Intent {
        // service startup intents
        // for starting router service
    	/**
    	 * generic intent action name for Router services
    	 */
        public static final String ACTION_SERVICE = "com.xconns.peerdevicenet.Service";
        /**
         * intent action name to allow client bind to ConnectionService.
         */
        public static final String ACTION_CONNECTION_SERVICE = "com.xconns.peerdevicenet.ConnectionService";
        /**
         * intent action name to allow client bind to GroupService.
         */
        public static final String ACTION_GROUP_SERVICE = "com.xconns.peerdevicenet.GroupService";
        /**
         * intent action name to allow clients bind to Messenger service.
         */
        public static final String ACTION_MESSENGER_SERVICE = "com.xconns.peerdevicenet.Messenger";
        // for starting conn mgr service
        /**
         * intent action name to bring up external Connector apps.
         */
        public static final String ACTION_CONNECTOR = "com.xconns.peerdevicenet.CONNECTOR";
        /**
         * intent action name to bring up external ConnectionManager apps.
         */
        public static final String ACTION_CONNECTION_MANAGEMENT = "com.xconns.peerdevicenet.CONNECTION_MANAGEMENT";
        /**
         * intent action name to bring up external ConnectionSettings apps.
         */
        public static final String ACTION_CONNECTION_SETTINGS = "com.xconns.peerdevicenet.CONNECTION_SETTINGS";
        /**
         * intent action name to bind to remote intent service
         */
        public static final String ACTION_REMOTE_INTENT_SERVICE = "com.xconns.peerdevicenet.RemoteIntentService";

        // life-cycle
        // cmds for router to run
        /**
         * intent action name for starting up Router services.
         */
        public static final String ACTION_ROUTER_STARTUP = "com.xconns.peerdevicenet.Startup";
        /**
         * intent action name to request a Router reset; all active peer connection will be disconnected;
         * other components in system can watch for this intent and do their cleanup accordingly.
         */
        public static final String ACTION_ROUTER_RESET = "com.xconns.peerdevicenet.Reset";
        /**
         * intent action name to request Router shutdown.
         */
        public static final String ACTION_ROUTER_SHUTDOWN = "com.xconns.peerdevicenet.Shutdown";
        // status change event from router
        /**
         * notify other components in system that Router services is up and ready;
         * they can start their startup process.
         */
        public static final String ACTION_ROUTER_UP = "com.xconns.peerdevicenet.Up";
        /**
         * notify other components that Router services are reset.
         */
        public static final String ACTION_ROUTER_CLEAR = "com.xconns.peerdevicenet.Clear";
        /**
         * notify other components that Router service shuts down.
         */
        public static final String ACTION_ROUTER_DOWN = "com.xconns.peerdevicenet.Down";

        // PeerDeviceNet intent actions
        /**
         * intent action name for Router to return error messages to clients.
         *
         * <p>Message Data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>MSG_DATA string</dt><dd>error message string.</dd>
         * </dl>
         */
        public static final String ACTION_ERROR = "com.xconns.peerdevicenet.ERROR";

        // ConnectionService intent actions
        /**
         * intent action name for connecting to a network
         *
         * <p>Reply Message Data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final String ACTION_CONNECT_NETWORK = "com.xconns.peerdevicenet.CONNECT_NETWORK";
        /**
         * intent action name for disconnecting a network
         *
         * <p>Reply Message Data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final String ACTION_DISCONNECT_NETWORK = "com.xconns.peerdevicenet.DISCONNECT_NETWORK";
        /**
         * intent action name for request and reply info of networks
         * currently attached to device.
         *
         * <p>Reply Message Data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPES integer array</dt><dd>network types.</dd>
         * <dt>NET_NAMES string array</dt><dd>network names.</dd>
         * <dt>NET_PASSES string array</dt><dd>network passwds.</dd>
         * <dt>NET_INFOS binary array</dt><dd>network infos.</dd>
         * <dt>NET_INTF_NAMES string array</dt><dd>network interface names.</dd>
         * <dt>NET_ADDRS string array</dt><dd>network addresses.</dd>
         * </dl>
         */
        public static final String ACTION_GET_NETWORKS = "com.xconns.peerdevicenet.GET_NETWORKS";
        /**
         * intent action name for request and reply info of the network
         * currently used to discover and connect peer devices.
         *
         * <p>Reply message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final String ACTION_GET_ACTIVE_NETWORK = "com.xconns.peerdevicenet.GET_ACTIVE_NETWORK";
        /**
         * intent action name for request and reply to activate
         * a network for use to discover and connect peer devices.
         *
         * <p>Request and reply message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         *
         */
        public static final String ACTION_ACTIVATE_NETWORK = "com.xconns.peerdevicenet.ACTIVATE_NETWORK";
        /**
         * intent action name to notify clients device is attached to
         * a new network.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final String ACTION_NETWORK_CONNECTED = "com.xconns.peerdevicenet.NETWORK_CONNECTED";
        /**
         * intent action name to notify clients device is detached from
         * a network.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final String ACTION_NETWORK_DISCONNECTED = "com.xconns.peerdevicenet.NETWORK_DISCONNECTED";
        /**
         * intent action name to notify clients that is connecting to a network
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final String ACTION_NETWORK_CONNECTING = "com.xconns.peerdevicenet.NETWORK_CONNECTING";
        /**
         * intent action name to notify client that a connect attempt to network failed
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final String ACTION_NETWORK_CONNECTION_FAILED = "com.xconns.peerdevicenet.NETWORK_CONNECTION_FAILED";
        /**
         * intent action name for request to start a new peer search session.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>SEARCH_TIMEOUT seconds</dt><dd>search session timeout.</dd>
         * <dt>PEER_NAME string</dt><dd>search leader device name (can be missing or null).</dd>
         * <dt>PEER_ADDR string</dt><dd>search leader device address (can be missing or null).</dd>
         * <dt>PEER_PORT string</dt><dd>search leader device port (can be missing or null).</dd>
         * </dl>
         *
         */
        public static final String ACTION_START_SEARCH = "com.xconns.peerdevicenet.START_SEARCH";
        /**
         * intent action name for request to stop current active peer search session.
         */
        public static final String ACTION_STOP_SEARCH = "com.xconns.peerdevicenet.STOP_SEARCH";
        /**
         * intent action name to notify client the search session started.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>search leader device name (can be missing or null).</dd>
         * <dt>PEER_ADDR string</dt><dd>search leader device address (can be missing or null).</dd>
         * <dt>PEER_PORT string</dt><dd>search leader device port (can be missing or null).</dd>
         * </dl>
         */
        public static final String ACTION_SEARCH_START = "com.xconns.peerdevicenet.SEARCH_START";
        /**
         * intent action name to notify client that new device found
         * during search.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>found device name (can be missing or null).</dd>
         * <dt>PEER_ADDR string</dt><dd>found device address (can be missing or null).</dd>
         * <dt>PEER_PORT string</dt><dd>found device port (can be missing or null).</dd>
         * <dt>USE_SSL boolean</dt><dd>does the found device use SSL for conenction?</dd>
         * </dl>
         */
        public static final String ACTION_SEARCH_FOUND_DEVICE = "com.xconns.peerdevicenet.SEARCH_FOUND_DEVICE";
        /**
         * intent action name to notify clients that search session completed, either because search session time out or client stoped search explicitly.
         */
        public static final String ACTION_SEARCH_COMPLETE = "com.xconns.peerdevicenet.SEARCH_COMPLETE";
        /**
         * intent action name for request to connect to a peer device.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * <dt>AUTHENTICATION_TOKEN binary</dt><dd>authentication data for peer to verify connection.</dd>
         * <dt>CONNECT_TIMEOUT integer</dt><dd>connection timeout.</dd>
         * </dl>
         */
        public static final String ACTION_CONNECT = "com.xconns.peerdevicenet.CONNECT";
        /**
         * intent action name for request to disconnect a peer device.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         */
        public static final String ACTION_DISCONNECT = "com.xconns.peerdevicenet.DISCONNECT";
        /**
         * intent action name for accepting a peer's connection request.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         */
        public static final String ACTION_ACCEPT_CONNECTION = "com.xconns.peerdevicenet.ACCEPT_CONNECTION";
        /**
         * intent action name for denying peer's connection request.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * <dt>CONN_DENY_CODE integer</dt><dd>{@link com.xconns.peerdevicenet.Router.ConnFailureCode Connection Failure code} for the reason of denial.</dd>
         * </dl>
         */
        public static final String ACTION_DENY_CONNECTION = "com.xconns.peerdevicenet.DENY_CONNECTION";
        /**
         * intent action name to notify client that a peer device is trying
         * to connect.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * <dt>AUTHENTICATION_TOKEN binary</dt><dd>authentication data from peer for verification.</dd>
         * </dl>
         */
        public static final String ACTION_CONNECTING = "com.xconns.peerdevicenet.CONNECTING";
        /**
         * intent action name to notify client that connection to peer failed.

         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * <dt>CONN_DENY_CODE integer</dt><dd>{@link com.xconns.peerdevicenet.Router.ConnFailureCode Connection Failure code} for the reason of denial.</dd>
         * </dl>
         */
        public static final String ACTION_CONNECTION_FAILED = "com.xconns.peerdevicenet.CONNECTION_FAILED";
        /**
         * intent action name to notify client that a peer connected.

         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         */
        public static final String ACTION_CONNECTED = "com.xconns.peerdevicenet.CONNECTED";
        /**
         * intent action name to notify client that a peer disconnected.

         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         */
        public static final String ACTION_DISCONNECTED = "com.xconns.peerdevicenet.DISCONNECTED";
        /**
         * intent action names for request and reply of setting connection related info in ConnectionService.
         *
         * <p>Request message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>DEVICE_NAME string</dt><dd>my device name.</dd>
         *     <dt>LIVENESS_TIMEOUT seconds</dt><dd>the period between successive checking of peers' aliveness.</dd>
         *     <dt>CONNECT_TIMEOUT seconds</dt><dd>peer device connection timeout.</dd>
         *     <dt>SEARCH_TIMEOUT seconds</dt><dd>peer search session timeout.</dd>
         *     <dt>USE_SSL boolean</dt><dd>does my connections to peers use SSL?</dd>
         * </dl>
         */
        public static final String ACTION_SET_CONNECTION_INFO = "com.xconns.peerdevicenet.SET_CONNECTION_INFO";
        /**
         * intent action names for request and reply of connection related info at ConnectionService.
         *
         * <p>Reply message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>DEVICE_NAME string</dt><dd>my device name.</dd>
         *     <dt>LIVENESS_TIMEOUT seconds</dt><dd>the period between successive checking of peers' aliveness.</dd>
         *     <dt>CONNECT_TIMEOUT seconds</dt><dd>peer device connection timeout.</dd>
         *     <dt>SEARCH_TIMEOUT seconds</dt><dd>peer search session timeout.</dd>
         *     <dt>USE_SSL boolean</dt><dd>does my connections to peers use SSL?</dd>
         * </dl>
         */
        public static final String ACTION_GET_CONNECTION_INFO = "com.xconns.peerdevicenet.GET_CONNECTION_INFO";
        /**
         * intent action names for request and reply of my device info at ConnectionService.
         *
         * <p>Reply message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         */
        public static final String ACTION_GET_DEVICE_INFO = "com.xconns.peerdevicenet.GET_DEVICE_INFO";

        // GroupService intent actions
        /**
         * intent action name for request to join a group
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group to join.</dd>
        * </dl>
         */
        public static final String ACTION_JOIN_GROUP = "com.xconns.peerdevicenet.JOIN_GROUP";
        /**
         * intent action name for request to leave a group
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group to leave.</dd>
         * </dl>
         */
        public static final String ACTION_LEAVE_GROUP = "com.xconns.peerdevicenet.LEAVE_GROUP";
        /**
         * intent action name to notify client it has successfully joined a group.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group joined.</dd>
         * <dt>PEER_NAMES string array</dt><dd>peer device names.</dd>
         * <dt>PEER_ADDRS string array</dt><dd>peer device addresses.</dd>
         * <dt>PEER_PORTS string array</dt><dd>peer device ports.</dd>
         * </dl>
         */
        public static final String ACTION_SELF_JOIN = "com.xconns.peerdevicenet.SELF_JOIN";
        /**
         * intent action name to notify client a peer device joined group.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group joined.</dd>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         *
         */
        public static final String ACTION_PEER_JOIN = "com.xconns.peerdevicenet.PEER_JOIN";
        /**
         * intent action name to notify client it has left a group.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group left.</dd>
         * </dl>
         */
        public static final String ACTION_SELF_LEAVE = "com.xconns.peerdevicenet.SELF_LEAVE";
        /**
         * intent action name to notify client a peer device left group.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group joined.</dd>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         *
         */
        public static final String ACTION_PEER_LEAVE = "com.xconns.peerdevicenet.PEER_LEAVE";
        /**
         * intent action name to send a message to peers.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>destination device name (can be missing or null for broadcast).</dd>
         * <dt>PEER_ADDR string</dt><dd>destination device address (can be missing or null for broadcast).</dd>
         * <dt>PEER_PORT string</dt><dd>destination device port (can be missing or null for broadcast).</dd>
         *     <dt>GROUP_ID string</dt><dd>name of the group for broadcast message</dd>
         *     <dt>MSG_ID string</dt><dd>message id</dd>
         *     <dt>MSG_DATA string</dt><dd>message data</dd>
         * </dl>
         */
        public static final String ACTION_SEND_MSG = "com.xconns.peerdevicenet.SEND_MSG";
        /**
         * intent action name to notify client to receive a message from peers.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>source device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>source device address.</dd>
         * <dt>PEER_PORT string</dt><dd>source device port.</dd>
         *     <dt>GROUP_ID string</dt><dd>name of the group for broadcast message</dd>
         *     <dt>MSG_ID string</dt><dd>message id</dd>
         *     <dt>MSG_DATA string</dt><dd>message data</dd>
         * </dl>
         */
        public static final String ACTION_RECV_MSG = "com.xconns.peerdevicenet.RECV_MSG";
        /**
         * intent action name for request and reply to get connected peer device info.
         *
         * <p>Request message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group to request peers info (can be missing or null if request all peer device info).</dd>
         * </dl>
         *
         * <p>Reply message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group to request info (can be missing or null if request all peer info).</dd>
         * <dt>PEER_NAMES string array</dt><dd>peer device names.</dd>
         * <dt>PEER_ADDRS string array</dt><dd>peer device addresses.</dd>
         * <dt>PEER_PORTS string array</dt><dd>peer device ports.</dd>
         * </dl>
         *
         */
        public static final String ACTION_GET_CONNECTED_PEERS = "com.xconns.peerdevicenet.GET_CONNECTED_PEERS";

        // remote intent related
        public static final String ACTION_START_REMOTE_ACTIVITY = "com.xconns.peerdevicenet.START_REMOTE_ACTIVITY";
        public static final String ACTION_START_REMOTE_ACTIVITY_FOR_RESULT = "com.xconns.peerdevicenet.START_REMOTE_ACTIVITY_FOR_RESULT";
        public static final String ACTION_START_REMOTE_SERVICE = "com.xconns.peerdevicenet.START_REMOTE_SERVICE";
        public static final String ACTION_SEND_REMOTE_BROADCAST = "com.xconns.peerdevicenet.SEND_REMOTE_BROADCAST";
    }

    /**
	 * Messenger api allows clients communicate with Router services using
	 * "Messenger" design pattern with messages identified by message ids;
	 * clients will use a messenger to send messages to Router services and
	 * register another messenger to receive messages from Router services;
	 * there is 1-1 correspondence between intent names and message ids.
	 * All message data are passed as a bundle with data items indexed by keys. 
     * The message data keys is defined at 
     * {@link com.xconns.peerdevicenet.Router.MsgKey Router.MsgKey} class.
	 * <p>
	 * include the following groups of message ids:
	 * <ol>
	 * <li>message ids for communicating with ConnectionService:
	 * <ol type="A">
	 * <li>network detection, connection and disconnection: GET_NETWORKS, GET_ACTIVE_NETWORK,
	 * ACTIVATE_NETWORK, CONNECT_NETWORK, DISCONNECT_NETWORK, NETWORK_CONNECTING, NETWORK_CONNECTED, NETWORK_DISCONNECTED, NETWORK_CONNECTION_FAILED.
	 * <li>peer discovery: START_SEARCH, SEARCH_FOUND_DEVICE, SEARCH_COMPLETE.
	 * <li>peer device connection: CONNECT, DISCONNECT, ACCEPT_CONNECTION,
	 * DENY_CONNECTION, CONNECTING, CONNECTION_FAILED, CONNECTED, DISCONNECTED.
	 * <li>info retrieval: SET_CONNECTION_INFO, GET_CONNECTION_INFO,
	 * GET_DEVICE_INFO
	 * </ol>
	 * <li>message ids for communicating with GroupService:
	 * <ol type="A">
	 * <li>group membership: JOIN_GROUP, LEAVE_GROUP, SELF_JOIN, PEER_JOIN,
	 * SELF_LEAVE, PEER_LEAVE</li>
	 * <li>message passing: SEND_MSG, RECV_MSG</li>
	 * <li>info retrieval: GET_CONNECTED_PEERS</li>
	 * </ol>
	 * </ol>
	 * <p>
	 * A sample scenario of using Messenger api to interact with GroupService is
	 * as following:
	 * <ol>
	 * <li>Initialization at onCreate() or onResume():
	 * 
	 * <pre>
     //first bind to messenger service:
     Intent intent = new Intent("com.xconns.peerdevicenet.Messenger");
     bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
     ......
     //then at onServiceConnected(): register a receiver messenger to receive messages:
     Message msg = Message.obtain(null, Router.MsgId.REGISTER_RECEIVER);
     msg.replyTo = mMessenger;
     mService.send(msg);
	 </pre>
	 * 
	 * <li>use messenger to send messages to Router services to perform
	 * operations:
	 * 
	 <pre>
     Message msg = Message.obtain(null, Router.MsgId.SEND_MSG, 0, 0);
     Bundle b = new Bundle();
     b.putByteArray(Router.MSG_DATA, msg_data.getBytes());
     b.putString(Router.GROUP_ID, groupId);
     msg.setData(b);
     mService.send(msg);
	 </pre>
	 * 
	 * <li>cleanup at onDestroy() or onPause():
	 * 
	 <pre>
     // first tell Router services we are leaving:
     Message msg = Message.obtain(null, Router.MsgId.LEAVE_GROUP);
     msg.setData(b);
     mService.send(msg);
     // then unregister receiver messenger:
     msg = Message.obtain(null, Router.MsgId.UNREGISTER_RECEIVER);
     msg.replyTo = mMessenger;
     mService.send(msg);
     // finally unbind messenger service
     unbindService(mConnection);
	 </pre>
	 * 
	 * </ol>
	 * <p>
	 * For detailed tutorial on how to use messengers talk to GroupService and
	 * send/receive messages to peers, please check out <a
	 * href="http://github.com/peerdevicenet/sample_Chat">the github sample
	 * project.</a>
	 */
    public final static class MsgId {
        // connection msgs
        /**
         * message id for Router to return error messages to clients.
         *
         * <p>Message Data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>MSG_DATA string</dt><dd>error message string.</dd>
         * </dl>
         */
        public final static int ERROR = -10100;

        // cmds
        /**
         * message id for request to start a new peer search session.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>SEARCH_TIMEOUT seconds</dt><dd>search session timeout.</dd>
         * <dt>PEER_NAME string</dt><dd>search leader device name (can be missing or null).</dd>
         * <dt>PEER_ADDR string</dt><dd>search leader device address (can be missing or null).</dd>
         * <dt>PEER_PORT string</dt><dd>search leader device port (can be missing or null).</dd>
         * </dl>
         *
         */
        public final static int START_SEARCH = -10200;
        /**
         * message id for request to stop current active peer search session.
         */
        public final static int STOP_SEARCH = -10201;
        // state change
        /**
         * message id to notify client the search session started.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>search leader device name (can be missing or null).</dd>
         * <dt>PEER_ADDR string</dt><dd>search leader device address (can be missing or null).</dd>
         * <dt>PEER_PORT string</dt><dd>search leader device port (can be missing or null).</dd>
         * </dl>
         */
        public final static int SEARCH_START = -10210;
        /**
         * message id to notify client that new device found
         * during search.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>found device name (can be missing or null).</dd>
         * <dt>PEER_ADDR string</dt><dd>found device address (can be missing or null).</dd>
         * <dt>PEER_PORT string</dt><dd>found device port (can be missing or null).</dd>
         * <dt>USE_SSL boolean</dt><dd>does the found device use SSL for conenction?</dd>
         * </dl>
         */
        public final static int SEARCH_FOUND_DEVICE = -10211;
        /**
         * intent action name to notify clients that search session completed, either because search session time out or client stoped search explicitly.
         */
        public final static int SEARCH_COMPLETE = -10212;

        // cmds
        /**
         * message id for request to connect to a peer device.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * <dt>AUTHENTICATION_TOKEN binary</dt><dd>authentication data for peer to verify connection.</dd>
         * <dt>CONNECT_TIMEOUT integer</dt><dd>connection timeout.</dd>
         * </dl>
         */
        public final static int CONNECT = -10300;
        /**
         * message id for request to disconnect a peer device.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         */
        public final static int DISCONNECT = -10301;
        /**
         * message id for accepting a peer's connection request.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         */
        public final static int ACCEPT_CONNECTION = -10302;
        /**
         * message id for denying peer's connection request.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * <dt>CONN_DENY_CODE integer</dt><dd>{@link com.xconns.peerdevicenet.Router.ConnFailureCode Connection Failure code} for the reason of denial.</dd>
         * </dl>
         */
        public final static int DENY_CONNECTION = -10303;
        // state change
        /**
         * message id to notify client that a peer device is trying
         * to connect.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * <dt>AUTHENTICATION_TOKEN binary</dt><dd>authentication data from peer for verification.</dd>
         * </dl>
         */
        public final static int CONNECTING = -10310;
        /**
         * message id to notify client that connection to peer failed.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * <dt>CONN_DENY_CODE integer</dt><dd>{@link com.xconns.peerdevicenet.Router.ConnFailureCode Connection Failure code} for the reason of denial.</dd>
         * </dl>
         */
        public final static int CONNECTION_FAILED = -10311;
        /**
         * message id to notify client that a peer connected.

         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         */
        public final static int CONNECTED = -10312;
        /**
         * message id to notify client that a peer disconnected.

         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         */
        public final static int DISCONNECTED = -10313;

        // cmds
        /**
         * message id for request to join a group
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group to join.</dd>
         * </dl>
         */
        public final static int JOIN_GROUP = -10400;
        /**
         * message id for request to leave a group
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group to leave.</dd>
         * </dl>
         */
        public final static int LEAVE_GROUP = -10401;
        // state change
        /**
         * message id to notify client it has successfully joined a group.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group joined.</dd>
         * <dt>PEER_NAMES string array</dt><dd>peer device names.</dd>
         * <dt>PEER_ADDRS string array</dt><dd>peer device addresses.</dd>
         * <dt>PEER_PORTS string array</dt><dd>peer device ports.</dd>
         * </dl>
         */
        public final static int SELF_JOIN = -10410;
        /**
         * message id to notify client a peer device joined group.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group joined.</dd>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         *
         */
        public final static int PEER_JOIN = -10411;
        /**
         * message id to notify client it has left a group.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group left.</dd>
         * </dl>
         */
        public final static int SELF_LEAVE = -10412;
        /**
         * message id to notify client a peer device left group.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group joined.</dd>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         *
         */
        public final static int PEER_LEAVE = -10413;

        //
        /**
         * message id to send a message to peers.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>destination device name (can be missing or null for broadcast).</dd>
         * <dt>PEER_ADDR string</dt><dd>destination device address (can be missing or null for broadcast).</dd>
         * <dt>PEER_PORT string</dt><dd>destination device port (can be missing or null for broadcast).</dd>
         *     <dt>GROUP_ID string</dt><dd>name of the group for broadcast message</dd>
         *     <dt>MSG_ID string</dt><dd>message id</dd>
         *     <dt>MSG_DATA string</dt><dd>message data</dd>
         * </dl>
         */
        public final static int SEND_MSG = -10500;
        /**
         * message id to notify client to receive a message from peers.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>source device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>source device address.</dd>
         * <dt>PEER_PORT string</dt><dd>source device port.</dd>
         *     <dt>GROUP_ID string</dt><dd>name of the group for broadcast message</dd>
         *     <dt>MSG_ID string</dt><dd>message id</dd>
         *     <dt>MSG_DATA string</dt><dd>message data</dd>
         * </dl>
         */
        public final static int RECV_MSG = -10510;

        //
        /**
         * message id for request and reply of setting connection related info in ConnectionService.
         *
         * <p>Request message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>DEVICE_NAME string</dt><dd>my device name.</dd>
         *     <dt>LIVENESS_TIMEOUT seconds</dt><dd>the period between successive checking of peers' aliveness.</dd>
         *     <dt>CONNECT_TIMEOUT seconds</dt><dd>peer device connection timeout.</dd>
         *     <dt>SEARCH_TIMEOUT seconds</dt><dd>peer search session timeout.</dd>
         *     <dt>USE_SSL boolean</dt><dd>does my connections to peers use SSL?</dd>
         * </dl>
         * */
        public final static int SET_CONNECTION_INFO = -10600;
        /**
         * message id for request and reply of connection related info at ConnectionService.
         *
         * <p>Reply message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>DEVICE_NAME string</dt><dd>my device name.</dd>
         *     <dt>LIVENESS_TIMEOUT seconds</dt><dd>the period between successive checking of peers' aliveness.</dd>
         *     <dt>CONNECT_TIMEOUT seconds</dt><dd>peer device connection timeout.</dd>
         *     <dt>SEARCH_TIMEOUT seconds</dt><dd>peer search session timeout.</dd>
         *     <dt>USE_SSL boolean</dt><dd>does my connections to peers use SSL?</dd>
         * </dl>
         */
        public final static int GET_CONNECTION_INFO = -10601;
        /**
         * message id for request and reply of my device info at ConnectionService.
         *
         * <p>Reply message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>PEER_NAME string</dt><dd>peer device name.</dd>
         * <dt>PEER_ADDR string</dt><dd>peer device address.</dd>
         * <dt>PEER_PORT string</dt><dd>peer device port.</dd>
         * </dl>
         */
        public final static int GET_DEVICE_INFO = -10602;
        /**
         * message id for request and reply to get connected peer device info.
         *
         * <p>Request message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group to request peers info (can be missing or null if request all peer device info).</dd>
         * </dl>
         *
         * <p>Reply message data is a bundle indexed by MsgKey:
         * <dl>
         *     <dt>GROUP_ID string</dt><dd>name of group to request info (can be missing or null if request all peer info).</dd>
         * <dt>PEER_NAMES string array</dt><dd>peer device names.</dd>
         * <dt>PEER_ADDRS string array</dt><dd>peer device addresses.</dd>
         * <dt>PEER_PORTS string array</dt><dd>peer device ports.</dd>
         * </dl>
         *
         */
        public final static int GET_CONNECTED_PEERS = -10603;

        //
        /**
         * message id for request and reply info of networks
         * currently attached to device.
         *
         * <p>Reply Message Data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPES integer array</dt><dd>network types.</dd>
         * <dt>NET_NAMES string array</dt><dd>network names.</dd>
         * <dt>NET_PASSES string array</dt><dd>network passwds.</dd>
         * <dt>NET_INFOS binary array</dt><dd>network infos.</dd>
         * <dt>NET_INTF_NAMES string array</dt><dd>network interface names.</dd>
         * <dt>NET_ADDRS string array</dt><dd>network addresses.</dd>
         * </dl>
         */
        public static final int GET_NETWORKS = 10700;
        /**
         * message id for request and reply info of the network
         * currently used to discover and connect peer devices.
         *
         * <p>Reply message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final int GET_ACTIVE_NETWORK = 10701;
        /**
         * message id for request and reply to activate
         * a network for use to discover and connect peer devices.
         *
         * <p>Request and reply message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         *
         */
        public static final int ACTIVATE_NETWORK = 10702;
        /**
         * message id to notify clients device is attached to
         * a new network.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final int NETWORK_CONNECTED = 10703;
        /**
         * message id to notify clients device is detached from
         * a network.
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final int NETWORK_DISCONNECTED = 10704;
        /**
         * message id to request connecting to a network
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final int CONNECT_NETWORK = 10705;
        /**
         * message id to request disconnecting from a network
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final int DISCONNECT_NETWORK = 10706;
        /**
         * message id to notify clients that a connection to network is in process
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final int NETWORK_CONNECTING = 10707;
        /**
         * message id to notify clients that connection to a network failed
         *
         * <p>Message data is a bundle indexed by MsgKey:
         * <dl>
         * <dt>NET_TYPE integer</dt><dd>network type.</dd>
         * <dt>NET_NAME string</dt><dd>network name.</dd>
         * <dt>NET_PASS string</dt><dd>network passwd.</dd>
         * <dt>NET_INFO binary</dt><dd>network info.</dd>
         * <dt>NET_INTF_NAME string</dt><dd>network interface name.</dd>
         * <dt>NET_ADDR string</dt><dd>network address.</dd>
         * </dl>
         */
        public static final int NETWORK_CONNECTION_FAILED = 10708;

        //
        /**
         * message id to register a receiver messenger.
         *
         * <p>Message data:
         * <dl>
         * <dt>replyTo</dt><dd>receiver messenger to be registered.</dd>
         * </dl>
         */
        public final static int REGISTER_RECEIVER = -10800;
        /**
         * message id to unregister a receiver messenger.
         *
         * <p>Message data:
         * <dl>
         * <dt>replyTo</dt><dd>receiver messenger to be unregistered.</dd>
         * </dl>
         */
        public final static int UNREGISTER_RECEIVER = -10801;

        //
        public final static int START_REMOTE_ACTIVITY = -10900;
        public final static int START_REMOTE_SERVICE = -10901;
        public final static int SEND_REMOTE_BROADCAST = -10902;
    }

    /**
     * Reasons why peer device connections failed.
     */
    public final static class ConnFailureCode {
        /**
         * Connection request rejected because target device is not in active
         * connection setup process (whose connection manager is not active).
         */
        public final static int FAIL_CONNMGR_INACTIVE = 1;
        /**
         * Connection request rejected because security token passed by
         * connecting peer is invalid.
         */
        public final static int FAIL_PIN_MISMATCH = 2;
        /**
         * Connection request rejected because same peer-peer connection has
         * been set up.
         */
        public final static int FAIL_CONN_EXIST = 3;
        /**
         * Connection request is explicitly rejected by user.
         */
        public final static int FAIL_REJECT_BY_USER = 4;
        /**
         * Connection request is rejected because connecting peer is not found
         * during search.
         */
        public final static int FAIL_UNKNOWN_PEER = 5;
        /**
         * Self connection request is rejected.
         */
        public final static int FAIL_CONN_SELF = 6;
        /**
         * Peer connection failed because wifi is lost.
         */
        public final static int FAIL_LOSE_WIFI = 7;
        /**
         * Connection is not responding.
         */
        public final static int FAIL_LOSE_CONNECTION = 8;
    }

    /**
     * key names for indexing data items in message bundle.
     *
     */
    public final static class MsgKey {
        public static final String MSG_ID = "MSG_ID";
        public static final String MSG_DATA = "MSG_DATA";
        public static final String PEER_NAME = "PEER_NAME";
        public static final String PEER_ADDR = "PEER_ADDR";
        public static final String PEER_PORT = "PEER_PORT";
        public static final String DEVICE_NAME = "DEVICE_NAME";
        public static final String DEVICE_ADDR = "DEVICE_ADDR";
        public static final String DEVICE_PORT = "DEVICE_PORT";
        // multicast groups; use string group_id to dispatch msgs to group peers
        public static final String GROUP_ID = "GROUP_ID";
        // id for data array
        public static final String PEER_NAMES = "PEER_NAMES";
        public static final String PEER_ADDRS = "PEER_ADDRS";
        public static final String PEER_PORTS = "PEER_PORTS";
        // keys for net info
        public static final String NET_TYPE = "NET_TYPE";
        public static final String NET_NAME = "NET_NAME";
        public static final String NET_ENCRYPT = "NET_ENCRYPT";
        public static final String NET_PASS = "NET_PASS";
        public static final String NET_HIDDEN = "NET_HIDDEN";
        public static final String NET_INFO = "NET_INFO";
        public static final String NET_INTF_NAME = "NET_INTF_NAME";
        public static final String NET_ADDR = "NET_ADDR";
        public static final String NET_INTF_MCAST = "NET_INTF_MCAST";

        public static final String NET_TYPES = "NET_TYPES";
        public static final String NET_NAMES = "NET_NAMES";
        public static final String NET_ENCRYPTS = "NET_ENCRYPTS";
        public static final String NET_PASSES = "NET_PASSES";
        public static final String NET_HIDDENS = "NET_HIDDENS";
        public static final String NET_INFOS = "NET_INFOS";
        public static final String NET_INTF_NAMES = "NET_INTF_NAMES";
        public static final String NET_ADDRS = "NET_ADDRS";

        //
        public static final String TIMEOUT = "TIMEOUT";
        public static final String LIVENESS_TIMEOUT = "LIVENESSTIMEOUT";
        public static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";
        public static final String SEARCH_TIMEOUT = "SEARCH_TIMEOUT";
        public static final String USE_SSL = "USE_SSL";
        public static final String AUTHENTICATION_TOKEN = "AUTHENTICATION_TOKEN";
        public static final String CONN_DENY_CODE = "CONNECTION_DENY_CODE";
        // remote intent bundle keys
        public static final String ACTION = "ACTION";
        public static final String TYPE = "TYPE";
        public static final String URI = "URI";
        public static final String URIS = "URIS";
        public static final String EXTRAS = "EXTRAS";
        public static final String REMOTE_INTENT = "REMOTE_INTENT";
        public static final String PACKAGE_NAME = "PACKAGE_NAME";
    }

    /**
     * translate message ids into string for printout
     */
    public static String MsgName(int msgId) {
        switch (msgId) {
            case MsgId.ERROR:
                return "ERROR";
            case MsgId.START_SEARCH:
                return "START_SEARCH";
            case MsgId.STOP_SEARCH:
                return "STOP_SEARCH";
            case MsgId.SEARCH_START:
                return "SEARCH_START";
            case MsgId.SEARCH_FOUND_DEVICE:
                return "SEARCH_FOUND_DEVICE";
            case MsgId.SEARCH_COMPLETE:
                return "SEARCH_COMPLETE";
            case MsgId.ACCEPT_CONNECTION:
                return "ACCEPT_CONNECTION";
            case MsgId.DENY_CONNECTION:
                return "DENY_CONNECTION";
            case MsgId.CONNECTING:
                return "CONNECTING";
            case MsgId.CONNECTION_FAILED:
                return "CONNECTION_FAILED";
            case MsgId.CONNECT:
                return "CONNECT";
            case MsgId.DISCONNECT:
                return "DISCONNECT";
            case MsgId.CONNECTED:
                return "CONNECTED";
            case MsgId.DISCONNECTED:
                return "DISCONNECTED";
            case MsgId.JOIN_GROUP:
                return "JOIN_GROUP";
            case MsgId.LEAVE_GROUP:
                return "LEAVE_GROUP";
            case MsgId.SELF_JOIN:
                return "SELF_JOIN";
            case MsgId.PEER_JOIN:
                return "PEER_JOIN";
            case MsgId.SELF_LEAVE:
                return "SELF_LEAVE";
            case MsgId.PEER_LEAVE:
                return "PEER_LEAVE";
            case MsgId.SEND_MSG:
                return "SEND_MSG";
            case MsgId.RECV_MSG:
                return "RECV_MSG";
            case MsgId.SET_CONNECTION_INFO:
                return "SET_CONNECTION_INFO";
            case MsgId.GET_CONNECTION_INFO:
                return "GET_CONNECTION_INFO";
            case MsgId.GET_DEVICE_INFO:
                return "GET_DEVICE_INFO";
            case MsgId.GET_CONNECTED_PEERS:
                return "GET_CONNECTED_PEERS";
            case MsgId.GET_NETWORKS:
                return "GET_NETWORKS";
            case MsgId.GET_ACTIVE_NETWORK:
                return "GET_ACTIVE_NETWORK";
            case MsgId.ACTIVATE_NETWORK:
                return "ACTIVATE_NETWORK";
            case MsgId.NETWORK_CONNECTED:
                return "NETWORK_CONNECTED";
            case MsgId.NETWORK_DISCONNECTED:
                return "NETWORK_DISCONNECTED";
            case MsgId.REGISTER_RECEIVER:
                return "REGISTER_RECEIVER";
            case MsgId.UNREGISTER_RECEIVER:
                return "UNREGISTER_RECEIVER";
            case MsgId.START_REMOTE_ACTIVITY:
                return "START_REMOTE_ACTIVITY";
            case MsgId.START_REMOTE_SERVICE:
                return "START_REMOTE_SERVICE";
            case MsgId.SEND_REMOTE_BROADCAST:
                return "SEND_REMOTE_BROADCAST";
        }
        return Integer.toString(msgId);
    }

}
