/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\MYDOCUMENT\\project\\RuiqianqiApp\\zcbrain\\src\\main\\aidl\\com\\zccl\\ruiqianqi\\brain\\system\\ITtsCallback.aidl
 */
package com.zccl.ruiqianqi.brain.system;
public interface ITtsCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements ITtsCallback
{
private static final String DESCRIPTOR = "com.zccl.ruiqianqi.brain.system.ITtsCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.zccl.ruiqianqi.brain.system.ITtsCallback interface,
 * generating a proxy if needed.
 */
public static ITtsCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof ITtsCallback))) {
return ((ITtsCallback)iin);
}
return new Proxy(obj);
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
case TRANSACTION_OnBegin:
{
data.enforceInterface(DESCRIPTOR);
this.OnBegin();
reply.writeNoException();
return true;
}
case TRANSACTION_OnPause:
{
data.enforceInterface(DESCRIPTOR);
this.OnPause();
reply.writeNoException();
return true;
}
case TRANSACTION_OnResume:
{
data.enforceInterface(DESCRIPTOR);
this.OnResume();
reply.writeNoException();
return true;
}
case TRANSACTION_OnComplete:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.OnComplete(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements ITtsCallback
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
public String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void OnBegin() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_OnBegin, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void OnPause() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_OnPause, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void OnResume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_OnResume, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void OnComplete(String error, String tag) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(error);
_data.writeString(tag);
mRemote.transact(Stub.TRANSACTION_OnComplete, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_OnBegin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_OnPause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_OnResume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_OnComplete = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void OnBegin() throws android.os.RemoteException;
public void OnPause() throws android.os.RemoteException;
public void OnResume() throws android.os.RemoteException;
public void OnComplete(String error, String tag) throws android.os.RemoteException;
}
