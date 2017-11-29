package me.eagzzycsl.wecheat.auto

import android.content.Context
import android.view.accessibility.AccessibilityNodeInfo
import me.eagzzycsl.wecheat.utils.MyLogger

abstract class AutoAction {
    open val ignore = false;

    enum class ActionStatus {
        Pending,
        Running,
        Done,
    }

    val inPending: Boolean
        get() = this.status == ActionStatus.Pending;
    val inRunning: Boolean
        get() = this.status == ActionStatus.Running;
    val inDone: Boolean
        get() = this.status == ActionStatus.Done;

    var status = ActionStatus.Pending;
    val state_init = MachineState("init");
    var current: MachineState? = state_init;

    fun accept(name: String) {
        if (this.status != ActionStatus.Running) {
            return;
        }
        this.onAccept(name);
    }

    abstract fun onAccept(name: String)

    fun react(nodeInfo: AccessibilityNodeInfo?) {
        onReact(nodeInfo);
    }

    @Synchronized
    fun resolve() {
        this.status = ActionStatus.Done
    }

    abstract fun onReact(nodeInfo: AccessibilityNodeInfo?)

    fun findNode(nodeInfo: AccessibilityNodeInfo?, widget: String?, text: String): AccessibilityNodeInfo?
            = nodeInfo?.findAccessibilityNodeInfosByText(text)?.firstOrNull { (widget == null || it.className == widget) && it.isEnabled }

    @Synchronized
    fun strike(context: Context) {
        MyLogger.Action.i("AutoAction", "strike")
        this.status = ActionStatus.Running;
        this.onStrike(context)
    }

    abstract fun onStrike(context: Context)
}