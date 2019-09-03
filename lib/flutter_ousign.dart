import 'dart:async';

import 'package:flutter/services.dart';

class FlutterOusign {
  static const MethodChannel _channel =
      const MethodChannel('plugins.openunion.cn/flutter_ousign');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
