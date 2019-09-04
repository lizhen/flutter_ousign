package com.openunion.flutter_ousign.base;

import android.support.annotation.CallSuper;

import com.openunion.flutter_ousign.utils.schedulers.BaseSchedulerProvider;
import com.openunion.flutter_ousign.repository.UserSource;
import com.openunion.flutter_ousign.repository.SettingSource;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by wufan on 2017/10/17.
 */

public abstract class AbstractBasePresenter<V> implements BasePresenter {
  protected final V view;
//  protected final DemoApi demoApi;
  protected final BaseSchedulerProvider schedulerProvider;
  protected final UserSource userSource;
  protected final SettingSource settingSource;

  protected final CompositeDisposable disposables = new CompositeDisposable();

  protected AbstractBasePresenter(V view, BaseSchedulerProvider schedulerProvider,
      UserSource userSource, SettingSource settingSource) {
    this.view = view;
    this.schedulerProvider = schedulerProvider;
    this.userSource = userSource;
    this.settingSource = settingSource;
  }

  @CallSuper
  @Override
  public void start() {
  }

  @CallSuper
  @Override
  public void stop() {
    disposables.clear();
  }
}
