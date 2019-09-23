package com.shuisechanggong.ktdemo.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.shuisechanggong.base.BaseActivity
import com.shuisechanggong.ktdemo.R
import com.shuisechanggong.violet.ScanQRCodeFragment
import kotlinx.android.synthetic.main.activity_qrcode.*

class QRCodeActivity : BaseActivity() {

    override fun initViewModel(provider: ViewModelProvider) {
    }

    override fun initViews() {
        setContentView(R.layout.activity_qrcode)
        toolbar.setNavigationOnClickListener { finish() }



        btnOpenInBrowser.setOnClickListener{

            val content = edtContent.text.toString()
            if (content.trim().isNotEmpty()) {
                try {
                    openBrowser(content)
                } catch (e: Exception) {
                    openBrowser("http://" + content)
                }
            }
        }

        btnOpenCamera.setOnClickListener {

            if (!checkPermission(Manifest.permission.CAMERA)) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA),0x001)
                } else {
                    AlertDialog.Builder(this)
                            .setMessage("请设置权限")
                            .setPositiveButton("确定",object :DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {

                                    val packageURI = Uri.parse("package:"+ packageName)
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                                    startActivity(intent)
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show()
                }
                return@setOnClickListener
            }



            val tag = "qrcode"
            var scanQRCodeFragment = supportFragmentManager.findFragmentByTag(tag)
            if (scanQRCodeFragment == null) {
                val fragment = ScanQRCodeFragment()
                fragment.onQRCodeResultListener={
                    edtContent.setText(it)
                    edtContent.setSelection(it?.length ?: 0)
                    supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
                }


                scanQRCodeFragment = fragment
            }

            supportFragmentManager.beginTransaction().replace(R.id.container,scanQRCodeFragment, tag).commitAllowingStateLoss()

        }

    }

    private fun openBrowser(content: String) {
        val content = Uri.parse(content)
        val intent = Intent(Intent.ACTION_VIEW, content)
        startActivity(intent)
    }


    fun checkPermission(permission:String):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }

        return true
    }
}
