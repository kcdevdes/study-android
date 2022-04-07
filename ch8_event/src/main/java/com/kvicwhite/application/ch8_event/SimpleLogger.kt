package com.kvicwhite.application.ch8_event

import android.util.Log

class SL {
    companion object {
        var TAG : String
        var stElements : Array<StackTraceElement>

        init {
            TAG = "unknown"
            stElements = Thread.currentThread().stackTrace
            for (i in 0..stElements.size) {
                var st = stElements[i]
                if (st.className != SL.javaClass.name && st.className.indexOf("java.lang.thread") != 0) {
                    TAG = st.className
                }
            }
        }

        fun logd(content: String) {
            Log.d(this.TAG, content)
        }
    }
}