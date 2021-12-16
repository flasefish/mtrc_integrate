package com.sensetime.mtrc_integrate

import android.content.Context
import android.os.Handler
import android.util.Log
import org.json.JSONObject
import thunder.mrtc.MrtcOperator
import thunder.mrtc.model.MrtcSetupParam

class MtrcControl{
   /* companion object {
        var impl: MtrcControl = MtrcControl()
        fun getInstance(): MtrcControl {
            return impl
        }
    }*/

    public lateinit var mrtcOperator: MrtcOperator
    private lateinit var onLineEventExecutor: OnLineEvent
    private lateinit var handler: Handler

    public fun initMrtc(context: Context) {
        val mrtcSetupParam = MrtcSetupParam("ws://14.215.130.139:18081/ws", "stun:14.215.130.139:3478", true, context)
        mrtcOperator = MrtcOperator.getInstance()
        mrtcOperator.setup(mrtcSetupParam)
        Log.d("zhoud","initMrtc")
    }

    public fun login(loginId:String){
        try {
            onLineEventExecutor =  OnLineEvent()
            onLineEventExecutor.handler = handler

            var strOnlineExtraData: String? = null
            val extraDataMap: MutableMap<Any?, Any?> = HashMap<Any?, Any?>()
            val valueDataMap: MutableMap<Any?, Any?> = HashMap<Any?, Any?>()
            valueDataMap.put("channel", "pass") //取值{pass, web，mobile}
            valueDataMap.put("type", 1) //取值{  1：门口机 2：室内机 3：管理机}
            extraDataMap.put("onlineExtraData", valueDataMap)
            val jsonObject = JSONObject(extraDataMap)
            if (jsonObject != null) {
                strOnlineExtraData = jsonObject.toString()
            }
            mrtcOperator.online(loginId, "token-device-1:776e83532848482db216474f47160588", strOnlineExtraData, 0, 2, 3, onLineEventExecutor)
            Log.d("zhoud","login")
        } catch (e: Exception) {
            Log.d("zhoud", e.message.toString())
        }
    }

    class LoginHandler : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MessageType.LOGIN -> Log.d("zhoud", "MessageType.LOGIN")
                MessageType.LOGOUT -> Log.d("zhoud", "MessageType.LOGOUT")
                MessageType.CALL_IN -> {
                    Log.d("zhoud", "MessageType.CALL_IN")
                }
                else -> {
                    Log.d("zhoud", "MessageType.unknow")
                }
            }
        }
    }




}