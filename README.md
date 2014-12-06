Router: A Runtime For Peer-Peer Communication Among Android Devices
===================================================================

Router features:

       * supports network detection (Wifi / Wifi Direct / Mobile hotspot)
       * supports peer discovery, peer device connection
       * supports group communication.
       * provides 3 layers of APIs(idl/messenger/intents) to access the runtime functions as documented in http://peerdevicenet.github.io.
       * run as a service in a background process.
       * a pure generic kernel without enforcing any kind of connection strategy or GUI.

Router is published as two jar/aar files at Maven Central. You can download the latest jars from http://search.maven.org/#search|ga|1|peerdevicenet, or grab via Maven or gradle as following:

       * peerdevicenet-api.jar:
                * provide client api to access Router runtime.
                * Maven:
                        * <dependency>
                                * <groupId>com.xconns.peerdevicenet</groupId>
                                * <artifactId>peerdevicenet-api</artifactId>
                                * <version>1.1.4</version>
                        * </dependency>
                * gradle:
                        * 'com.xconns.peerdevicenet:peerdevicenet-api:1.1.4'
       * peerdevicenet-router.aar:
                * allow you embedded a Router instance into your app
                * Maven:
                        * <dependency>
                                * <groupId>com.xconns.peerdevicenet</groupId>
                                * <artifactId>peerdevicenet-router</artifactId>
                                * <version>1.1.4</version>
                                * <packaging>aar</packaging>
                        * </dependency>
                * gradle:
                        * 'com.xconns.peerdevicenet:peerdevicenet-router:1.1.4'

Router can be used in 2 ways:

       * directly embed a Router instance into your app in one of the following two ways:
                  * download Router project and use it as your app's dependent library project.
                  * if you are using android's new gradle build system, you can import it as 'com.xconns.peerdevicenet:peerdevicenet-router:1.1.4'.

       * use router's API (peerdevicenet-api) to acces a Router instance embedded in other connector/manager app.
                  * download peerdevicenet-api.jar from the above MavenCentral addr and copy to project's "libs/" directory.
                  * if you are using android's new gradle build system, you can import it as 'com.xconns.peerdevicenet:peerdevicenet-api:1.1.4'.

Two kinds of Apps built using Router:

        * Connectors/ConnectionManagers: 
                      * directly embed a Router instance into your app.
                      * responsible for network detection, peer discovery and device connection.
                      * talk to Router ConnectionService thru APIs (aidl/intents/messenger)

        * Connected Apps:
                      * talk to Router GroupService thru APIs (peerdevicenet-api.jar)
                      * send/recv app messages

Sample Connectors:

       * Connector_wifi_intent:
                      * use Wifi network thru external wireless router
                      * use WifiDirect network setup among a group of WifiDirect enabled devices
                      * use intenting api to perform network detection, peer discovery and device connection.
                      * directly embed a router instance by including Router project as a library project or importing peerdevicenet-router.aar.

       * Connector_wifi_aidl:
                      * use Wifi network thru external wireless router
                      * use WifiDirect network setup among a group of WifiDirect enabled devices
                      * use AIDL api to perform network detection, peer discovery and device connection.
                      * use router's APIs to access a Router instance embedded inside other app such as Connector_wifi_intent (so need to install Connector_wifi_intent before using this connector).

       * Connector_wifidirect_hotspot:
                      * use WifiP2pManager to create a p2p group as a hotspot
                      * connect legacy wifi or wifi direct enabled devices thru this hotspot
                      * use aidl api to perform network detection, peer discovery and device connection.
                      * directly embed a router service by including router as a library project.
                      * use router's APIs to access a Router instance embedded inside other app such as Connector_wifi_intent (so need to install Connector_wifi_intent before using this connector).

Sample Connected Apps:

       * Chat:
                     * use any of sample connectors to connect peer devices.
                     * do group chat among connected devices.

       * Rotate:
                     * use any of sample connectors to connect peer devices.
                     * allow connected peers to rotate 3D cube together.

