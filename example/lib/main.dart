import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:vscodeplugin/vscodeplugin.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _platformBattery = 'Unknown';
  String _platformTelephony = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    String platformBattery;
    String platformTelephony;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await Vscodeplugin.platformVersion ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    try {
      platformBattery =
          await Vscodeplugin.platformBattery ?? 'Unknown platform version';
    } on PlatformException {
      platformBattery = 'Failed to get platform version.';
    }

    try {
      platformTelephony =
          await Vscodeplugin.platformTelephony ?? 'Unknown platform version';
    } on PlatformException {
      platformTelephony = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
      _platformBattery = platformBattery;
      _platformTelephony = platformTelephony;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('Plugin example app'),
          ),
          body: Column(
            children: [
              Text('Running on: $_platformVersion\n'),
              Text('Running on: $_platformBattery\n'),
              Text('Running on: $_platformTelephony\n'),
            ],
          )),
    );
  }
}
