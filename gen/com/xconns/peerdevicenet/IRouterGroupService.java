/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/dev/Workspaces/Workspace/xconns-com/PeerDeviceNet/Router/src/com/xconns/peerdevicenet/IRouterGroupService.aidl
 */
package com.xconns.peerdevicenet;
/* pure async api */
public interface IRouterGroupService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.xconns.peerdevicenet.IRouterGroupService
{
private static final java.lang.String DESCRIPTOR = "com.xconns.peerdevicenet.IRouterGroupService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.xconns.peerdevicenet.IRouterGroupService interface,
 * generating a proxy if needed.
 */
public static com.xconns.peerdevicenet.IRouterGroupService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.xconns.peerdevicenet.IRouterGroupService))) {
return ((com.xconns.peerdevicenet.IRouterGroupService)iin);
}
return new com.xconns.peerdevicenet.IRouterGroupService.Stub.Proxy(obj);
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
case TRANSACTION_joinGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.xconns.peerdevicenet.DeviceInfo[] _arg1;
_arg1 = data.createTypedArray(com.xconns.peerdevicenet.DeviceInfo.CREATOR);
com.xconns.peerdevicenet.IRouterGroupHandler _arg2;
_arg2 = com.xconns.peerdevicenet.IRouterGroupHandler.Stub.asInterface(data.readStrongBinder());
this.joinGroup(_arg0, _arg1, _arg2);
return true;
}
case TRANSACTION_leaveGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.xconns.peerdevicenet.IRouterGroupHandler _arg1;
_arg1 = com.xconns.peerdevicenet.IRouterGroupHandler.Stub.asInterface(data.readStrongBinder());
this.leaveGroup(_arg0, _arg1);
return true;
}
case TRANSACTION_send:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.xconns.peerdevicenet.DeviceInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = com.xconns.peerdevicenet.DeviceInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
byte[] _arg2;
_arg2 = data.createByteArray();
this.send(_arg0, _arg1, _arg2);
return true;
}
case TRANSACTION_getPeerDevices:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.getPeerDevices(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.xconns.peerdevicenet.IRouterGroupService
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
@Override public void joinGroup(java.lang.String groupId, com.xconns.peerdevicenet.DeviceInfo[] peers, com.xconns.peerdevicenet.IRouterGroupHandler h) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(groupId);
_data.writeTypedArray(peers, 0);
_data.writeStrongBinder((((h!=null))?(h.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_joinGroup, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void leaveGroup(java.lang.String groupId, com.xconns.peerdevicenet.IRouterGroupHandler h) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(groupId);
_data.writeStrongBinder((((h!=null))?(h.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_leaveGroup, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void send(java.lang.String groupId, com.xconns.peerdevicenet.DeviceInfo dest, byte[] msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(groupId);
if ((dest!=null)) {
_data.writeInt(1);
dest.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeByteArray(msg);
mRemote.transact(Stub.TRANSACTION_send, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void getPeerDevices(java.lang.String groupId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(groupId);
mRemote.transact(Stub.TRANSACTION_getPeerDevices, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_joinGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_leaveGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_send = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getPeerDevices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void joinGroup(java.lang.String groupId, com.xconns.peerdevicenet.DeviceInfo[] peers, com.xconns.peerdevicenet.IRouterGroupHandler h) throws android.os.RemoteException;
public void leaveGroup(java.lang.String groupId, com.xconns.peerdevicenet.IRouterGroupHandler h) throws android.os.RemoteException;
public void send(java.lang.String groupId, com.xconns.peerdevicenet.DeviceInfo dest, byte[] msg) throws android.os.RemoteException;
public void getPeerDevices(java.lang.String groupId) throws android.os.RemoteException;
}
