package com.sensetime.mtrc_integrate

import android.content.Context
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

        impl.initMrtc(mContext)

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
            result.success("android原生执行：："+methodCall.arguments.toString()+methodCall.method)
            impl.login(methodCall.arguments.toString())

        }else if(methodCall.method == "MtrcCall"){
            Log.d("zhoud","Login")

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
}
