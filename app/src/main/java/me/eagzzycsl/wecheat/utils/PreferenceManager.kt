package me.eagzzycsl.wecheat.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val preName = "me.eagzzycsl.wecheat_preferences"
    private val sp: SharedPreferences;

    fun getSecurityKey(): String {
        return sp.getString("securityKey", "")
    }

    fun getSignatureKey(): String {
        return sp.getString("signatureKey", "");
    }

    fun getServerUrl(): String {
        return sp.getString("serverUrl", "");
    }

    init {
        this.sp = context.getSharedPreferences(preName, Context.MODE_PRIVATE)
    }


}
