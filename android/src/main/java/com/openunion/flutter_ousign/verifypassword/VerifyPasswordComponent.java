package com.openunion.flutter_ousign.verifypassword;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

public interface VerifyPasswordComponent extends AndroidInjector<VerifyPasswordDialog> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<VerifyPasswordDialog> {
    }
}
