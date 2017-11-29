package me.eagzzycsl.wecheat.task

import me.eagzzycsl.wecheat.model.Msg
import java.util.*

object MsgQueue {
    private val messages: LinkedList<Msg> = LinkedList<Msg>()
    @Synchronized
    fun pollAll(): Array<Msg> {
        val arr = messages.toTypedArray();
        messages.clear();
        return arr;
    }

    @Synchronized
    fun append(msg: Msg) {
        messages.offer(msg)
    }
}