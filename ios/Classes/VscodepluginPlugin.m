#import "VscodepluginPlugin.h"
#if __has_include(<vscodeplugin/vscodeplugin-Swift.h>)
#import <vscodeplugin/vscodeplugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "vscodeplugin-Swift.h"
#endif

@implementation VscodepluginPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftVscodepluginPlugin registerWithRegistrar:registrar];
}
@end
