package com.sensetime.mtrc_integrate

import android.content.Context
import android.os.Handler
import android.os.Message
import io.flutter.plugin.platform.PlatformView
import thunder.mrtc.MrtcRender
import android.view.View
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink


class  MtrcView(context: Context, messenger: BinaryMessenger, viewId: Int, args: Map<String, Any>?)
    : PlatformView,MethodChannel.MethodCallHandler,EventChannel.StreamHandler {
    lateinit var mtrcView : MrtcRender
    lateinit var mContext : Context
    var viewId: Int = -1
    private var eventSink: EventChannel.EventSink? = null
    lateinit var methodChannel : MethodChannel
    lateinit var eventChannel  : EventChannel
    var impl: MtrcControl = MtrcControl()
    lateinit var handler: Handler
    lateinit var callHandler: Handler


    companion object {
        var NATIVE_MRTC_VIEW_TYPE_ID: String = "com.sensetime.mtrc_integrate/MtrcView"
        var NATIVE_MRTC_VIEW_EVENT_ID: String = "com.sensetime.mtrc_integrate/MtrcEvent"
    }

    init {
        this.mtrcView = MrtcRender(context)
        this.mContext = context
        this.viewId = viewId
        Log.d("zhoud","MtrcView init viewID = $viewId")
        methodChannel = MethodChannel(messenger, NATIVE_MRTC_VIEW_TYPE_ID.toString() + "_" + viewId)
        methodChannel.setMethodCallHandler(this)

        eventChannel = EventChannel(messenger,NATIVE_MRTC_VIEW_EVENT_ID.toString() + "_" + viewId);
        eventChannel.setStreamHandler(this)

        handler = LoginHandler()
        callHandler = CallHandler()
        impl.initMrtc(mContext,handler,callHandler);


    }

    override fun getView(): View {
        return this.mtrcView
    }

    override fun dispose() {
      //  this.mtrcView.onDestroy()
    }


    override fun onMethodCall(@NonNull methodCall: MethodCall, @NonNull result: MethodChannel.Result) {
        Log.d("zhoud","onMethodCall = ${methodCall.method}  arg =${methodCall.arguments.toString()} ")
        if(methodCall.method == "MtrcLogin"){
            Log.d("zhoud","Login")
            impl.login(methodCall.arguments.toString())
            result.success("android原生执行：："+methodCall.arguments.toString()+methodCall.method)
        }else if(methodCall.method == "MtrcCall"){
            Log.d("zhoud","call")
            impl.call(methodCall.arguments.toString(),mtrcView)
            result.success("android androidMethodLoginExec run：：" + methodCall.arguments.toString() + methodCall.method)
        }else if(methodCall.method == "MtrcLogout") {
            Log.d("zhoud","MtrcLogout")
        }else if(methodCall.method == "MtrcHangup") {
            Log.d("zhoud","MtrcHangup")
        }else if (methodCall.method.equals("addMsg")) {
            Log.d("zhoud","MtrcaddMsg call ${methodCall.method},arg = ${methodCall.arguments} " )
            if (methodCall.arguments != null) {
                val type: Integer = methodCall.arguments as Integer
                Log.d("zhoud","type = $type" )
                eventSink?.success(type)
            }else{
                Log.d("zhoud","arguments = null")
            }
        }else{
            result.notImplemented()
        }
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        // eventChannel 建立连接;
        eventSink = events
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }

    open fun SendMessageToApp(type:Int){
        eventSink?.success(type)
    }

    inner class LoginHandler : Handler() {
       override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MessageType.LOGIN -> {
                    Log.d("zhoud","LoginHandler MessageType.LOGIN")
                    this@MtrcView.SendMessageToApp(MessageType.LOGIN) //onRegSuccess()
                }
                MessageType.LOGOUT -> {
                    Log.d("zhoud","LoginHandler MessageType.LOGOUT")
                    this@MtrcView.SendMessageToApp(MessageType.LOGOUT) //onCloseWebsocket()
                }
                MessageType.CALL_IN -> {
                    Log.d("zhoud","LoginHandler MessageType.LOGOUY")
                }
                MessageType.CANCEL_CALL->{
                    Log.d("zhoud","LoginHandler MessageType.CANCEL_CALL")
                    this@MtrcView.SendMessageToApp(MessageType.CANCEL_CALL) //onCloseWebsocket()
                }
                MessageType.EXCEPTION->{
                    Log.d("zhoud","LoginHandler MessageType.EXCEPTION")
                    this@MtrcView.SendMessageToApp(MessageType.EXCEPTION) //onCloseWebsocket()
                }
                MessageType.CHANNELMESSAGE->{
                    Log.d("zhoud","LoginHandler MessageType.CHANNELMESSAGE")
                    this@MtrcView.SendMessageToApp(MessageType.CHANNELMESSAGE) //onCloseWebsocket()
                }
                else -> {
                    Log.d("zhoud","LoginHandler MessageType.LOGOUY")
                }
            }
        }
    }

    inner class CallHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
           // val intent = Intent()
            when (msg.what) {
                MessageType.CALLOUT_ACCEPTED -> {
                    Log.d("zhoud", "Accept the call")
                    //mHandler.post(Runnable { videoOutput.setVisibility(View.VISIBLE) })
                    this@MtrcView.SendMessageToApp(MessageType.CALLOUT_ACCEPTED)
                }
                MessageType.REJECT -> {
                    Log.d("zhoud", "reject the call")
                    this@MtrcView.SendMessageToApp(MessageType.REJECT)
                    //把需要返回的数据存放在intent
                    //      intent.putExtra("type", "reject")
                    //设置返回数据
                    //     setResult(RESULT_OK, intent)
                    //      finish()
                }
                MessageType.NETWORK_DISCONNECT -> {
                    Log.d("zhoud", "NETWORK_DISCONNECT the call")
                    //把需要返回的数据存放在intent
                    //     intent.putExtra("type", "disconnect")
                    //设置返回数据
                    //     setResult(RESULT_OK, intent)
                    //     finish()
                }
                MessageType.BYE -> {
                    Log.d("zhoud", "bye the call")
                    //把需要返回的数据存放在intent
                    //  intent.putExtra("type", "reject")
                    //设置返回数据
                    //  setResult(RESULT_OK, intent)
                    // mHandler.post(Runnable { videoOutput.setVisibility(View.INVISIBLE) })
                    //  finish()
                    this@MtrcView.SendMessageToApp(MessageType.BYE)
                }
                else -> {  Log.d("zhoud", "CallHandler unkonw message")}
            }
        }
    }
}
