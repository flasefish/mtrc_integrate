package com.sensetime.mtrc_integrate

import thunder.mrtc.callback.CallEventExecutor
import thunder.mrtc.model.DeviceInfo
import thunder.mrtc.common.ByeReason


import android.util.Log
import android.os.Handler


class CallEvent : CallEventExecutor {
    public lateinit var handler:Handler

    constructor() {}
    constructor(handler: Handler) {
        this.handler = handler
    }

    override fun onCalleeAccept(callee: DeviceInfo?) {
        Log.d("CallEvent", "on callee accept")
        handler.sendEmptyMessage(MessageType.CALLOUT_ACCEPTED)
    }

    override fun onCalleeReject() {
        Log.d("CallEvent", "on callee reject")
        handler.sendEmptyMessage(MessageType.REJECT)
    }

    override fun onCalleeRinging() {
        Log.d("CallEvent", "on callee ringing")
    }

    override fun onCallTimeout() {
        Log.d("CallEvent", "on call timeout")
        handler.sendEmptyMessage(MessageType.REJECT)
    }

    override fun onRemoteBye(byeReason: ByeReason?) {
        Log.d("CallEvent", "callee send remote bye")
        handler.sendEmptyMessage(MessageType.BYE)
    }

    override fun onException(code: Int, message: String?) {
        Log.d("CallEvent", "callee exception")
        handler.sendEmptyMessage(MessageType.NETWORK_DISCONNECT)
    }

}