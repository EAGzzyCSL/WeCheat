package me.eagzzycsl.wecheat.auto

import android.content.Intent
import me.eagzzycsl.wecheat.utils.ConstantString
import me.eagzzycsl.wecheat.utils.MyLogger
import java.io.File
import android.net.Uri
import android.support.v4.content.FileProvider
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.TextView


class ShareImgAction(contact: String, private val imgUri: Uri?) : ShareAction(contact) {
    val state_pickContact = MachineState("state_pickContact")
    val state_confirmSend = MachineState("state_confirmSend")
    val state_chooseBackOrStay = MachineState("state_chooseBackOrStay")

    init {
        state_init.setNext(WindowChange.SHARE_RetransmitUI, state_pickContact)
        state_init.setNext(WindowChange.SELECT_CONVERSATION_UI, state_pickContact)
        state_pickContact.setNext(WindowChange.NONE, state_confirmSend)
        state_confirmSend.setNext(WindowChange.NONE, state_chooseBackOrStay)
    }

    override fun onReact(nodeInfo: AccessibilityNodeInfo?) {
        println(current)
        when (current) {
            state_pickContact -> {
                if (this.pickContact(nodeInfo)) else this.resolve()
            }
            state_confirmSend -> {
                if (this.performSendClick(nodeInfo)) else this.resolve()
            }
            state_chooseBackOrStay -> {
//                if (this.performBackClick(nodeInfo)) else this.resolve()
                this.resolve()
            }
        }
    }

    fun performSendClick(nodeInfo: AccessibilityNodeInfo?): Boolean {
        return this.findNode(
                nodeInfo,
                Button::class.java.name,
                ConstantString.WeChatButton_Send
        )?.performAction(AccessibilityNodeInfo.ACTION_CLICK) == true;
    }

    override fun onPutExtra(intent: Intent) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.type = ConstantString.ShareType_img
        intent.putExtra(Intent.EXTRA_STREAM, imgUri)
        MyLogger.Accessibility.i("发出分享", imgUri.toString());
    }

}