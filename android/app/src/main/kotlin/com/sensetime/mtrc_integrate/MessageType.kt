package com.sensetime.mtrc_integrate

object MessageType {
    // 登陆
    const val LOGIN = 1001
    // 掉线
    const val LOGOUT = 1002
    const val CALL_IN = 1003
    const val REJECT = 1004
    const val BYE = 1005
    const val CALLOUT_ACCEPTED = 1006
    const val CANCEL_CALL = 1007
    const val REVOTE_BYE = 1008
    const val NETWORK_DISCONNECT = 1009
    const val EXCEPTION = 1010
    const val CHANNELMESSAGE = 1011
    const val ACTIVE_CALL = "active_call"
    const val PASSIVE_CALL = "passive_call"
    const val LOCAL_ID = "local_id"
    const val REMOTE_ID = "remote_id"
    const val CALL_TYPE = "call_type"
    const val ACCEPT_CALL = "accept"
    const val REJECT_CALL = "reject"
}