package me.eagzzycsl.wecheat.utils

import android.content.Context
import android.text.TextUtils
import me.eagzzycsl.wecheat.task.ActionQueue
import me.eagzzycsl.wecheat.task.ShuttleService

object RunConfig {
    val actionInterval = 100;
    val shuttleInterval = 1000;
    var actionThreadRunning = false;
    var netThreadRunning = false;
    var signatureKey = ""
    var securityKey = ""

    private var serverUrl = "";
    private var mainUrl = "api/1/wechat_messages/";
    var mainInterface = serverUrl + mainUrl;
    fun loadConfig(context: Context): Boolean {
        val pm = PreferenceManager(context);
        this.signatureKey = pm.getSignatureKey();
        this.securityKey = pm.getSecurityKey();
        this.serverUrl = pm.getServerUrl();
        MyLogger.Exception.i("siKey", signatureKey);
        MyLogger.Exception.i("seKey", securityKey);
        MyLogger.Exception.i("ser", serverUrl);
        joinUrl();
        return check();
    }

    private fun check(): Boolean {
        return !(
                TextUtils.isEmpty(signatureKey) || TextUtils.isEmpty(securityKey) || TextUtils.isEmpty(serverUrl)
                )
    }

    private fun joinUrl() {
        this.mainInterface = serverUrl + mainUrl;
    }

    val running
        get() = this.actionThreadRunning && netThreadRunning;

    fun start(context: Context) {
        this.startAction(context);
        this.startShuttle(context);
    }

    fun startAction(context: Context) {
        this.actionThreadRunning = true;
        ActionQueue.startTask(context);

    }

    fun startShuttle(context: Context) {
        this.netThreadRunning = true;
        ShuttleService.startTask(context);
    }

    fun stop() {
        this.stopAction();
        this.stopShuttle()
    }

    fun stopShuttle() {
        this.netThreadRunning = false;

    }

    fun stopAction() {
        this.actionThreadRunning = false;

    }

}