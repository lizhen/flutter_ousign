package com.openunion.flutter_ousign;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.openunion.flutter_ousign.base.BaseActivity;
import com.openunion.flutter_ousign.constant.OuSignPluginMethods;
import com.openunion.flutter_ousign.handler.HKEApiWrapper;
import com.openunion.flutter_ousign.verifypassword.VerifyPasswordDialog;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterOusignPlugin */
public class FlutterOusignPlugin extends BaseActivity implements MethodCallHandler {
  private final Registrar registrar;
  private final MethodChannel channel;

  private FlutterOusignPlugin(Registrar registrar, MethodChannel channel) {
    this.registrar = registrar;
    this.channel = channel;
  }

  private Activity getActivity(){
    return registrar.activity();
  }

  private Context getApplicationContext(){
    return registrar.activity().getApplicationContext();
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "plugins.openunion.cn/flutter_ousign");
    channel.setMethodCallHandler(new FlutterOusignPlugin(registrar, channel));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    String method = call.method;
    switch (method) {
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case OuSignPluginMethods.INITIALIZE:
        HKEApiWrapper.initialize(registrar, (Map)call.arguments, result);
        break;
      case OuSignPluginMethods.REQUEST_RANDOM:
        HKEApiWrapper.getInstance().requestRandom((Map)call.arguments).subscribe(s -> result.success("requestRandom " + s));
        break;
      case OuSignPluginMethods.AUTHENTICATE:
        HKEApiWrapper.getInstance().authenticate((Map)call.arguments).subscribe(s -> result.success("authenticate " + s));
        break;
      case OuSignPluginMethods.DOWNLOAD_CERTIFICATE:
        HKEApiWrapper.getInstance().downloadCertificate((Map)call.arguments).subscribe(s -> result.success("downloadCertificate " + s));
        break;
      case OuSignPluginMethods.SIGN:
        HKEApiWrapper.getInstance().sign((Map)call.arguments).subscribe(s -> result.success("sign " + s));
        break;
      case OuSignPluginMethods.SET_PASSWORD:
        HKEApiWrapper.getInstance().setPassword((Map)call.arguments).subscribe(s -> result.success("setPassword " + s));
        break;
      case OuSignPluginMethods.CHANGE_PASSWORD:
        HKEApiWrapper.getInstance().changePassword((Map)call.arguments).subscribe(s -> result.success("changePassword " + s));
        break;
      case OuSignPluginMethods.VERIFY_PASSWORD:
        HKEApiWrapper.getInstance().verifyPassword((Map)call.arguments).subscribe(s -> result.success("verifyPassword " + s));
        break;
      case OuSignPluginMethods.SHOW_VERIFY_PASSWORD:
        showVerifyPasswordDialog();
        break;
      case OuSignPluginMethods.SET_RANDOM:
        break;
      case OuSignPluginMethods.GET_ENCRYPT_DATA:
        break;
      case OuSignPluginMethods.CANCEL:
        HKEApiWrapper.getInstance().cancel((Map)call.arguments);
        break;
      default:
        result.notImplemented();
        break;
    }
  }

  @Override
  protected int layoutId() {
    return 0;
  }

  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

  }
}
