package com.omardhanishdon.smsreadertask.utils

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.util.Predicate

object CommonUtils {

    fun showToastLong(message: String , context: Context?){
        context?.apply {
            Toast.makeText(context , message , Toast.LENGTH_LONG).show()
        }
    }

    fun showToastShort(message: String , context: Context?){
        context?.apply {
            Toast.makeText(context , message , Toast.LENGTH_SHORT).show()
        }
    }

    fun getSdkVersionNumber() : Int{
        return Build.VERSION.SDK_INT
    }

    fun <T> removeFilter(list: MutableList<T>, predicate: Predicate<T>) {
        val itr = list.iterator()

        while (itr.hasNext()) {
            val t = itr.next()
            if (predicate.test(t)) {
                itr.remove()
            }
        }
    }

}