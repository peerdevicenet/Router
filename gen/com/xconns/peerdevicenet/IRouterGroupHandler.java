/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/dev/Workspaces/Workspace/xconns-com/PeerDeviceNet/Router/src/com/xconns/peerdevicenet/IRouterGroupHandler.aidl
 */
package com.xconns.peerdevicenet;
public interface IRouterGroupHandler extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.xconns.peerdevicenet.IRouterGroupHandler
{
private static final java.lang.String DESCRIPTOR = "com.xconns.peerdevicenet.IRouterGroupHandler";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.xconns.peerdevicenet.IRouterGroupHandler interface,
 * generating a proxy if needed.
 */
public static com.xconns.peerdevicenet.IRouterGroupHandler asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.xconns.peerdevicenet.IRouterGroupHandler))) {
return ((com.xconns.peerdevicenet.IRouterGroupHandler)iin);
}
return new com.xconns.peerdevicenet.IRouterGroupHandler.Stub.Proxy(obj);
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
case TRANSACTION_onSelfJoin:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo[] _arg0;
_arg0 = data.createTypedArray(com.xconns.peerdevicenet.DeviceInfo.CREATOR);
this.onSelfJoin(_arg0);
return true;
}
case TRANSACTION_onPeerJoin:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onPeerJoin(_arg0);
return true;
}
case TRANSACTION_onSelfLeave:
{
data.enforceInterface(DESCRIPTOR);
this.onSelfLeave();
return true;
}
case TRANSACTION_onPeerLeave:
{
data.enforceInterface(DESCRIPTOR);
com.xconns.peerdevicenet.DeviceInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onPeerLeave(_arg0);
return true;
}
case TRANSACTION_onReceive:
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
this.onReceive(_arg0, _arg1);
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
private static class Proxy implements com.xconns.peerdevicenet.IRouterGroupHandler
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
@Override public void onSelfJoin(com.xconns.peerdevicenet.DeviceInfo[] devices) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedArray(devices, 0);
mRemote.transact(Stub.TRANSACTION_onSelfJoin, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onPeerJoin(com.xconns.peerdevicenet.DeviceInfo device) throws android.os.RemoteException
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
mRemote.transact(Stub.TRANSACTION_onPeerJoin, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onSelfLeave() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onSelfLeave, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onPeerLeave(com.xconns.peerdevicenet.DeviceInfo device) throws android.os.RemoteException
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
mRemote.transact(Stub.TRANSACTION_onPeerLeave, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onReceive(com.xconns.peerdevicenet.DeviceInfo src, byte[] msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((src!=null)) {
_data.writeInt(1);
src.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeByteArray(msg);
mRemote.transact(Stub.TRANSACTION_onReceive, _data, null, android.os.IBinder.FLAG_ONEWAY);
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
static final int TRANSACTION_onSelfJoin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onPeerJoin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onSelfLeave = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onPeerLeave = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onReceive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_onGetPeerDevices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
public void onError(java.lang.String errInfo) throws android.os.RemoteException;
public void onSelfJoin(com.xconns.peerdevicenet.DeviceInfo[] devices) throws android.os.RemoteException;
public void onPeerJoin(com.xconns.peerdevicenet.DeviceInfo device) throws android.os.RemoteException;
public void onSelfLeave() throws android.os.RemoteException;
public void onPeerLeave(com.xconns.peerdevicenet.DeviceInfo device) throws android.os.RemoteException;
public void onReceive(com.xconns.peerdevicenet.DeviceInfo src, byte[] msg) throws android.os.RemoteException;
public void onGetPeerDevices(com.xconns.peerdevicenet.DeviceInfo[] devices) throws android.os.RemoteException;
}
