package me.eagzzycsl.wecheat.auto

import android.content.Intent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import me.eagzzycsl.wecheat.utils.ConstantString
import me.eagzzycsl.wecheat.utils.MyLogger


class ShareMsgAction(contact: String, private var msg: String) : ShareAction(contact) {
    val state_jumpToPickContact = MachineState("state_jumpToPickContact")
    val state_pickContact = MachineState("state_pickContact")
    val state_confirmSend = MachineState("state_confirmSend")
    val state_confirmSendBefore = MachineState("state_confirmSendBefore");
    val state_chooseBackOrStay = MachineState("state_chooseBackOrStay")
    init {
        state_init.setNext(WindowChange.SHARE_IMG_UI, state_jumpToPickContact)
        state_jumpToPickContact.setNext(WindowChange.SELECT_CONVERSATION_UI, state_pickContact)
        state_pickContact.setNext(WindowChange.NONE, state_confirmSend)
        state_pickContact.setNext(WindowChange.SEND_APP_MESSAGE_WRAPPER_UI, state_confirmSendBefore);
        state_confirmSendBefore.setNext(WindowChange.NONE, state_confirmSend);
        state_confirmSend.setNext(WindowChange.NONE, state_chooseBackOrStay)
    }
    fun performSendClick(nodeInfo: AccessibilityNodeInfo?): Boolean {
        return this.findNode(
                nodeInfo,
                Button::class.java.name,
                ConstantString.WeChatButton_Share
        )?.performAction(AccessibilityNodeInfo.ACTION_CLICK) == true
    }
    override fun onReact(nodeInfo: AccessibilityNodeInfo?) {
        when (current) {
            state_pickContact -> {
                if (this.pickContact(nodeInfo)) else this.resolve()
            }
            state_confirmSend -> {
                if (this.performSendClick(nodeInfo)) else this.resolve()
            }
            state_chooseBackOrStay -> {
                if (this.performBackClick(nodeInfo)) else this.resolve()
                this.resolve()
            }
        }
    }
    override fun onPutExtra(intent: Intent) {
        intent.type = ConstantString.ShareType_text
        intent.putExtra(Intent.EXTRA_TEXT, this.msg)
        MyLogger.Exception.i("发出分享", msg);
    }

}