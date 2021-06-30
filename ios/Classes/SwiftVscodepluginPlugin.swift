import Flutter
import UIKit

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
    case "getTelephonyInfo": result(UIDevice.current.model)
      break  
    default:result(FlutterMethodNotImplemented)
    }
    result("iOS " + UIDevice.current.systemVersion)
  }
}
