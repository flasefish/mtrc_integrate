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

class  MtrcView(context: Context, messenger: BinaryMessenger, viewId: Int, args: Map<String, Any>?)
    : PlatformView,MethodChannel.MethodCallHandler {
    lateinit var mtrcView : MrtcRender
    lateinit var mContext : Context
    var viewId: Int = -1
    lateinit var methodChannel : MethodChannel
    var NATIVE_CCTV_VIEW_TYPE_ID:String = "com.sensetime.mtrc_integrate/MtrcView"

    init {

        this.mtrcView = MrtcRender(context)
        this.mContext = context
        this.viewId = viewId
        Log.d("zhoud","MtrcView init viewID = $viewId")
        methodChannel = MethodChannel(messenger, NATIVE_CCTV_VIEW_TYPE_ID.toString() + "_" + viewId)
        methodChannel.setMethodCallHandler(this)
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
        }else if(methodCall.method == "MtrcCall"){
            Log.d("zhoud","Login")

            result.success("android androidMethodLoginExec run：：" + methodCall.arguments.toString() + methodCall.method)
        }else if(methodCall.method == "MtrcLogout") {
            Log.d("zhoud","MtrcLogout")
        }else if(methodCall.method == "MtrcHangup") {
            Log.d("zhoud","MtrcHangup")
        }else{
            result.notImplemented()
        }
    }
}
