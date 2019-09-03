import 'dart:async';

import 'package:flutter/services.dart';

class FlutterOusign {
  static const MethodChannel _channel =
      const MethodChannel('plugins.openunion.cn/flutter_ousign');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// 初始化
  static Future<String> initialize({
    String orgID,
    String appID,
    bool isProd,
  }) async {
    final String result = await _channel.invokeMethod(
      'initialize',
      {
        'orgID': orgID,
        'appID': appID,
        'isProd': isProd,
      },
    );
    return result;
  }

  /// 请求随机数
  static Future<String> getRandom({
    String userName,
    String identityTypeCode,
    String identityNumber,
    String phoneNumber,
    String deviceID,
    String userInfo,
  }) async {
    final String result = await _channel.invokeMethod(
      'getRandom',
      {
        'userName': userName,
        'identityTypeCode': identityTypeCode,
        'identityNumber ': identityNumber,
        'phoneNumber': phoneNumber,
        'deviceID': deviceID,
        'userInfo': userInfo,
      },
    );
    return result;
  }

  /// 验证支付密码
  static Future<String> showVerifyPassword() async {
    await _channel.invokeMethod('showVerifyPassword');
  }
}
