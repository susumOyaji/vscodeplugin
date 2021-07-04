package com.example.vscodeplugin

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar


//import android.os.Bundle
import android.widget.Toast
import android.annotation.SuppressLint 

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;


///import io.flutter.plugin.common.android.telephony.TelephonyManager;

/** VscodepluginPlugin */
public class VscodepluginPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  var parameters: String? = null


  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "vscodeplugin")
    channel.setMethodCallHandler(this);
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "vscodeplugin")
      channel.setMethodCallHandler(VscodepluginPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "getPlatformBattery") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "getTelephonyInfo") {
      result.success("Android  Telephony ${android.os.Build.VERSION.RELEASE}")  
    } else if (call.method == "androidphone") {
      //Toast.makeText(this@VscodepluginPlugin, "Started theMethodChannel to makeCall", Toast.LENGTH_SHORT).show()

      // invokeMethodの第二引数で指定したパラメータを取得できます
      parameters = call.arguments.toString()
      val phonestate = makeCall(parameters)
      if (phonestate != null) {
          result.success(phonestate) //return to Flutter
      } else {
          result.error("UNAVAILABLE", "AndroidPhone not available.", null)
      }
    } else if (call.method == "hangup") {
          //Toast.makeText(this@VscodepluginPlugin, "Started theMethodChannel to hangup ", Toast.LENGTH_SHORT).show()
          // invokeMethodの第二引数で指定したパラメータを取得できます
          val hangupparameters = call.arguments as Boolean
          val hangup = hangup(hangupparameters)
          if (hangup != true) {
              result.success(hangup)
          } else {
              result.error("UNAVAILABLE", "Hangup not available.", null)
          }
      //} else {
      //    result.notImplemented() //該当するメソッドが実装されていない
      //} // TOD
    //} // TODO
  } else {
    result.notImplemented() //該当するメソッドが実装されていない
  }
}


  @SuppressLint("MissingPermission")
  fun makeCall(_phone: String?): String? {
      // If permission to call is granted
      //アクセス権限を与える仕組みをPermissionという。
      //アクセス権限が許可されているかどうかをcheckSelfPermissionメソッドを使用して確認いたします。
      if (PermissionChecker.checkSelfPermission(CALL_PHONE) === PERMISSION_GRANTED) {
          // Create the Uri from phoneNumberInput
          val uri: Uri = Uri.parse("tel:$_phone")

          // Start call to the number in input
          startActivity(Intent(Intent.ACTION_CALL, uri))
      } else {
          // Request permission to call
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

    //companion object {
    //    private const val CHANNEL = "samples.flutter.io/androidphone"
    //    private const val EXTRA_STRING = "extra_string"
    //    private const val REQUEST_PERMISSION = 0
    //    const val REQUEST_CODE = 1
    //    private const val REQUEST_ID = 1
    //}
//}

  

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
