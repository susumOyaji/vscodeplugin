import Flutter
import UIKit
import PushKit

public class SwiftVscodepluginPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "vscodeplugin", binaryMessenger: registrar.messenger())
    let instance = SwiftVscodepluginPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getPlatformVersion": result("iOS " + UIDevice.current.systemVersion)
    case "getPlatformBattery": result("BATTERY " + String(UIDevice.current.batteryLevel))
    case "getTelephonyInfo": result(self.callNumber(phoneNumber:"7178881234"))
      break  
    default:result(FlutterMethodNotImplemented)
    }
    result("iOS " + UIDevice.current.systemVersion)
  }
   ///case "getTelephonyInfo": result(UIDevice.current.model)
   ///    result(callNumber(phoneNumber:"7178881234"))
  private func callNumber(phoneNumber:String) {
  if let url = URL(string: "tel://\(phoneNumber)"),
   UIApplication.shared.canOpenURL(url) {
      if #available(iOS 10, *) {
        UIApplication.shared.open(url, options: [:], completionHandler:nil)
       } else {
           UIApplication.shared.openURL(url)
       }
   } else {
            // add error message here 
   }
}
  


}
