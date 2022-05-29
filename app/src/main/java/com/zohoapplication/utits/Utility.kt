package com.zohoapplication.utits

import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.zohoapplication.R
import java.util.regex.Pattern

object Utility {
    val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z][a-zA-Z\\-]{1,25}" +
                ")+"
    )

    fun isWhiteSpace(str : String) : Boolean {
        if (str.contains(" ") && (!str.startsWith(" ") && !str.endsWith(" ")))
            return true
        return false
    }

    fun isComma(str : String) : Boolean {
        if (str.contains(","))
            return true
        return false
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun hideKeyboard(context: Context, view: View?) {
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    var pd: ProgressDialog? = null
    fun showProgessDialog(context: Context, message: String) {
        try {
            if (pd != null && pd!!.isShowing) {
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

    fun hideProgessDialog(context: Context) {
        try {
            if (pd != null && pd!!.isShowing) {
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
