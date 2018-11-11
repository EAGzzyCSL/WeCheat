package me.eagzzycsl.wecheat

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.pm.PackageManager
import android.view.accessibility.AccessibilityEvent
import me.eagzzycsl.wecheat.task.ActionQueue
import me.eagzzycsl.wecheat.utils.MyLogger


class AutoService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event != null && event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val activityComponentName = ComponentName(
                    event.packageName.toString(),
                    event.className.toString()
            )
            try {
                val packageManager = packageManager
                val activityInfo = packageManager.getActivityInfo(activityComponentName, 0)
                ActionQueue.accept(activityInfo.name);
            } catch (e: PackageManager.NameNotFoundException) {
                ActionQueue.accept("");
            }
            if(rootInActiveWindow!=null){
                ActionQueue.react(rootInActiveWindow);
            }else {
                MyLogger.Exception.e("rootInActiveWindow == null")
            }
        }
    }

    override fun onCreate() {}


    override fun onInterrupt() {

    }
}
