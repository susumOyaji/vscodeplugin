import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:vscodeplugin/vscodeplugin.dart';

void main() {
  const MethodChannel channel = MethodChannel('vscodeplugin');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Vscodeplugin.platformVersion, '42');
  });
}
