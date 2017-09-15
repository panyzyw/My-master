/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\MYDOCUMENT\\project\\RuiqianqiApp\\zcbrain\\src\\main\\aidl\\com\\zccl\\ruiqianqi\\brain\\system\\IMainService.aidl
 */
package com.zccl.ruiqianqi.brain.system;
public interface IMainService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IMainService
{
private static final String DESCRIPTOR = "com.zccl.ruiqianqi.brain.system.IMainService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.zccl.ruiqianqi.brain.system.IMainService interface,
 * generating a proxy if needed.
 */
public static IMainService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IMainService))) {
return ((IMainService)iin);
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
case TRANSACTION_sendCommand:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
String _arg1;
_arg1 = data.readString();
IMainCallback _arg2;
_arg2 = IMainCallback.Stub.asInterface(data.readStrongBinder());
this.sendCommand(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_sendCommandSync:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
String _arg1;
_arg1 = data.readString();
MainBean _result = this.sendCommandSync(_arg0, _arg1);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_startTTS:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
ITtsCallback _arg2;
_arg2 = ITtsCallback.Stub.asInterface(data.readStrongBinder());
this.startTTS(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_pauseTTS:
{
data.enforceInterface(DESCRIPTOR);
this.pauseTTS();
reply.writeNoException();
return true;
}
case TRANSACTION_resumeTTS:
{
data.enforceInterface(DESCRIPTOR);
this.resumeTTS();
reply.writeNoException();
return true;
}
case TRANSACTION_stopTTS:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.stopTTS(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isSpeaking:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isSpeaking();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setAllTTSCallback:
{
data.enforceInterface(DESCRIPTOR);
com.zccl.ruiqianqi.brain.system.IAllTtsCallback _arg0;
_arg0 = com.zccl.ruiqianqi.brain.system.IAllTtsCallback.Stub.asInterface(data.readStrongBinder());
this.setAllTTSCallback(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IMainService
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
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     *///void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);
// 异步通知

@Override public void sendCommand(int cmd, String msg, IMainCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(cmd);
_data.writeString(msg);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_sendCommand, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 同步结果

@Override public MainBean sendCommandSync(int cmd, String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
MainBean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(cmd);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_sendCommandSync, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = MainBean.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// 开始发音

@Override public void startTTS(String words, String from, ITtsCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(words);
_data.writeString(from);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_startTTS, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 暂停发音

@Override public void pauseTTS() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pauseTTS, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 恢复发音

@Override public void resumeTTS() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_resumeTTS, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 停止发音，并清空队列

@Override public void stopTTS(String from) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(from);
mRemote.transact(Stub.TRANSACTION_stopTTS, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 是否正在发音

@Override public boolean isSpeaking() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isSpeaking, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// 发音的回调

@Override public void setAllTTSCallback(com.zccl.ruiqianqi.brain.system.IAllTtsCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setAllTTSCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_sendCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_sendCommandSync = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_startTTS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_pauseTTS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_resumeTTS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_stopTTS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_isSpeaking = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_setAllTTSCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     *///void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);
// 异步通知

public void sendCommand(int cmd, String msg, IMainCallback callback) throws android.os.RemoteException;
// 同步结果

public MainBean sendCommandSync(int cmd, String msg) throws android.os.RemoteException;
// 开始发音

public void startTTS(String words, String from, ITtsCallback callback) throws android.os.RemoteException;
// 暂停发音

public void pauseTTS() throws android.os.RemoteException;
// 恢复发音

public void resumeTTS() throws android.os.RemoteException;
// 停止发音，并清空队列

public void stopTTS(String from) throws android.os.RemoteException;
// 是否正在发音

public boolean isSpeaking() throws android.os.RemoteException;
// 发音的回调

public void setAllTTSCallback(com.zccl.ruiqianqi.brain.system.IAllTtsCallback callback) throws android.os.RemoteException;
}
