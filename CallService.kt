package com.example.androidphnex

import android.telecom.Call

class CallService : InCallService() {
    fun onCallAdded(call: Call?) {
        OngoingCall().setCall(call)
        Toast.makeText(this, "class to CallService", Toast.LENGTH_SHORT).show()
        CallActivity.start(this, call)
    }

    fun onCallRemoved(call: Call?) {
        OngoingCall().setCall(null)
    }
}