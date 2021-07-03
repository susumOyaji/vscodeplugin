package com.example.androidphnex

import android.annotation.SuppressLint
import timber.log.Timber

internal object CallStateString {
    @SuppressLint("TimberArgCount")
    fun asString(`$receiver`: Int): String {
        val state: String
        state = when (`$receiver`) {
            0 -> "NEW"
            1 -> "DIALING"
            2 -> "RINGING"
            3 -> "HOLDING"
            4 -> "ACTIVE"
            5, 6 -> {
                Timber.w("Unknown state %s", `$receiver`, arrayOfNulls<Any>(0))
                "UNKNOWN"
            }
            7 -> "DISCONNECTED"
            8 -> "SELECT_PHONE_ACCOUNT"
            9 -> "CONNECTING"
            10 -> "DISCONNECTING"
            else -> {
                Timber.w("Unknown state %s", `$receiver`, arrayOfNulls<Any>(0))
                "UNKNOWN"
            }
        }
        return state
    }
}