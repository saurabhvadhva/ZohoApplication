package com.zohoapplication.utits

import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.zohoapplication.R
import java.util.regex.Pattern

object Utility {

    var pd: ProgressDialog? = null
    fun showProgressDialog(context: Context, message: String) {
        try {
            if (pd != null && pd?.isShowing == true) {
                pd?.dismiss()
            }
            pd = ProgressDialog(context, R.style.AppCompatAlertDialogStyle)
            pd?.setMessage(message)
            pd?.setCancelable(false)
            pd?.setCanceledOnTouchOutside(false)
            pd?.show()
        } catch (e: IllegalArgumentException) {
            // Handle or log or ignore
            e.printStackTrace()
        } catch (e: Exception) {
            // Handle or log or ignore
            e.printStackTrace()
        }
    }

    fun hideProgressDialog() {
        try {
            if (pd != null && pd?.isShowing == true) {
                pd?.dismiss()
            }
        } catch (e: IllegalArgumentException) {
            // Handle or log or ignore
            e.printStackTrace()
        } catch (e: Exception) {
            // Handle or log or ignore
            e.printStackTrace()
        }
    }
}
