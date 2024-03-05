package com.creator.myapplication.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:date")
fun setDateFromLong(textView: TextView, date: Long) {
    textView.text = date.toStringDate()
}