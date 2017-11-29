package me.eagzzycsl.wecheat.model

import android.net.Uri
import me.eagzzycsl.wecheat.auto.ShareAction
import me.eagzzycsl.wecheat.auto.ShareImgAction
import me.eagzzycsl.wecheat.auto.ShareMsgAction
import java.io.File


class Msg(private val group: String,
          val contact: String,
          val msg: String,
          val type: String = "text"
) {
    private var img: Uri? = null
    fun generateAction(): ShareAction {
        return when (this.type) {
            "text" -> {
                ShareMsgAction(group, msg);
            }
            "img" -> {
                ShareImgAction(group, img);
            }
            else -> {
                ShareMsgAction(group, "消息类别未知");
            }
        }
    }

    fun setImg(img: Uri) {
        this.img = img;
    }
}