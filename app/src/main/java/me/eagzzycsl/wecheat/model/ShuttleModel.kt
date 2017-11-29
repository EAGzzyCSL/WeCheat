package me.eagzzycsl.wecheat.model

import com.google.gson.Gson
import me.eagzzycsl.wecheat.utils.RunConfig
import me.eagzzycsl.wecheat.utils.Security

data class ShuttleModel(private val msgList: String, val signature: String) {

    fun getMsgList(): Array<Msg> {
//        val decryptedMsgList = Security.decrypt(msgList, RunConfig.securityKey);
//        val decryptedMsgList = Security.decrypt(msgList, RunConfig.securityKey);

        return Gson().fromJson(msgList, Array<Msg>::class.java);
    }
}