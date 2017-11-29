package me.eagzzycsl.wecheat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.FileProvider
import android.widget.*
import me.eagzzycsl.wecheat.utils.*
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*
import me.eagzzycsl.wecheat.model.Msg
import me.eagzzycsl.wecheat.task.ActionQueue
import me.eagzzycsl.wecheat.task.MsgQueue
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat


class MainActivity : ToolbarActivity(), View.OnClickListener {

    private var fabPower: FloatingActionButton? = null;
    private var tbtActionStatus: ToggleButton? = null;
    private var tbtShuttleStatus: ToggleButton? = null;
    private var tbtAutoStatus: ToggleButton? = null;
    private var tbtNotifyStatus: ToggleButton? = null;
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
    //        val intent = Intent(Intent.ACTION_SEND)
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setClassName(
//                ConstantString.WeChatPackageName,
//                ConstantString.WeChatActivityInfo_ShareImgUI)
//        intent.type = ConstantString.ShareType_img
//        intent.putExtra(Intent.EXTRA_STREAM, photoURI)
//        startActivity(intent)
    fun test() {
        val msg = Msg("Hackathon", "zhangsan", "test", "img");

        msg.setImg(
                FileProvider.getUriForFile(
                        this, this.applicationContext.packageName,
                        this.externalCacheDir.resolve("avatar.jpg")
                )
        )
        ActionQueue.append(msg.generateAction());
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.myFind()
        this.mySet();
//        test()
    }

    override fun onResume() {
        super.onResume();
        this.updateToggleButtonStatus()
    }

    private fun myFind() {
        fabPower = findViewById<FloatingActionButton>(R.id.fab_toggle);
        tbtShuttleStatus = findViewById<ToggleButton>(R.id.tbt_status_shuttle)
        tbtActionStatus = findViewById<ToggleButton>(R.id.tbt_status_action);
        tbtAutoStatus = findViewById<ToggleButton>(R.id.tbt_status_auto);
        tbtNotifyStatus = findViewById<ToggleButton>(R.id.tbt_status_notify);
    }

    private fun mySet() {
        fabPower?.setOnClickListener(this);
        tbtShuttleStatus?.setOnClickListener(this);
        tbtActionStatus?.setOnClickListener(this);
        tbtNotifyStatus?.setOnClickListener(this);
        tbtAutoStatus?.setOnClickListener(this);
    }

    private fun updateToggleButtonStatus() {
        tbtActionStatus?.isChecked = RunConfig.actionThreadRunning
        tbtShuttleStatus?.isChecked = RunConfig.netThreadRunning
        tbtAutoStatus?.isChecked = PermissionChecker.isAccessibilityServiceEnabled(this)
        tbtNotifyStatus?.isChecked = PermissionChecker.isNotificationServiceEnabled(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.fab_toggle -> {
                this.toggle();
            }
            R.id.tbt_status_action -> {
                if ((v as ToggleButton).isChecked) {
                    RunConfig.startAction(this);
                } else {
                    RunConfig.stopAction();
                }
            }
            R.id.tbt_status_shuttle -> {
                if ((v as ToggleButton).isChecked) {
                    RunConfig.startShuttle(this);
                } else {
                    RunConfig.stopShuttle();
                }
            }
            R.id.tbt_status_notify -> {
                PermissionChecker.gotoNotificationSettings(this);
            }
            R.id.tbt_status_auto -> {
                PermissionChecker.gotoAccessibilitySettings(this);
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        RunConfig.stop();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.action_about -> startActivity(Intent(this, AboutActivity::class.java))
            R.id.action_trick -> startActivity(Intent(this, TrickActivity::class.java))
        }
        return true
    }

    private fun toggle() {
        if (RunConfig.running) {
            RunConfig.stop();
        } else {
            if (RunConfig.loadConfig(this)) {
                if (PermissionChecker.requireAccessibility(this) && PermissionChecker.requireAccessibility(this)) {
                    RunConfig.start(this);
                    printLog("服务启动");
                } else {
                    printLog("需要手动授予权限");
                }
            } else {
                printLog("启动服务失败，配置有问题");
            }
        }
        this.updateToggleButtonStatus();
    }

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("HH:mm a");

    @SuppressLint("SetTextI18n")
    private fun printLog(log: String) {
        val time = dateFormat.format(Calendar.getInstance().time)
        val line = "\n" + time + " " + log;
        text_log.text = text_log.text.toString() + line;
    }

}
