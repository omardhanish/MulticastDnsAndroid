package com.omardhanishdon.smsreadertask.base

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.ArrayList

abstract class BaseActivity : AppCompatActivity() {

    private var REQUEST_CODE = -1

    protected abstract fun onRequestPermissionsResult(isAllPermissionAccepted: Boolean, notGivenPermission: Array<String?>?, requestCode: Int)

    protected open fun checkPermissions(requestCode: Int, permissions: Array<String?>
    ) {
        var hasPermission = true
        REQUEST_CODE = requestCode
        for (permission in permissions) {
            hasPermission = ContextCompat.checkSelfPermission(this, permission!!) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) break
        }
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        } else {
            onRequestPermissionsResult(true, ArrayList<String?>().toTypedArray(), requestCode
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        var status = true
        val notGivenPermission: MutableList<String?> =
            ArrayList()
        if (REQUEST_CODE == requestCode) {
            if (grantResults.size > 0) {
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        notGivenPermission.add(permissions[i])
                        if (status) status = false
                    }
                }
                onRequestPermissionsResult(status, notGivenPermission.toTypedArray(), requestCode
                )
            } else {
                onRequestPermissionsResult(false, permissions, requestCode)
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}