package com.example.androidphnex

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.androidphnex.CallActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class CallActivity : Activity() {
    private val disposables: CompositeDisposable = CompositeDisposable()
    private var number: String? = null
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this@CallActivity, "CallActivity", Toast.LENGTH_SHORT).show()
        setContentView(R.layout.activity_call)
        answer = findViewById(R.id.answer)
        hangup = findViewById(R.id.hangup)
        callInfo = findViewById(R.id.callInfo)
        number = getIntent().getData().getSchemeSpecificPart()
    }

    @SuppressLint("CheckResult")
    fun onStart() {
        super.onStart()
        answer.setOnClickListener({ v -> OngoingCall.answer() })
        hangup.setOnClickListener({ v -> OngoingCall.hangup() })

        // Subscribe to state change -> call updateUi when change
        OngoingCall()
        val disposable: Disposable = OngoingCall.state.subscribe { state: Int -> updateUi(state) }
        disposables.add(disposable)

        // Subscribe to state change (only when disconnected) -> call finish to close phone call
        OngoingCall()
        val disposable2: Disposable = OngoingCall.state
                .filter { state -> state === Call.STATE_DISCONNECTED }
                .delay(1, TimeUnit.SECONDS)
                .firstElement()
                .subscribe({ state: Int? -> finish(state) })
        disposables.add(disposable2)
    }

    // Call to Activity finish
    fun finish(state: Int?) {
        finish()
    }

    // Set the UI for the call
    @SuppressLint("SetTextI18n")
    fun updateUi(state: Int) {
        Toast.makeText(this@CallActivity, "updateUi    $state", Toast.LENGTH_SHORT).show()

        // Set callInfo text by the state
        callInfo.setText(CallStateString.asString(state).toLowerCase() + "   " + number)
        PhoneState = callInfo.getText().toString()
        if (state === Call.STATE_RINGING) answer.setVisibility(View.VISIBLE) // ボタンを表示する
        else answer.setVisibility(View.GONE) // ボタンを非表示にする
        if (state === Call.STATE_DIALING || state === Call.STATE_RINGING || state === Call.STATE_ACTIVE) hangup.setVisibility(View.VISIBLE) else hangup.setVisibility(View.GONE)
    }

    fun onStop() {
        super.onStop()
        disposables.clear()
    }

    companion object {
        private var answer: Button? = null
        private var hangup: Button? = null
        private var callInfo: TextView? = null
        @JvmField
        var PhoneState: String? = null

        @JvmStatic
        @SuppressLint("NewApi")
        fun start(context: Context, call: Call) {
            context.startActivity(Intent(context, CallActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setData(call.getDetails().getHandle()))
        }
    }
}