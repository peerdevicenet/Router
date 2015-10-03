/**
<html>
  <body>
    PeerDeviceNet enables P2P, M2M connection and communication among mobile,IoT 
    devices (phones, tablets, etc.).
    <p></p>
    At mobile devices, PeerDeviceNet runtime (Router) runs as android services
    in a background process. Currently it includes the following services
    <ol>
      <li> ConnectionService, supports the following functions:
        <ol type="A">
          <li>connect, disconnect networks, detect attachments and detachments (wifi, wifi
            direct, mobile hotspot). </li>
          <li>perform peer devices discovery </li>
          <li>handle device connections and disconnections.</li>
        </ol>
      </li>
      <li> GroupService, supports the following functions:
        <ol type="A">
          <li>join/leave communication group, detect peer devices
            joining or leaving. </li>
          <li>send messages to peers in group and receive messages from peers in group.</li>
        </ol>
      </li>
    </ol>
    <p>
    PeerDeviceNet APIs provides asynchronous messaging interfaces between
    clients and Router services (Connection Service and Group Service).
	Router services will expose their APIs as asynchronous messages (or one-way AIDL methods).
    Similarly clients will provide async messaging (or one-way AIDL methods) APIs to allow Router
    services to call back or notify clients for events such as network
    attachment and detachment, peer device connection and disconnection.
    <p> 
    Both runtime/Router and api have been published as two jar/aar files at
      Maven Central. You can download the latest jars from
      <a href="http://search.maven.org/#search|ga|1|peerdevicenet">MavenCentral Site</a>, or grab via
      Maven or gradle as following:
    </p>
    <ol>
      <li>API: peerdevicenet-api.jar </li>
      <ol type="A">
        <li>maven</li>
    &lt;dependency&gt;<br>
    &lt;groupId&gt;com.xconns.peerdevicenet&lt;/groupId&gt;<br>
    &lt;artifactId&gt;peerdevicenet-api&lt;/artifactId&gt;<br>
    &lt;version&gt;1.1.6&lt;/version&gt;<br>
    &lt;/dependency&gt;<br>
         <li>gradle<br>
    com.xconns.peerdevicenet:peerdevicenet-api:1.1.6<br>
      </ol>
      <li>Runtime: peerdevicenet-router.aar </li>
      <ol type="A">
        <li>maven</li>
    &lt;dependency&gt;<br>
    &lt;groupId&gt;com.xconns.peerdevicenet&lt;/groupId&gt;<br>
    &lt;artifactId&gt;peerdevicenet-router&lt;/artifactId&gt;<br>
    &lt;version&gt;1.1.6&lt;/version&gt;<br>
    &lt;packaging&gt;aar&lt;/packaging&gt;<br>
    &lt;/dependency&gt;<br>
         <li>gradle<br>
    com.xconns.peerdevicenet:peerdevicenet-router:1.1.6<br>
      </ol>
    </ol>
    <p>
These asynchronous messaging apis between clients and Router services can be accessed in three ways:
    <ol>
      <li>intent actions</li>
      Intent API provides high level access to Router's features. 
      To use this API, all you need is to have PeerDeviceNet (Router) 
      installed on users devices to gain the runtime support. Apps send
       normal android intents using PeerDeviceNet specific action names and 
       pack message data as intent "extra" data items with 
       PeerDeviceNet specific keys. All the PeerDeviceNet action names 
       are defined at {@link com.xconns.peerdevicenet.Router.Intent Router.Intent} 
       class and "extra" data keys at 
       {@link com.xconns.peerdevicenet.Router.MsgKey Router.MsgKey} class which you get when you add the client jar file 
       to your project.
      <li>one-way AIDL methods</li>
      To access Router services thru these apis, you can use directly access aidl apis or use client wrapper 
      classes {@link com.xconns.peerdevicenet.RouterConnectionClient RouterConnectionClient}
      and {@link com.xconns.peerdevicenet.RouterGroupClient RouterGroupClient}. These wrapper classes also handle 
      some common book-keeping for you. There is also {@link com.xconns.peerdevicenet.DeviceInfo DeviceInfo} class and 
      {@link com.xconns.peerdevicenet.NetInfo NetInfo} class used at aidl apis which defines information about devices and 
      networks. 
      <li>messages sent thru Messengers</li>
      This api defines a group of PeerDeviceNet specific message ids to allow apps 
      use android's "Messenger" design pattern to talk to Router services. Apps use messengers to 
      send message data as a bundle with data items indexed by keys. 
      All the PeerDeviceNet message ids are defined at 
      {@link com.xconns.peerdevicenet.Router.MsgId Router.MsgId} class and message data keys at 
       {@link com.xconns.peerdevicenet.Router.MsgKey Router.MsgKey} class.
    </ol>
 
  </body>
</html>
*/

package com.xconns.peerdevicenet;
