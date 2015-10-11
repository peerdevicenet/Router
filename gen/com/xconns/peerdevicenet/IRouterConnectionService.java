/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/dev/Workspaces/Workspace/xconns-com/PeerDeviceNet/Router/src/com/xconns/peerdevicenet/IRouterConnectionService.aidl
 */
package com.xconns.peerdevicenet;
public interface IRouterConnectionService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.xconns.peerdevicenet.IRouterConnectionService
{
private static final java.lang.String DESCRIPTOR = "com.xconns.peerdevicenet.IRouterConnectionService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.xconns.peerdevicenet.IRouterConnectionService interface,
 * generating a proxy if needed.
 */
public static com.xconns.peerdevicenet.IRouterConnectionService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.xconns.peerdevicenet.IRouterConnectionService))) {
return ((com.xconns.peerdevicenet.IRouterConnectionService)iin);
}
return new com.xconns.peerdevicenet.IRouterConnectionService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_shutdown:
{
data.enforceInterface(DESCRIPTOR);
this.shutdown();
return true;
}
case TRANSACTION_startSession:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.IRouterConnectionHandler _arg0;
_arg0 = com.xconns.peerdevicenet.IRouterConnectionHandler.Stub.asInterface(data.readStrongBinder());
int _result = this.startSession(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_stopSession:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.stopSession(_arg0);
return true;
}
case TRANSACTION_connectNetwork:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.xconns.peerdevicenet.NetInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = com.xconns.peerdevicenet.NetInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.connectNetwork(_arg0, _arg1);
return true;
}
case TRANSACTION_disconnectNetwork:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.xconns.peerdevicenet.NetInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = com.xconns.peerdevicenet.NetInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.disconnectNetwork(_arg0, _arg1);
return true;
}
case TRANSACTION_activateNetwork:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.xconns.peerdevicenet.NetInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = com.xconns.peerdevicenet.NetInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.activateNetwork(_arg0, _arg1);
return true;
}
case TRANSACTION_getNetworks:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.getNetworks(_arg0);
return true;
}
case TRANSACTION_getActiveNetwork:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.getActiveNetwork(_arg0);
return true;
}
case TRANSACTION_startPeerSearch:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.xconns.peerdevicenet.DeviceInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
int _arg2;
_arg2 = data.readInt();
this.startPeerSearch(_arg0, _arg1, _arg2);
return true;
}
case TRANSACTION_stopPeerSearch:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.stopPeerSearch(_arg0);
return true;
}
case TRANSACTION_connect:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.xconns.peerdevicenet.DeviceInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
byte[] _arg2;
_arg2 = data.createByteArray();
int _arg3;
_arg3 = data.readInt();
this.connect(_arg0, _arg1, _arg2, _arg3);
return true;
}
case TRANSACTION_disconnect:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.xconns.peerdevicenet.DeviceInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.disconnect(_arg0, _arg1);
return true;
}
case TRANSACTION_acceptConnection:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.xconns.peerdevicenet.DeviceInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.acceptConnection(_arg0, _arg1);
return true;
}
case TRANSACTION_denyConnection:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.xconns.peerdevicenet.DeviceInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
int _arg2;
_arg2 = data.readInt();
this.denyConnection(_arg0, _arg1, _arg2);
return true;
}
case TRANSACTION_setConnectionInfo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
boolean _arg2;
_arg2 = (0!=data.readInt());
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
int _arg5;
_arg5 = data.readInt();
this.setConnectionInfo(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
return true;
}
case TRANSACTION_getConnectionInfo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.getConnectionInfo(_arg0);
return true;
}
case TRANSACTION_getDeviceInfo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.getDeviceInfo(_arg0);
return true;
}
case TRANSACTION_getPeerDevices:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.getPeerDevices(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.xconns.peerdevicenet.IRouterConnectionService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
//shutdown router service

@Override public void shutdown() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_shutdown, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//startSession return sessionId

@Override public int startSession(com.xconns.peerdevicenet.IRouterConnectionHandler h) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((h!=null))?(h.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_startSession, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void stopSession(int sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
mRemote.transact(Stub.TRANSACTION_stopSession, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//------ network api ------
//connect to specified network

@Override public void connectNetwork(int sessionId, com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
if ((net!=null)) {
_data.writeInt(1);
net.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_connectNetwork, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//disconnect from specified network

@Override public void disconnectNetwork(int sessionId, com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
if ((net!=null)) {
_data.writeInt(1);
net.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_disconnectNetwork, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//choose specified network for PeerDeviceNet traffic

@Override public void activateNetwork(int sessionId, com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
if ((net!=null)) {
_data.writeInt(1);
net.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_activateNetwork, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//get current connected networks

@Override public void getNetworks(int sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
mRemote.transact(Stub.TRANSACTION_getNetworks, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//get current active network

@Override public void getActiveNetwork(int sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
mRemote.transact(Stub.TRANSACTION_getActiveNetwork, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//peer device discovery/search

@Override public void startPeerSearch(int sessionId, com.xconns.peerdevicenet.DeviceInfo groupLeader, int timeout) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
if ((groupLeader!=null)) {
_data.writeInt(1);
groupLeader.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(timeout);
mRemote.transact(Stub.TRANSACTION_startPeerSearch, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void stopPeerSearch(int sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
mRemote.transact(Stub.TRANSACTION_stopPeerSearch, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//------ connection api ------

@Override public void connect(int sessionId, com.xconns.peerdevicenet.DeviceInfo peer, byte[] token, int timeout) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
if ((peer!=null)) {
_data.writeInt(1);
peer.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeByteArray(token);
_data.writeInt(timeout);
mRemote.transact(Stub.TRANSACTION_connect, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void disconnect(int sessionId, com.xconns.peerdevicenet.DeviceInfo peer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
if ((peer!=null)) {
_data.writeInt(1);
peer.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_disconnect, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void acceptConnection(int sessionId, com.xconns.peerdevicenet.DeviceInfo peer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
if ((peer!=null)) {
_data.writeInt(1);
peer.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_acceptConnection, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void denyConnection(int sessionId, com.xconns.peerdevicenet.DeviceInfo peer, int rejectCode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
if ((peer!=null)) {
_data.writeInt(1);
peer.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(rejectCode);
mRemote.transact(Stub.TRANSACTION_denyConnection, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//query api - get & set my connection settings

@Override public void setConnectionInfo(int sessionId, java.lang.String devName, boolean useSSL, int liveTime, int connTime, int searchTime) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
_data.writeString(devName);
_data.writeInt(((useSSL)?(1):(0)));
_data.writeInt(liveTime);
_data.writeInt(connTime);
_data.writeInt(searchTime);
mRemote.transact(Stub.TRANSACTION_setConnectionInfo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void getConnectionInfo(int sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
mRemote.transact(Stub.TRANSACTION_getConnectionInfo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void getDeviceInfo(int sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
mRemote.transact(Stub.TRANSACTION_getDeviceInfo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//get peer devices in the network

@Override public void getPeerDevices(int sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
mRemote.transact(Stub.TRANSACTION_getPeerDevices, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_shutdown = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_startSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_stopSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_connectNetwork = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_disconnectNetwork = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_activateNetwork = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getNetworks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getActiveNetwork = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_startPeerSearch = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_stopPeerSearch = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_connect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_disconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_acceptConnection = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_denyConnection = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_setConnectionInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_getConnectionInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_getDeviceInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_getPeerDevices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
}
//shutdown router service

public void shutdown() throws android.os.RemoteException;
//startSession return sessionId

public int startSession(com.xconns.peerdevicenet.IRouterConnectionHandler h) throws android.os.RemoteException;
public void stopSession(int sessionId) throws android.os.RemoteException;
//------ network api ------
//connect to specified network

public void connectNetwork(int sessionId, com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException;
//disconnect from specified network

public void disconnectNetwork(int sessionId, com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException;
//choose specified network for PeerDeviceNet traffic

public void activateNetwork(int sessionId, com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException;
//get current connected networks

public void getNetworks(int sessionId) throws android.os.RemoteException;
//get current active network

public void getActiveNetwork(int sessionId) throws android.os.RemoteException;
//peer device discovery/search

public void startPeerSearch(int sessionId, com.xconns.peerdevicenet.DeviceInfo groupLeader, int timeout) throws android.os.RemoteException;
public void stopPeerSearch(int sessionId) throws android.os.RemoteException;
//------ connection api ------

public void connect(int sessionId, com.xconns.peerdevicenet.DeviceInfo peer, byte[] token, int timeout) throws android.os.RemoteException;
public void disconnect(int sessionId, com.xconns.peerdevicenet.DeviceInfo peer) throws android.os.RemoteException;
public void acceptConnection(int sessionId, com.xconns.peerdevicenet.DeviceInfo peer) throws android.os.RemoteException;
public void denyConnection(int sessionId, com.xconns.peerdevicenet.DeviceInfo peer, int rejectCode) throws android.os.RemoteException;
//query api - get & set my connection settings

public void setConnectionInfo(int sessionId, java.lang.String devName, boolean useSSL, int liveTime, int connTime, int searchTime) throws android.os.RemoteException;
public void getConnectionInfo(int sessionId) throws android.os.RemoteException;
public void getDeviceInfo(int sessionId) throws android.os.RemoteException;
//get peer devices in the network

public void getPeerDevices(int sessionId) throws android.os.RemoteException;
}
