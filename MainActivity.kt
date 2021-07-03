package com.example.androidphnex
//orignal to Add
import android.os.Bundle

class MainActivity : FlutterActivity() {
    /*
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
                new MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, Result result) {
                      if (call.method.equals("getBatteryLevel")) {
                        int batteryLevel = getBatteryLevel();

                        if (batteryLevel != -1) {
                          result.success(batteryLevel);
                        } else {
                          result.error("UNAVAILABLE", "Battery level not available.", null);
                        }
                      } else {
                        result.notImplemented();
                      }   // TODO
                    }
                });
  }//onCreate
  */
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(this)
        App()
        Toast.makeText(this@MainActivity, "Started the MainActivity.app", Toast.LENGTH_SHORT).show()
        val CallService = CallService()
        //CallService.onCallAdded();
        MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
                object : MethodCallHandler() {
                    fun onMethodCall(call: MethodCall, result: Result) { //TODO
                        Toast.makeText(this@MainActivity, "Started theMethodChannel ", Toast.LENGTH_SHORT).show()
                        if (call.method.equals("androidphone")) {
                            val phonestate = TelPhoneCall()
                            if (phonestate != -1) {
                                result.success(phonestate)
                            } else {
                                result.error("UNAVAILABLE", "AndroidPhone not available.", null)
                            }
                        } else {
                            result.notImplemented()
                        } // TODO
                    }
                }
        )
    }

    private fun TelPhoneCall(): Int {
        Toast.makeText(this@MainActivity, "TelPhoneDial", Toast.LENGTH_SHORT).show()
        val phonestate = -1
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            OngoingCall.answer()
        }
        return phonestate
    }

    companion object {
        private const val CHANNEL = "samples.flutter.io/androidphone"
        private const val PERMISSION_REQUEST_READ_PHONE_STATE = 1
        private val call: Call? = null
    }
}