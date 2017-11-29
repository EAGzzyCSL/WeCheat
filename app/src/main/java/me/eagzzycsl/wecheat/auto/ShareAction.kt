package me.eagzzycsl.wecheat.auto

import android.content.Context
import android.content.Intent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.TextView
import me.eagzzycsl.wecheat.utils.ConstantString
import me.eagzzycsl.wecheat.utils.MyLogger


abstract class ShareAction(private val group:String) : AutoAction() {

    init {

    }

    override fun onAccept(name: String) {
        MyLogger.Accessibility.i("change", name)
        val change = WindowChange.getState(name)
        this.current = this.current?.accept(change)
    }

    //TODO: reject
    override fun onReact(nodeInfo: AccessibilityNodeInfo?) {

    }

    fun pickContact(nodeInfo: AccessibilityNodeInfo?): Boolean {
        return this.findNode(
                nodeInfo,
                TextView::class.java.name,
                group
        )?.parent?.performAction(AccessibilityNodeInfo.ACTION_CLICK) == true;
    }

    override fun onStrike(context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setClassName(
                ConstantString.WeChatPackageName,
                ConstantString.WeChatActivityInfo_ShareImgUI)
        onPutExtra(intent);

        context.startActivity(intent)
    }
    abstract fun onPutExtra( intent:Intent)



    fun performBackClick(nodeInfo: AccessibilityNodeInfo?): Boolean {
        return this.findNode(
                nodeInfo,
                Button::class.java.name,
                ConstantString.WeChatButton_Back
        )?.performAction(AccessibilityNodeInfo.ACTION_CLICK) == true;
    }

}