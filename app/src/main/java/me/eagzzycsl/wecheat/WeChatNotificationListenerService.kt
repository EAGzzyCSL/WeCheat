package me.eagzzycsl.wecheat

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import me.eagzzycsl.wecheat.model.Msg
import me.eagzzycsl.wecheat.task.MsgQueue
import me.eagzzycsl.wecheat.utils.ConstantString
import me.eagzzycsl.wecheat.utils.MyLogger

class WeChatNotificationListenerService : NotificationListenerService() {
    val removePreFixCount = Regex("^(\\[\\d*\\条?])?");
    val splitColon = Regex(": ");
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        if (ConstantString.WeChatPackageName != sbn.packageName) {
            return
        }
        val extras = sbn.notification.extras
        if (extras != null) {
            val title = extras.getString(Notification.EXTRA_TITLE, "")
            val content = extras.getString(Notification.EXTRA_TEXT, "")
            MyLogger.Notification.i("获取到通知", title + "&" + content)
            MsgQueue.append(
                    generateMsg(title, content)
            )
        }
    }

    private fun generateMsg(title: String, content: String): Msg {
        val noCount = content.replace(removePreFixCount, "");
        val contactAndMsg = noCount.split(splitColon, 2);
        var c = ""
        if (contactAndMsg.size == 2) {
            c = contactAndMsg[1]
        }

        return Msg(title, contactAndMsg[0], c, "text")
    }

}
