package me.eagzzycsl.wecheat

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.EditText

import me.eagzzycsl.wecheat.model.Msg
import me.eagzzycsl.wecheat.task.ActionQueue

class TrickActivity : ToolbarActivity(), View.OnClickListener {
    private var etContact: EditText? = null;
    private var etContents: EditText? = null;
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_trick -> {
                val actions = etContents?.text?.split("\n")?.map {
                    Msg(etContact?.text.toString(), "", it, "text").generateAction()
                }?.toTypedArray()
                if (actions != null) {
                    ActionQueue.appendAll(actions)
                }
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_trick
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        etContact = findViewById<EditText>(R.id.et_contact)
        etContents = findViewById<EditText>(R.id.et_contents)
        val fabTrick = findViewById<FloatingActionButton>(R.id.fab_trick);
        fabTrick.setOnClickListener(this);
    }

}
