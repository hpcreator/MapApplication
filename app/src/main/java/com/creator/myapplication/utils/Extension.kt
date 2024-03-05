package com.creator.myapplication.utils

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.creator.myapplication.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

fun View.setSafeClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun View.showSnackBar(message: String, @StringRes actionString: Int = R.string.str_dismiss, action: (() -> Unit)? = null) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    val textView: TextView = snackBar.view.findViewById(com.google.android.material.R.id.snackbar_text)
    textView.textSize = 12F
    textView.maxLines = 4
    snackBar.setAction(actionString) {
        if (action != null) {
            action.invoke()
        } else {
            snackBar.dismiss()
        }
    }
    snackBar.setActionTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
    snackBar.show()
}

fun Long.toStringDate(format: String = "dd/MM/yyyy"): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.format(this)
}
