package me.eagzzycsl.wecheat.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.text.TextUtils

object PermissionChecker {
    fun isNotificationServiceEnabled(context: Context): Boolean {
        val pkgName = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            names
                    .map { ComponentName.unflattenFromString(it) }
                    .filter { it != null && TextUtils.equals(pkgName, it.packageName) }
                    .forEach { return true }
        }
        return false
    }

    fun isAccessibilityServiceEnabled(context: Context): Boolean {
        var accessibilityEnabled = 0
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.contentResolver,
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            MyLogger.Exception.i("获取access状态失败")
        }

        if (accessibilityEnabled == 1) {
            val services = Settings.Secure.getString(context.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (services != null) {
                return services.toLowerCase().contains(context.packageName.toLowerCase())
            }
        }

        return false
    }


    fun gotoAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }


    fun gotoNotificationSettings(context: Context) {
        context.startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    fun requireAccessibility(context: Context): Boolean {
        if (!this.isAccessibilityServiceEnabled(context)) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("无障碍权限未授予，程序正常工作需要该权限，是否立即授予？")
                    .setNegativeButton("否") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .setPositiveButton("是") { dialogInterface, _ ->
                        this.gotoAccessibilitySettings(context)
                        dialogInterface.dismiss()
                    }
                    .create().show()
            return false;
        }
        return true;
    }

    fun requireNotificationPermission(context: Context): Boolean {
        if (!this.isNotificationServiceEnabled(context)) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("通知权限未授予，程序正常工作需要该权限，是否立即授予？")
                    .setNegativeButton("否") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .setPositiveButton("是") { dialogInterface, _ ->
                        this.gotoNotificationSettings(context)
                        dialogInterface.dismiss()
                    }
                    .create().show()
            return true;
        }
        return false;
    }

}