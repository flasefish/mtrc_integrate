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
        Log.d("zhoud","onMethodCall")
       // Log.d("CCTextView MethodChannel call.method:" + methodCall.method.toString() + "  call arguments:" + methodCall.arguments)
        //if ("setText".equals(methodCall.method)) {
           // val text = methodCall.arguments as String
            //myNativeView.setText(text)
            result.success("修改成功")
        //}
    }
}
