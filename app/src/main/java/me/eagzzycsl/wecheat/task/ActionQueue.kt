package me.eagzzycsl.wecheat.task

import android.content.Context
import android.view.accessibility.AccessibilityNodeInfo
import me.eagzzycsl.wecheat.auto.AutoAction
import me.eagzzycsl.wecheat.auto.NotNullAction
import me.eagzzycsl.wecheat.utils.RunConfig
import java.util.*


object ActionQueue {
    val actions: LinkedList<AutoAction> = LinkedList<AutoAction>()
    private val notNullAction = NotNullAction();
    private var inAction: AutoAction = notNullAction;
    fun startTask(context: Context) {
        Thread({
            while (RunConfig.actionThreadRunning) {
                synchronized(inAction) {
                    if (inAction.ignore || inAction.inDone) {
                        inAction = if (actions.size > 0) actions.poll() else notNullAction;
                    } else if (inAction.inPending) {
                        inAction.strike(context)
                    }
                }
                Thread.sleep(RunConfig.actionInterval.toLong())
            }
        }).start();
    }

    @Synchronized
    fun append(action: AutoAction) {
        actions.offer(action);

    }

    @Synchronized
    fun appendAll(newActions: Array<out AutoAction>) {
        actions.addAll(newActions);
    }

    fun accept(name: String) {
        inAction.accept(name);
    }

    fun react(nodeInfo: AccessibilityNodeInfo) {
        inAction.react(nodeInfo)
    }

}
