package me.eagzzycsl.wecheat.utils


object ConstantString {

    val WeChatPackageName = "com.tencent.mm"
    private val WECHATUIName_MSGRETRASMITUI = "ui.transmit.MsgRetransmitUI"
    private val WeChatUIName_SelectConversationUiName = "ui.transmit.SelectConversationUI"
    private val WeChatUIName_ShareImgUI = "ui.tools.ShareImgUI"
    private val WeChatUIName_chatting="chatting.En_5b8fbb1e"
    private val WeChatUIName_SendAppMessageWrapperUI = "ui.transmit.SendAppMessageWrapperUI"

    val WeChatActivityInfo_SelectConversation =
            WeChatPackageName + "." + WeChatUIName_SelectConversationUiName
    val WeChatActivityInfo_ShareImgUI =
            WeChatPackageName + "." + WeChatUIName_ShareImgUI
    val WeChatActivityInfo_SendAppMessageWrapperUI =
            WeChatPackageName + "." + WeChatUIName_SendAppMessageWrapperUI
    val WeChatActivityInfo_MsgRetransmitUI = WeChatPackageName+"." +WECHATUIName_MSGRETRASMITUI
    val SharePick_WeChat = "微信"
    val WeChatButton_Share = "分享"
    val WeChatButton_Send ="发送"
    val WeChatButton_Back = "返回第三方工具"
    val WeChatButton_stay = "留在微信"
    val ShareType_text = "text/plain"
    val ShareType_img = "image/*"
    val shareImgName="imgNameOfShare"
}
