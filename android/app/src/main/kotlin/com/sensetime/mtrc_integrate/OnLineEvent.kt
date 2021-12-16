package com.sensetime.mtrc_integrate

import thunder.mrtc.callback.OnLineEventExecutor
import thunder.mrtc.Call
import thunder.mrtc.common.ByeReason
import thunder.mrtc.model.MediaAttributes
import thunder.mrtc.model.DeviceInfo
import thunder.mrtc.common.AttributeValue
import android.util.Log
import android.os.Handler
import android.os.Message
import com.sensetime.log.BILog
import java.nio.ByteBuffer
import java.nio.charset.Charset


public class OnLineEvent : OnLineEventExecutor {
    val TAG = "OnLineEvent"

    public lateinit var handler:Handler

    public fun OnLineEvent(handler: Handler) {
        this.handler = handler
    }

    override fun onRegSuccess() {
        Log.i(TAG, "Register success")
        handler.sendEmptyMessage(MessageType.LOGIN)
    }

    override fun onCloseWebsocket() {
        Log.i(TAG, "Close Websocket")
        handler.sendEmptyMessage(MessageType.LOGOUT)
    }

    override fun onCallIn(mrtcCall: Call) {
        Log.i(TAG, "ON CALL IN")
        val msg: android.os.Message = handler.obtainMessage()
        msg.what = MessageType.CALL_IN
        msg.obj = mrtcCall
        handler.sendMessage(msg)
    }

    override fun onCancelCall(mrtcCall: Call?) {}

    override fun onRemoteBye(mrtcCall: Call?, byeReason: ByeReason?) {
        Log.i(TAG, "on Remote Bye")
    }

    override fun onReconnect() {}

    override fun onException(code: Int, message: String?) {}

    /**
     * datachannel 打开事件
     */
    override fun onChannelOpen() {
        Log.i(TAG, "data channel opened")
    }

    /**
     * datachannel 关闭事件
     */
    override fun onChannelClose() {
        Log.i(TAG, "data channel closed")
    }

    /**
     * 收到datachannel消息
     */
    override fun onChannelMessage(msg: ByteBuffer) {
        val bytes = ByteArray(msg.capacity())
        msg.get(bytes)
        val strData = String(bytes, Charset.forName("UTF-8"))
        Log.i(TAG, "Got msg: $strData")
    }


}

