/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\MYDOCUMENT\\project\\RuiqianqiApp\\zcbrain\\src\\main\\aidl\\com\\zccl\\ruiqianqi\\brain\\system\\IMainCallback.aidl
 */
package com.zccl.ruiqianqi.brain.system;
public interface IMainCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IMainCallback
{
private static final String DESCRIPTOR = "com.zccl.ruiqianqi.brain.system.IMainCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.zccl.ruiqianqi.brain.system.IMainCallback interface,
 * generating a proxy if needed.
 */
public static IMainCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IMainCallback))) {
return ((IMainCallback)iin);
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
case TRANSACTION_OnSuccess:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
String _arg1;
_arg1 = data.readString();
this.OnSuccess(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_OnFailure:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
String _arg1;
_arg1 = data.readString();
this.OnFailure(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IMainCallback
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
@Override public void OnSuccess(int cmd, String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(cmd);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_OnSuccess, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void OnFailure(int cmd, String errmsg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(cmd);
_data.writeString(errmsg);
mRemote.transact(Stub.TRANSACTION_OnFailure, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_OnSuccess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_OnFailure = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void OnSuccess(int cmd, String msg) throws android.os.RemoteException;
public void OnFailure(int cmd, String errmsg) throws android.os.RemoteException;
}
