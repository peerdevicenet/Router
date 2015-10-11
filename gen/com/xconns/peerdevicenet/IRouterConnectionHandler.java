/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/dev/Workspaces/Workspace/xconns-com/PeerDeviceNet/Router/src/com/xconns/peerdevicenet/IRouterConnectionHandler.aidl
 */
package com.xconns.peerdevicenet;
public interface IRouterConnectionHandler extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.xconns.peerdevicenet.IRouterConnectionHandler
{
private static final java.lang.String DESCRIPTOR = "com.xconns.peerdevicenet.IRouterConnectionHandler";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.xconns.peerdevicenet.IRouterConnectionHandler interface,
 * generating a proxy if needed.
 */
public static com.xconns.peerdevicenet.IRouterConnectionHandler asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.xconns.peerdevicenet.IRouterConnectionHandler))) {
return ((com.xconns.peerdevicenet.IRouterConnectionHandler)iin);
}
return new com.xconns.peerdevicenet.IRouterConnectionHandler.Stub.Proxy(obj);
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
case TRANSACTION_onError:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onError(_arg0);
return true;
}
case TRANSACTION_onNetworkConnected:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.NetInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.NetInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onNetworkConnected(_arg0);
return true;
}
case TRANSACTION_onNetworkDisconnected:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.NetInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.NetInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onNetworkDisconnected(_arg0);
return true;
}
case TRANSACTION_onNetworkConnecting:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.NetInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.NetInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onNetworkConnecting(_arg0);
return true;
}
case TRANSACTION_onNetworkConnectionFailed:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.NetInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.NetInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onNetworkConnectionFailed(_arg0);
return true;
}
case TRANSACTION_onNetworkActivated:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.NetInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.NetInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onNetworkActivated(_arg0);
return true;
}
case TRANSACTION_onGetNetworks:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.NetInfo[] _arg0;
_arg0 = data.createTypedArray(com.xconns.peerdevicenet.NetInfo.CREATOR);
this.onGetNetworks(_arg0);
return true;
}
case TRANSACTION_onGetActiveNetwork:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.NetInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.NetInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onGetActiveNetwork(_arg0);
return true;
}
case TRANSACTION_onSearchStart:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onSearchStart(_arg0);
return true;
}
case TRANSACTION_onSearchFoundDevice:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
boolean _arg1;
_arg1 = (0!=data.readInt());
this.onSearchFoundDevice(_arg0, _arg1);
return true;
}
case TRANSACTION_onSearchComplete:
{
data.enforceInterface(DESCRIPTOR);
this.onSearchComplete();
return true;
}
case TRANSACTION_onConnecting:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
byte[] _arg1;
_arg1 = data.createByteArray();
this.onConnecting(_arg0, _arg1);
return true;
}
case TRANSACTION_onConnectionFailed:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
int _arg1;
_arg1 = data.readInt();
this.onConnectionFailed(_arg0, _arg1);
return true;
}
case TRANSACTION_onConnected:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onConnected(_arg0);
return true;
}
case TRANSACTION_onDisconnected:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onDisconnected(_arg0);
return true;
}
case TRANSACTION_onSetConnectionInfo:
{
data.enforceInterface(DESCRIPTOR);
this.onSetConnectionInfo();
return true;
}
case TRANSACTION_onGetConnectionInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
this.onGetConnectionInfo(_arg0, _arg1, _arg2, _arg3, _arg4);
return true;
}
case TRANSACTION_onGetDeviceInfo:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onGetDeviceInfo(_arg0);
return true;
}
case TRANSACTION_onGetPeerDevices:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo[] _arg0;
_arg0 = data.createTypedArray(com.xconns.peerdevicenet.DeviceInfo.CREATOR);
this.onGetPeerDevices(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.xconns.peerdevicenet.IRouterConnectionHandler
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
@Override public void onError(java.lang.String errInfo) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(errInfo);
mRemote.transact(Stub.TRANSACTION_onError, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//----------- network callbacks ---------
//network status events

@Override public void onNetworkConnected(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((net!=null)) {
_data.writeInt(1);
net.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNetworkConnected, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onNetworkDisconnected(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((net!=null)) {
_data.writeInt(1);
net.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNetworkDisconnected, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onNetworkConnecting(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((net!=null)) {
_data.writeInt(1);
net.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNetworkConnecting, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onNetworkConnectionFailed(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((net!=null)) {
_data.writeInt(1);
net.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNetworkConnectionFailed, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onNetworkActivated(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((net!=null)) {
_data.writeInt(1);
net.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNetworkActivated, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//get all connected networks

@Override public void onGetNetworks(com.xconns.peerdevicenet.NetInfo[] nets) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedArray(nets, 0);
mRemote.transact(Stub.TRANSACTION_onGetNetworks, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//get current active network

@Override public void onGetActiveNetwork(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((net!=null)) {
_data.writeInt(1);
net.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onGetActiveNetwork, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//search related

@Override public void onSearchStart(com.xconns.peerdevicenet.DeviceInfo groupLeader) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((groupLeader!=null)) {
_data.writeInt(1);
groupLeader.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onSearchStart, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onSearchFoundDevice(com.xconns.peerdevicenet.DeviceInfo device, boolean useSSL) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((device!=null)) {
_data.writeInt(1);
device.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(((useSSL)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_onSearchFoundDevice, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onSearchComplete() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onSearchComplete, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//---------- connection callbacks -------

@Override public void onConnecting(com.xconns.peerdevicenet.DeviceInfo device, byte[] token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((device!=null)) {
_data.writeInt(1);
device.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeByteArray(token);
mRemote.transact(Stub.TRANSACTION_onConnecting, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onConnectionFailed(com.xconns.peerdevicenet.DeviceInfo device, int rejectCode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((device!=null)) {
_data.writeInt(1);
device.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(rejectCode);
mRemote.transact(Stub.TRANSACTION_onConnectionFailed, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onConnected(com.xconns.peerdevicenet.DeviceInfo device) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((device!=null)) {
_data.writeInt(1);
device.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onConnected, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onDisconnected(com.xconns.peerdevicenet.DeviceInfo device) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((device!=null)) {
_data.writeInt(1);
device.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onDisconnected, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onSetConnectionInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onSetConnectionInfo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onGetConnectionInfo(java.lang.String devName, boolean useSSL, int liveTime, int connTime, int searchTime) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(devName);
_data.writeInt(((useSSL)?(1):(0)));
_data.writeInt(liveTime);
_data.writeInt(connTime);
_data.writeInt(searchTime);
mRemote.transact(Stub.TRANSACTION_onGetConnectionInfo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onGetDeviceInfo(com.xconns.peerdevicenet.DeviceInfo device) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((device!=null)) {
_data.writeInt(1);
device.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onGetDeviceInfo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onGetPeerDevices(com.xconns.peerdevicenet.DeviceInfo[] devices) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedArray(devices, 0);
mRemote.transact(Stub.TRANSACTION_onGetPeerDevices, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_onError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onNetworkConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onNetworkDisconnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onNetworkConnecting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onNetworkConnectionFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onNetworkActivated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_onGetNetworks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_onGetActiveNetwork = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_onSearchStart = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_onSearchFoundDevice = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_onSearchComplete = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_onConnecting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_onConnectionFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_onConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_onDisconnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_onSetConnectionInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_onGetConnectionInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_onGetDeviceInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_onGetPeerDevices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
}
public void onError(java.lang.String errInfo) throws android.os.RemoteException;
//----------- network callbacks ---------
//network status events

public void onNetworkConnected(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException;
public void onNetworkDisconnected(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException;
public void onNetworkConnecting(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException;
public void onNetworkConnectionFailed(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException;
public void onNetworkActivated(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException;
//get all connected networks

public void onGetNetworks(com.xconns.peerdevicenet.NetInfo[] nets) throws android.os.RemoteException;
//get current active network

public void onGetActiveNetwork(com.xconns.peerdevicenet.NetInfo net) throws android.os.RemoteException;
//search related

public void onSearchStart(com.xconns.peerdevicenet.DeviceInfo groupLeader) throws android.os.RemoteException;
public void onSearchFoundDevice(com.xconns.peerdevicenet.DeviceInfo device, boolean useSSL) throws android.os.RemoteException;
public void onSearchComplete() throws android.os.RemoteException;
//---------- connection callbacks -------

public void onConnecting(com.xconns.peerdevicenet.DeviceInfo device, byte[] token) throws android.os.RemoteException;
public void onConnectionFailed(com.xconns.peerdevicenet.DeviceInfo device, int rejectCode) throws android.os.RemoteException;
public void onConnected(com.xconns.peerdevicenet.DeviceInfo device) throws android.os.RemoteException;
public void onDisconnected(com.xconns.peerdevicenet.DeviceInfo device) throws android.os.RemoteException;
public void onSetConnectionInfo() throws android.os.RemoteException;
public void onGetConnectionInfo(java.lang.String devName, boolean useSSL, int liveTime, int connTime, int searchTime) throws android.os.RemoteException;
public void onGetDeviceInfo(com.xconns.peerdevicenet.DeviceInfo device) throws android.os.RemoteException;
public void onGetPeerDevices(com.xconns.peerdevicenet.DeviceInfo[] devices) throws android.os.RemoteException;
}
