package com.openunion.flutter_ousign.verifypassword;

import com.openunion.flutter_ousign.base.AbstractBasePresenter;
import com.openunion.flutter_ousign.constant.Constants;
import com.openunion.flutter_ousign.handler.HKEApiWrapper;
import com.openunion.flutter_ousign.repository.SettingSource;
import com.openunion.flutter_ousign.repository.UserSource;
import com.openunion.flutter_ousign.utils.schedulers.BaseSchedulerProvider;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class VerifyPasswordPresenter extends AbstractBasePresenter<VerifyPasswordContract.View> implements VerifyPasswordContract.Presenter {

    @Inject
    VerifyPasswordPresenter(VerifyPasswordContract.View view, BaseSchedulerProvider schedulerProvider, UserSource userSource, SettingSource settingSource) {
        super(view, schedulerProvider, userSource, settingSource);
    }

    @Override
    public void verifyPassword(String encryptedPassword, String encryptedClientRandom) {
        Map arguments = new HashMap();
        arguments.put(Constants.ENCRYPTED_PASSWORD, encryptedPassword);
        arguments.put(Constants.ENCRYPTED_CLIENT_RANDOM, encryptedClientRandom);

        disposables.add(HKEApiWrapper.getInstance()
                .verifyPassword(arguments)
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(subscription -> view.showLoading())
                .doFinally(view::hideLoading)
                .subscribe(o -> view.showVerifyPasswordSuccess(), view::showVerifyPasswordFailure)
        );
    }
}
