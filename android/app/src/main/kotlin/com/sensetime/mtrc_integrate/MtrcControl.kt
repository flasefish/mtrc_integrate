package com.sensetime.mtrc_integrate

import android.content.Context
import android.os.Handler
import android.util.Log
import org.json.JSONObject
import thunder.mrtc.MrtcOperator
import thunder.mrtc.model.MrtcSetupParam
import thunder.mrtc.MrtcRender
import thunder.mrtc.callback.CallEventExecutor

import thunder.mrtc.Call
import thunder.mrtc.callback.HangupEventExecutor
import thunder.mrtc.model.MediaAttributes
import thunder.mrtc.common.AttributeValue
import thunder.mrtc.common.ByeReason

class MtrcControl{
    public  lateinit var call:Call
    public  lateinit var mrtcOperator: MrtcOperator
    private lateinit var onLineEventExecutor: OnLineEvent
    private lateinit var handler: Handler
    private lateinit var callEvent: CallEventExecutor
    private lateinit var callhandler :Handler


    public fun initMrtc(context: Context,mtrcLoginHandle : Handler,mtrcCallHandler:Handler) {
        val mrtcSetupParam = MrtcSetupParam("ws://14.215.130.139:18081/ws", "stun:14.215.130.139:3478", true, context)
        mrtcOperator = MrtcOperator.getInstance()
        mrtcOperator.setup(mrtcSetupParam)

        handler = mtrcLoginHandle
        callhandler = mtrcCallHandler
        Log.d("zhoud","initMrtc")
    }

    public fun login(loginId:String){
        Log.d("zhoud","MtrcControl login call")
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
            Log.d("zhoud","MtrcControl login")
        } catch (e: Exception) {
            Log.d("zhoud", e.message.toString())
        }
    }

    public fun call(remoteId:String,view:MrtcRender){
        callEvent = CallEvent(callhandler)
        Log.d("zhoud","start call " +remoteId)
        try {
            mrtcOperator = MrtcOperator.getInstance()
            mrtcOperator.setRemoteRender(view)

            val mediaAttributes = MediaAttributes()
            mediaAttributes.setVideoAttributeValue(AttributeValue.SENDRECV)
            mediaAttributes.setAudioAttributeValue(AttributeValue.SENDRECV)
            var extraData: String? = null
            val extraDataMap: MutableMap<Any?, Any?> = HashMap<Any?, Any?>()
            val valueDataMap: MutableMap<Any?, Any?> = HashMap<Any?, Any?>()
            // val extraDataMap: MutableMap<String?, Any?> = HashMap<Any?, Any?>()
            // val valueDataMap: MutableMap<String?, Any?> = HashMap<Any, Any?>()
            valueDataMap.put("location", "SenseThunderE-F00095") //取值{pass, web，mobile}
            valueDataMap.put("device_name", "SenseThunderE-F00095") //取值{  1：门口机 2：室内机 3：管理机}
            valueDataMap.put("device_ldi", "SPSPE-40133fb330a749200682322e640f645d")
            extraDataMap.put("callExtraData", valueDataMap)
            val jsonObject = JSONObject(extraDataMap)
            if (jsonObject != null) {
                extraData = jsonObject.toString()
            }
            this.call = mrtcOperator.call(remoteId, extraData, mediaAttributes, 10, callEvent)
        } catch (e: Exception) {
            Log.e("zhoud", e.message.toString())
        }
    }

    public fun hangup(){
        try {
            this.call.hangup(object : HangupEventExecutor {
                override fun onSuccess() {
                    Log.i("zhoud", "hang up success")
                    callhandler.sendEmptyMessage(MessageType.HANDUP_OK)
                }

                override fun onFail(code: Int, message: String?) {
                    Log.i("zhoud", "hang up onFail")
                    callhandler.sendEmptyMessage(MessageType.HANDUP_FAIL)
                }
            }, ByeReason.INITIATIVE)

        } catch (e: Exception) {
            Log.e("zhoud", e.toString())
        }
    }





}