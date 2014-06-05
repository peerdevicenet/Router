Router: A Runtime For Peer-Peer Communication Among Android Devices
===================================================================

Router features:

       * handles network detection, peer discovery, peer device connection and group communication.
       * provides 3 layers of APIs(idl/messenger/intents) to access the runtime functions as documented in user_guide.
       * run as a service in a background process.
       * a pure generic kernel without enforcing any kind of connection strategy or GUI.
       * packaged as an android library project, which you can add to your application project's dependencies to gain all functionalities.
        

Two kinds of Apps built using Router:

        * Connectors: 
                      * responsible for network detection, peer discovery and device connection.
                      * talk to Router ConnectionService thru APIs (aidl/intents/messenger)

        * Connected Apps:
                      * talk to Router GroupService thru APIs (aidl/intents/messenger)
                      * send/recv app messages

Sample Connectors:

       * Connector_wifi_aidl:
                      * use Wifi network thru external wireless router
                      * use WifiDirect network setup among a group of WifiDirect enabled devices
                      * use AIDL api to perform network detection, peer discovery and device connection.

      * Connector_wifi_intent:
                      * use Wifi network thru external wireless router
                      * use WifiDirect network setup among a group of WifiDirect enabled devices
                      * use intenting api to perform network detection, peer discovery and device connection.

Sample Connected Apps:

       * Chat:
                     * use any of sample connectors to connect peer devices.
                     * do group chat among connected devices

       * Rotate:
                     * use any of sample connectors to connect peer devices.
                     * allow connected peers to rotate 3D cube together.

