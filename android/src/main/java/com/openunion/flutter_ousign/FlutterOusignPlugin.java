package com.openunion.flutter_ousign;

import android.support.v4.app.DialogFragment;

import com.openunion.flutter_ousign.constant.OuSignPluginMethods;
import com.openunion.flutter_ousign.handler.HKEApiWrapper;
import com.openunion.flutter_ousign.verifypassword.VerifyPasswordDialog;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterOusignPlugin */
public class FlutterOusignPlugin implements MethodCallHandler {
  private final Registrar registrar;

  private FlutterOusignPlugin(Registrar registrar) {
    this.registrar = registrar;
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "plugins.openunion.cn/flutter_ousign");
    channel.setMethodCallHandler(new FlutterOusignPlugin(registrar));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    String method = call.method;
    switch (method) {
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case OuSignPluginMethods.INITIALIZE:
        HKEApiWrapper.initialize(registrar, call, result);
        break;
      case OuSignPluginMethods.REQUEST_RANDOM:
        HKEApiWrapper.getInstance().requestRandom(call).subscribe(s -> result.success("requestRandom " + s));
        break;
      case OuSignPluginMethods.AUTHENTICATE:
        HKEApiWrapper.getInstance().authenticate(call).subscribe(s -> result.success("authenticate " + s));
        break;
      case OuSignPluginMethods.DOWNLOAD_CERTIFICATE:
        HKEApiWrapper.getInstance().downloadCertificate(call).subscribe(s -> result.success("downloadCertificate " + s));
        break;
      case OuSignPluginMethods.SIGN:
        HKEApiWrapper.getInstance().sign(call).subscribe(s -> result.success("sign " + s));
        break;
      case OuSignPluginMethods.SET_PASSWORD:
        HKEApiWrapper.getInstance().setPassword(call).subscribe(s -> result.success("setPassword " + s));
        break;
      case OuSignPluginMethods.CHANGE_PASSWORD:
        HKEApiWrapper.getInstance().changePassword(call).subscribe(s -> result.success("changePassword " + s));
        break;
      case OuSignPluginMethods.VERIFY_PASSWORD:
        HKEApiWrapper.getInstance().verifyPassword(call).subscribe(s -> result.success("verifyPassword " + s));
        break;
      case OuSignPluginMethods.SHOW_VERIFY_PASSWORD:
        showVerifyPasswordDialog();
        break;
      case OuSignPluginMethods.SET_RANDOM:
        break;
      case OuSignPluginMethods.GET_ENCRYPT_DATA:
        break;
      case OuSignPluginMethods.CANCEL:
        HKEApiWrapper.getInstance().cancel(call);
        break;
      default:
        result.notImplemented();
        break;
    }
  }

  protected void showVerifyPasswordDialog() {
    VerifyPasswordDialog dialog = VerifyPasswordDialog.newInstance();
    showDialogFragment(dialog, VerifyPasswordDialog.class);
  }

  protected <T extends DialogFragment> void showDialogFragment(T dialogFragment, Class<T> clz) {
    getSupportFragmentManager().beginTransaction()
            .add(dialogFragment, clz.getName())
            .commitAllowingStateLoss();
  }
}
