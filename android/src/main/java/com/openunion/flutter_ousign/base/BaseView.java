/*
 * Copyright (c) CFCA 2017.
 */

package com.openunion.flutter_ousign.base;

import android.content.Context;

/**
 * Created by wufan on 2017/4/24.
 */

public interface BaseView {
  Context context();

  void showError(String message);

  void showError(Throwable e);

  void showLoading();

  void hideLoading();
}
