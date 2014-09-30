Router: A Runtime For Peer-Peer Communication Among Android Devices
===================================================================

Router features:

       * supports network detection (Wifi / Wifi Direct / Mobile hotspot)
       * supports peer discovery, peer device connection
       * supports group communication.
       * provides 3 layers of APIs(idl/messenger/intents) to access the runtime functions as documented in user_guide.
       * run as a service in a background process.
       * a pure generic kernel without enforcing any kind of connection strategy or GUI.

Router can be used in 2 ways:

       * directly embed into your app as an android library project and access its APIs.

       * remotely access an external router's APIs which is embeded in other connector/manager app.

Two kinds of Apps built using Router:

        * Connectors/ConnectionManagers: 
                      * responsible for network detection, peer discovery and device connection.
                      * talk to Router ConnectionService thru APIs (aidl/intents/messenger)

        * Connected Apps:
                      * talk to Router GroupService thru APIs (aidl/intents/messenger)
                      * send/recv app messages

Sample Connectors:

      * Connector_wifi_intent:
                      * use Wifi network thru external wireless router
                      * use WifiDirect network setup among a group of WifiDirect enabled devices
                      * use intenting api to perform network detection, peer discovery and device connection.
                      * directly embed a router service by including router as a library project.

       * Connector_wifi_aidl:
                      * use Wifi network thru external wireless router
                      * use WifiDirect network setup among a group of WifiDirect enabled devices
                      * use AIDL api to perform network detection, peer discovery and device connection.
                      * remotely invoke external router's APIs embedded inside other app such as Connector_wifi_intent (so need to install Connector_wifi_intent before using this connector).

      * Connector_wifidirect_hotspot:
                      * use WifiP2pManager to create a p2p group as a hotspot
                      * connect legacy wifi or wifi direct enabled devices thru this hotspot
                      * use aidl api to perform network detection, peer discovery and device connection.
                      * directly embed a router service by including router as a library project.
                      * remotely invoke external router's APIs embedded inside other app such as Connector_wifi_intent (so need to install Connector_wifi_intent before using this connector).

Sample Connected Apps:

       * Chat:
                     * use any of sample connectors to connect peer devices.
                     * do group chat among connected devices

       * Rotate:
                     * use any of sample connectors to connect peer devices.
                     * allow connected peers to rotate 3D cube together.

