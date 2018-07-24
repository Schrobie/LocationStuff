package com.schrobieapps.locationtest

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View

//extension method to help with setting clickable spans
fun SpannableString.clickableSpan(spanStart: Int, spanEnd: Int, onClickListener: () -> Unit): SpannableString {
    val clickSpan = object : ClickableSpan() {
        override fun onClick(widget: View?) {
            onClickListener.invoke()
        }
    }
    setSpan(clickSpan,
            spanStart,
            spanEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return this
}