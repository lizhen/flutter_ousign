package com.openunion.flutter_ousign.verifypassword;

import com.openunion.flutter_ousign.base.BaseView;
import com.openunion.flutter_ousign.base.BasePresenter;

/**
 * Created by wufan on 2018/3/22.
 */

interface VerifyPasswordContract {

  interface View extends BaseView {
    void showVerifyPasswordSuccess();

    void showVerifyPasswordFailure(Throwable e);
  }

  interface Presenter extends BasePresenter {
    void verifyPassword(String encryptedPassword, String encryptedClientRandom);
  }
}
