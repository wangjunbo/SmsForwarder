package com.idormy.sms.forwarder.activity

import android.annotation.SuppressLint
import android.util.Log
import android.view.KeyEvent
import com.idormy.sms.forwarder.R
import com.idormy.sms.forwarder.utils.CommonUtils.Companion.showPrivacyDialog
import com.idormy.sms.forwarder.utils.MMKVUtils
import com.idormy.sms.forwarder.utils.SettingUtils.Companion.isAgreePrivacy
import com.idormy.sms.forwarder.utils.SettingUtils.Companion.isFirstOpen
import com.xuexiang.xui.utils.KeyboardUtils
import com.xuexiang.xui.widget.activity.BaseSplashActivity
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xutil.app.ActivityUtils
import me.jessyan.autosize.internal.CancelAdapt

@Suppress("PropertyName")
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseSplashActivity(), CancelAdapt {

    val TAG: String = SplashActivity::class.java.simpleName

    override fun getSplashDurationMillis(): Long {
        return 500
    }

    /**
     * activity启动后的初始化
     */
    override fun onCreateActivity() {
        initSplashView(R.drawable.xui_config_bg_splash)
        startSplash(false)
    }

    /**
     * 启动页结束后的动作
     */
    override fun onSplashFinished() {
        if (isFirstOpen) {
            isFirstOpen = false
            Log.d(TAG, "从SP迁移数据")
            MMKVUtils.importSharedPreferences(this)
        }

        if (isAgreePrivacy) {
            loginOrGoMainPage()
        } else {
            showPrivacyDialog(this) { dialog: MaterialDialog, _: DialogAction? ->
                dialog.dismiss()
                isAgreePrivacy = true
                loginOrGoMainPage()
            }
        }
    }

    private fun loginOrGoMainPage() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && SettingUtils.enableExcludeFromRecents) {
            val intent = Intent(App.context, if (hasToken()) MainActivity::class.java else LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            App.context.startActivity(intent)
        } else {
            if (hasToken()) {
                ActivityUtils.startActivity(MainActivity::class.java)
            } else {
                ActivityUtils.startActivity(LoginActivity::class.java)
            }
        }*/
        ActivityUtils.startActivity(MainActivity::class.java)
        finish()
    }

    /**
     * 菜单、返回键响应
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event)
    }
}