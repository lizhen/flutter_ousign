/*
 * Copyright (c) CFCA 2016.
 */

package com.openunion.flutter_ousign.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.openunion.flutter_ousign.R;
import com.openunion.flutter_ousign.verifypassword.VerifyPasswordDialog;

import butterknife.BindView;

import cn.com.cfca.sdk.hke.HKEPasswordInvalidException;
import cn.com.cfca.sdk.hke.HKEWithPasswordApi;

/**
 * Created by wufan on 16/3/25.
 * Base activity
 * 包含 activity跳转，信息存储读取，实名认证操作的公共方法
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
  public static final int RC_SET_PASSWORD = 0x01;
  public static final int RC_VERIFY_PASSWORD = 0x02;
  public static final int RC_DOWNLOAD_CERTIFICATE = 0x03;

  @BindView(R.id.layout_container)
  public View containerView;

  @BindView(R.id.toolbar)
  public Toolbar toolbar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    initDependency();
    super.onCreate(savedInstanceState);
    setContentView(layoutId());
    initView();
  }

  @LayoutRes
  abstract protected int layoutId();

  protected void initView() {
  }

  protected void initDependency() {
    // no op
  }

  @Override
  public Context context() {
    return this;
  }

  @Override
  public void showError(String message) {
    showToast(message == null ? getString(R.string.err_msg_account) : message);
  }

  @Override
  public void showError(Throwable e) {
    if (e instanceof HKEPasswordInvalidException) {
      HKEPasswordInvalidException passwordInvalidException = (HKEPasswordInvalidException) e;
      showError(getString(R.string.password_invalid,
          passwordInvalidException.getLeftPasswordRetryIime()));
    } else {
      showError(e.getLocalizedMessage());
    }
  }

  @NonNull
  private Snackbar makeSnackbar(String message) {
    Snackbar snackbar = Snackbar.make(containerView, message, Snackbar.LENGTH_LONG);
    View sbview = snackbar.getView();
    TextView textView = sbview.findViewById(android.support.design.R.id.snackbar_text);
    textView.setTextColor(Color.YELLOW);
    return snackbar;
  }

  protected void showSnackbar(String message) {
    Snackbar snackbar = makeSnackbar(message);
    snackbar.show();
  }

  protected void showSnackbar(String message, Runnable runnable) {
    showSnackbar(message, getString(R.string.confirm), runnable);
  }

  protected void showSnackbar(String message, String action, Runnable runnable) {
    Snackbar snackbar = makeSnackbar(message);
    snackbar.setAction(action, v -> runnable.run());
    snackbar.show();
  }

  protected void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

//  @Override
//  public void showLoading() {
//    showLoadingDialog();
//  }

//  @Override
//  public void hideLoading() {
//    dismissLoadingDialog();
//  }

//  protected void gotoSetPasswordActivity() {
//    Intent intent = SetPasswordActivity.intent(this);
//    startActivityForResult(intent, RC_SET_PASSWORD);
//  }

  @SuppressWarnings("unchecked")
  @Nullable
  protected <T extends Fragment> T findFragmentByTag(Class<T> clz) {
    Fragment fragment = getSupportFragmentManager().findFragmentByTag(clz.getName());
    if (fragment != null) {
      return (T) fragment;
    }
    return null;
  }

  protected <T extends DialogFragment> void showDialogFragment(T dialogFragment, Class<T> clz) {
    getSupportFragmentManager().beginTransaction()
        .add(dialogFragment, clz.getName())
        .commitAllowingStateLoss();
  }

  protected void dismissDialogFragment(DialogFragment dialog) {
    if (dialog == null) return;
    dialog.dismissAllowingStateLoss();
  }

//  public void showLoadingDialog() {
//    LoadingFragment loadingFragment = LoadingFragment.newInstance();
//    showDialogFragment(loadingFragment, LoadingFragment.class);
//  }

//  public void dismissLoadingDialog() {
//    LoadingFragment loadingFragment = findFragmentByTag(LoadingFragment.class);
//    dismissDialogFragment(loadingFragment);
//  }

  protected void showVerifyPasswordDialog() {
    VerifyPasswordDialog dialog = VerifyPasswordDialog.newInstance();
    showDialogFragment(dialog, VerifyPasswordDialog.class);
  }

  protected void showVerifyPasswordDialogForSign() {
    VerifyPasswordDialog dialog = VerifyPasswordDialog.newInstanceForInput();
    showDialogFragment(dialog, VerifyPasswordDialog.class);
  }

  protected void showVerifyPasswordDialogForEnvelopeTest() {
    VerifyPasswordDialog dialog = VerifyPasswordDialog.newInstanceForInput();
    showDialogFragment(dialog, VerifyPasswordDialog.class);
  }

  protected void dismissVerifyPasswordDialog() {
    VerifyPasswordDialog dialog = findFragmentByTag(VerifyPasswordDialog.class);
    dismissDialogFragment(dialog);
  }

//  protected void showFingerprintDialog(FingerprintManagerCompat.CryptoObject cryptoObject) {
//    VerifyFingerprintFragment dialog = VerifyFingerprintFragment.newInstance();
//    dialog.setCryptoObject(cryptoObject);
//    showDialogFragment(dialog, VerifyFingerprintFragment.class);
//  }

//  protected void dismissFingerprintDialog() {
//    VerifyFingerprintFragment dialog = findFragmentByTag(VerifyFingerprintFragment.class);
//    dismissDialogFragment(dialog);
//  }

//  protected void gotoDownloadActivity() {
//    Intent intent = DownloadActivity.intent(this);
//    startActivityForResult(intent, RC_DOWNLOAD_CERTIFICATE);
//  }

//  protected void gotoResultActivity(boolean success, String message) {
//    Intent intent = ResultActivity.intent(this, success, message);
//    startActivityForResult(intent, DemoConstants.RESULT_CODE);
//  }

//  protected void gotoSelectorActivity(boolean isSupportEnvelope) {
//    Intent intent = SelectorActivity.intent(this, isSupportEnvelope);
//    startActivity(intent);
//  }

//  protected void gotoLogActivity() {
//    Intent intent = ShowLogActivity.intent(this);
//    startActivity(intent);
//  }

//  protected void gotoChangePasswordActivity() {
//    Intent intent = ChangePasswordActivity.intent(this);
//    startActivity(intent);
//  }

//  protected void gotoSignActivity(@DemoConstants.SignWay int signWay) {
//    Intent intent = SignActivity.intent(this, signWay);
//    startActivity(intent);
//  }

//  protected void gotoEnvelopeTestActivity(@DemoConstants.EnvelopeWay int envelopeWay) {
//    Intent intent = EnvelopeTestActivity.intent(this, envelopeWay);
//    startActivity(intent);
//  }

//  protected void gotoFingerprintActivity() {
//    Intent intent = FingerprintActivity.intent(this);
//    startActivity(intent);
//  }

//  protected void gotoTestActivity() {
//    Intent intent = TestActivity.intent(this);
//    startActivity(intent);
//  }

//  protected void requestFocus(View view) {
//    view.requestFocus();
//  }

//  protected boolean checkFingerprint() {
//    final int clientFingerprintState = HKEWithPasswordApi.getBiometryState(this);
//    if (clientFingerprintState == HKEWithPasswordApi.HKE_BIOMETRY_STATE_READY) {
//      return true;
//    } else if (clientFingerprintState == HKEWithPasswordApi.HKE_BIOMETRY_STATE_SYSTEM_VERION_LOW) {
//      showToast(getString(R.string.fingerprint_system_version_low));
//      return false;
//    } else if (clientFingerprintState == HKEWithPasswordApi.HKE_BIOMETRY_STATE_NOT_AVAILABLE) {
//      showToast(getString(R.string.fingerprint_hardware_not_available));
//    } else if (clientFingerprintState == HKEWithPasswordApi.HKE_BIOMETRY_STATE_NOT_ENROLLED) {
//      showToast(getString(R.string.fingerprint_not_enrolled));
//    }
//    return false;
//  }
//
//  protected boolean checkServerFingerprint(UserSource userSource) {
//    switch (userSource.getServerFingerprintState()) { // 检查服务器指纹状态
//      case HKEWithPasswordApi.HKESERVER_BIOMETRY_STATE_DISABLE:
//        showToast(getString(R.string.fingerprint_not_support_by_server));
//        return false;
//      case HKEWithPasswordApi.HKESERVER_BIOMETRY_STATE_NOT_SET:
//        showToast(getString(R.string.fingerprint_not_set));
//        return false;
//      case HKEWithPasswordApi.HKESERVER_BIOMETRY_STATE_NEED_UPDATE:
//        showToast(getString(R.string.fingerprint_need_update));
//        return false;
//      case HKEWithPasswordApi.HKESERVER_BIOMETRY_STATE_READY:
//        return true;
//      case HKEWithPasswordApi.HKESERVER_BIOMETRY_STATE_NOT_AVAILABLE:
//        showToast(getString(R.string.fingerprint_not_available_by_pin));
//        return false;
//    }
//    return false;
//  }
}
