package com.example.vscodeplugin_example

//import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant


import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

//import android.database.Cursor;
//import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telecom.Call
import android.net.Uri
import android.widget.Toast

//AndroidX
//import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest
import java.util.*
//import static android.Manifest.permission.CALL_PHONE;


class MainActivity: FlutterActivity() {
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
    }

    private fun getBatteryLevel(): Int {
        val batteryLevel: Int
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
        val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        }

        return batteryLevel
    }


    //@SuppressLint("MissingPermission")
       
    private fun makeCall(_phone: String?): String? {
        // If permission to call is granted
        // 呼び出す権限が付与されている場合
        
        if (ContextCompat.checkSelfPermission(CALL_PHONE) === PERMISSION_GRANTED) {
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
    

    private fun makeCall(context: Context, mob: String) {
        try {
            val intent= Intent(Intent.ACTION_DIAL)
            intent.data= Uri.parse("tel:$mob")
            context.startActivity(intent)
        } catch (e: java.lang.Exception) {
            Toast.makeText(context,
                "Unable to call at this time", Toast.LENGTH_SHORT).show()
        }
    }

    //ダイヤラからの通話の場合（許可は不要）：
    private fun callFromDailer(mContext: Context, number: String) {
        try {
            val callIntent= Intent(Intent.ACTION_DIAL)
            callIntent.data= Uri.parse("tel:$number")
            mContext.startActivity(callIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(mContext, "No SIM Found", Toast.LENGTH_LONG).show()
        }
    }

    //アプリからの直接通話の場合（許可が必要）：
    private fun callDirect(mContext: Context, number: String) {
        try {
            val callIntent= Intent(Intent.ACTION_CALL)
            callIntent.data= Uri.parse("tel:$number")
            mContext.startActivity(callIntent)
        } catch (e: SecurityException) {
            Toast.makeText(mContext, "Need call permission", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(mContext, "No SIM Found", Toast.LENGTH_LONG).show()
        }
    }


    /* 
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
    */


}
