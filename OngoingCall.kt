package com.example.androidphnex

import android.telecom.Call

class OngoingCall {
    val state: BehaviorSubject
        get() = Companion.state

    fun setCall(value: Call?) {
        if (call != null) {
            call.unregisterCallback(callback)
        }
        if (value != null) {
            value.registerCallback(callback)
            Companion.state.onNext(value.getState())
        }
        call = value
    }

    companion object {
        val state: BehaviorSubject<Int>? = null
        private val callback: Call.Callback? = null
        private var call: Call? = null

        // Anwser the call
        fun answer() {
            call.answer(0)
        }

        // Hangup the call
        fun hangup() {
            call.disconnect()
        }

        init {
            // Create a BehaviorSubject to subscribe
            state = BehaviorSubject.create()
            callback = object : Callback() {
                fun onStateChanged(call: Call, newState: Int) {
                    Timber.d(call.toString())
                    // Change call state
                    state.onNext(newState)
                }
            }
        }
    }
}