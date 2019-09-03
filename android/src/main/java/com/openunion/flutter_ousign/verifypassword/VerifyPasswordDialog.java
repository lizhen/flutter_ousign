package com.openunion.flutter_ousign.verifypassword;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cfca.mobile.hke.sipedit.SipEditText;
import com.cfca.mobile.hke.sipkeyboard.SipResult;
import com.cfca.mobile.log.CodeException;
import com.openunion.flutter_ousign.R;
import com.openunion.flutter_ousign.repository.UserSource;
import com.openunion.flutter_ousign.utils.SipEditTextUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cfca.sdk.hke.HKEPasswordInvalidException;
import dagger.android.support.AndroidSupportInjection;

import static com.openunion.flutter_ousign.utils.SipEditTextUtil.initSipEditText;


/**
 * Created by wufan on 2018/3/22.
 */

public class VerifyPasswordDialog extends DialogFragment implements VerifyPasswordContract.View {

  private static final String ARG_FOR_INPUT = "ARG_FOR_INPUT";
  private Listener callback;
  private ListenerForInput callbackForInput;

  @BindView(R.id.input_login_pwd)
  SipEditText sip;

  @Inject
  VerifyPasswordContract.Presenter presenter;

  @Inject
  UserSource userSource;

  boolean forInput; // 签名时输入密码，不需要调用验证

  // 在Dialog中处理验证密码逻辑
  public interface Listener {
    void onVerifyLoginPasswordSuccess();
  }

  // 在Dialog中仅输入密码，外部使用密码加密结果
  public interface ListenerForInput {
    void onPasswordInput(String encryptedPassword, String encryptedClientRandom);
  }

  public static VerifyPasswordDialog newInstance() {
    Bundle args = new Bundle();
    VerifyPasswordDialog fragment = new VerifyPasswordDialog();
    args.putBoolean(ARG_FOR_INPUT, false);
    fragment.setArguments(args);
    return fragment;
  }

  public static VerifyPasswordDialog newInstanceForInput() {
    Bundle args = new Bundle();
    VerifyPasswordDialog fragment = new VerifyPasswordDialog();
    args.putBoolean(ARG_FOR_INPUT, true);
    fragment.setArguments(args);
    return fragment;
  }

  public void onAttach(Context context) {
    AndroidSupportInjection.inject(this);
    super.onAttach(context);
    try {
      callback = (Listener) context;
    } catch (ClassCastException e) {
      callback = null;
    }

    try {
      callbackForInput = (ListenerForInput) context;
    } catch (ClassCastException e) {
      callbackForInput = null;
    }

    if (callback == null && callbackForInput == null) {
      throw new ClassCastException(context.toString() + " must implement Listener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    callback = null;
    callbackForInput = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      forInput = getArguments().getBoolean(ARG_FOR_INPUT, false);
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    Window window = dialog.getWindow();
    if (window != null) {
      window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
      WindowManager.LayoutParams lp = window.getAttributes();
      if (lp != null) {
        lp.y = getResources().getDimensionPixelSize(R.dimen.verify_dialog_top_margin);
        window.setGravity(Gravity.TOP);
      }
    }
    return dialog;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    getDialog().setCancelable(false);
    getDialog().setCanceledOnTouchOutside(false);
    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    LayoutInflater activityInflater = LayoutInflater.from(getActivity());
    View view = activityInflater.inflate(R.layout.dialog_verify_password, container, false);
    ButterKnife.bind(this, view);
    initSipEditText(sip, userSource.getPinServerRandom(),
        new SipEditTextUtil.SipEditTextDelegatorAdaptor() {

          @Override
          public void afterClickDown(SipEditText sipEditText) {
            onDoneClicked();
          }
        });
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    presenter.start();
  }

  @Override
  public void onResume() {
    super.onResume();
    Window window = getDialog().getWindow();
    // setLayout must place in onResume method
    if (window != null) {
      window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    presenter.stop();
  }

  public void onDoneClicked() {
    try {
      if (sip.getText().length() == 0) {
        showToast(getString(R.string.password0));
        return;
      }
      SipResult sipResult = sip.getEncryptData();
      if (forInput) {
        if (callbackForInput != null) {
          callbackForInput.onPasswordInput(sipResult.getEncryptInput(),
              sipResult.getEncryptRandomNum());
          dismiss();
        }
      } else {
        presenter.verifyPassword(sipResult.getEncryptInput(), sipResult.getEncryptRandomNum());
      }
    } catch (CodeException e) {
      Toast.makeText(getActivity(), String.format("0x%02X,%s", e.getCode(), e.getMessage()),
          Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public Context context() {
    return getActivity();
  }

  @Override
  public void showError(String message) {
    showToast(message);
  }

  @Override
  public void showError(Throwable e) {
    showError(e.getLocalizedMessage());
  }

  @Override
  public void showLoading() {
//    BaseActivity activity = (BaseActivity) getActivity();
//    activity.showLoadingDialog();
  }

  @Override
  public void hideLoading() {
//    BaseActivity activity = (BaseActivity) getActivity();
//    activity.dismissLoadingDialog();
  }

  @Override
  public void showVerifyPasswordSuccess() {
    dismiss();
    if (callback != null) {
      callback.onVerifyLoginPasswordSuccess();
    }
  }

  @Override
  public void showVerifyPasswordFailure(Throwable e) {
    sip.clear();
    if (e instanceof HKEPasswordInvalidException) {
      showToast(getString(R.string.password_invalid,
          ((HKEPasswordInvalidException) e).getLeftPasswordRetryIime()));
    } else {
      showToast(e.getLocalizedMessage());
    }
  }

  private void showToast(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }

  @OnClick(R.id.btn_dismiss)
  public void onDismissClicked() {
    sip.hideSecurityKeyBoard();
    dismiss();
  }
}
