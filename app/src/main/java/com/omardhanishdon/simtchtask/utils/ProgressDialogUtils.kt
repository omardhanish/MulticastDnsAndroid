package com.omardhanishdon.simtchtask.utils

import android.app.Activity
import android.app.Dialog
import android.widget.TextView
import com.omardhanishdon.simtchtask.R

object ProgressDialogUtils {

    var dialog : Dialog? = null

    fun showProgressDialog(activity: Activity, message : String = "Please Wait") {
        try {
            dismissDialog()
            dialog = Dialog(activity , R.style.DialogTransparent)

            dialog?.apply {
                setContentView(R.layout.dialog_progressbar)
                setCancelable(false)
                findViewById<TextView>(R.id.message).text = message
                show()
            }
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            dialog = null
        }
    }

    fun dismissDialog() {
        try {
            dialog?.apply {
                dismiss()
            }
            dialog = null
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

}