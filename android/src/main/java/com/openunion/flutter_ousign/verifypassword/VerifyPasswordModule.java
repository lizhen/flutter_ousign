package com.openunion.flutter_ousign.verifypassword;

import com.openunion.flutter_ousign.repository.SettingSource;
import com.openunion.flutter_ousign.repository.UserSource;
import com.openunion.flutter_ousign.utils.schedulers.BaseSchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class VerifyPasswordModule {

    @Provides
    public VerifyPasswordContract.View provideView(VerifyPasswordDialog verifyPasswordDialog) {
        return verifyPasswordDialog;
    }

    @Provides
    public VerifyPasswordContract.Presenter providePresenter(VerifyPasswordContract.View view, BaseSchedulerProvider schedulerProvider, UserSource userSource, SettingSource settingSource) {
        return new VerifyPasswordPresenter(view, schedulerProvider, userSource, settingSource);
    }
}
