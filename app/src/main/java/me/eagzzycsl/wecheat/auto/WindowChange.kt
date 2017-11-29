package me.eagzzycsl.wecheat.auto

import me.eagzzycsl.wecheat.utils.ConstantString

enum class WindowChange (private val changeName:String) {
    INIT("~"),
    SHARE_IMG_UI(ConstantString.WeChatActivityInfo_ShareImgUI),
    SHARE_RetransmitUI(ConstantString.WeChatActivityInfo_MsgRetransmitUI),
    SELECT_CONVERSATION_UI(ConstantString.WeChatActivityInfo_SelectConversation),
    SEND_APP_MESSAGE_WRAPPER_UI(ConstantString.WeChatActivityInfo_SendAppMessageWrapperUI),
    NONE(""),
    OUT_OF_EXCEPTION("!");

    private fun match(name: String): Boolean = this.changeName ==name

    companion object {
        fun getState(name: String): WindowChange {
            return WindowChange.values().firstOrNull { it.match(name) }?: OUT_OF_EXCEPTION
        }
    }

}