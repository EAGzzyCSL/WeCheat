package me.eagzzycsl.wecheat.auto

import android.content.Context
import android.view.accessibility.AccessibilityNodeInfo

class NotNullAction : AutoAction() {
    override val ignore = true;
    override fun onStrike(context: Context) {
    }

    override fun onReact(nodeInfo: AccessibilityNodeInfo?) {
    }

    override fun onAccept(name: String) {
    }

}