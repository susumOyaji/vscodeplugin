package com.example.androidphnex

import android.os.Bundle
import java.util.*

class DialerActivity : FlutterActivity() {
    //CallActivity callActivity = new CallActivity();
    var phoneNumberInput: String? = null
    var parameters: String? = null
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(this)
        Toast.makeText(this@DialerActivity, "Started the DialerActivity.app", Toast.LENGTH_SHORT).show()


        //ユーザがDialerユーザインターフェイスを経由せずに通話を開始し、通話を確認することをアプリケーションに許可します。
        ActivityCompat.requestPermissions(this, arrayOf<String>(
                Manifest.permission.CALL_PHONE
        ), REQUEST_CODE)

        //setContentView(R.layout.activity_dialer);
        phoneNumberInput = "123456789" //findViewById(R.id.phoneNumberInput);

        // get Intent data (tel number)
        //if (getIntent().getData() != null)
        //  phoneNumberInput.setText(getIntent().getData().getSchemeSpecificPart());


        //new App();
        MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
                object : MethodCallHandler() {
                    fun onMethodCall(call: MethodCall, result: Result) { //TODO
                        if (call.method.equals("androidphone")) {
                            Toast.makeText(this@DialerActivity, "Started theMethodChannel to makeCall", Toast.LENGTH_SHORT).show()

                            // invokeMethodの第二引数で指定したパラメータを取得できます
                            parameters = call.arguments.toString()
                            val phonestate = makeCall(parameters)
                            if (phonestate != null) {
                                result.success(phonestate) //return to Flutter
                            } else {
                                result.error("UNAVAILABLE", "AndroidPhone not available.", null)
                            }
                        } else {
                            if (call.method.equals("hangup")) {
                                Toast.makeText(this@DialerActivity, "Started theMethodChannel to hangup ", Toast.LENGTH_SHORT).show()
                                // invokeMethodの第二引数で指定したパラメータを取得できます
                                val hangupparameters = call.arguments as Boolean
                                val hangup = hangup(hangupparameters)
                                if (hangup != true) {
                                    result.success(hangup)
                                } else {
                                    result.error("UNAVAILABLE", "Hangup not available.", null)
                                }
                            } else {
                                result.notImplemented() //該当するメソッドが実装されていない
                            } // TOD
                        } // TODO


                        /*
                      if (call.method.equals("hangup")) {
                        Toast.makeText(DialerActivity.this, "Started theMethodChannel to hangup ", Toast.LENGTH_SHORT).show();
                        // invokeMethodの第二引数で指定したパラメータを取得できます
                        boolean hangupparameters = (boolean)call.arguments;
                        boolean hangup = hangup(hangupparameters);
                        
                        if (hangup != true) {
                          result.success(hangup);
                        } else {
                          result.error("UNAVAILABLE", "Hangup not available.", null);
                        }
                      } else {
                        result.notImplemented();
                      } // TODO
                      */
                    }
                }
        )
    }

    fun onStart() {
        super.onStart()
        offerReplacingDefaultDialer()
    }

    @SuppressLint("MissingPermission")
    fun makeCall(_phone: String?): String? {
        // If permission to call is granted
        // 呼び出す権限が付与されている場合
        if (checkSelfPermission(CALL_PHONE) === PERMISSION_GRANTED) {
            // Create the Uri from phoneNumberInput
            val uri: Uri = Uri.parse("tel:$_phone")

            // Start call to the number in input
            startActivity(Intent(Intent.ACTION_CALL, uri))
        } else {
            // Request permission to call
            //呼び出す許可を要求します
            ActivityCompat.requestPermissions(this, arrayOf<String>(CALL_PHONE), REQUEST_PERMISSION)
        }
        val tv = CallActivity.PhoneState
        Toast.makeText(this@DialerActivity, "makeCall  $tv", Toast.LENGTH_SHORT).show()
        return tv
    }

    fun hangup(hangup: Boolean): Boolean {
        Toast.makeText(this@DialerActivity, "hangup  to True ", Toast.LENGTH_SHORT).show()
        //CallActivity.onHangup();
        OngoingCall.hangup()
        return true
    }

    fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String?>?, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(this@DialerActivity, "onRequestPermissionsResult", Toast.LENGTH_SHORT).show()
        val grantRes = ArrayList<Int>()
        // Add every result to the array
        for (grantResult in grantResults) grantRes.add(grantResult)
        if (requestCode == REQUEST_PERMISSION && grantRes.contains(PERMISSION_GRANTED)) {
            makeCall(parameters)
        }
    }

    //ユーザーが自分のアプリをデフォルトの電話アプリとして設定する
    private fun offerReplacingDefaultDialer() {
        val systemService: TelecomManager = this.getSystemService(TelecomManager::class.java)
        if (systemService != null && !systemService.getDefaultDialerPackage().equals(this.getPackageName())) {
            startActivity(Intent(ACTION_CHANGE_DEFAULT_DIALER).putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, this.getPackageName()))
        }
    }

    companion object {
        private const val CHANNEL = "samples.flutter.io/androidphone"
        private const val EXTRA_STRING = "extra_string"
        private const val REQUEST_PERMISSION = 0
        const val REQUEST_CODE = 1
        private const val REQUEST_ID = 1
    }
}