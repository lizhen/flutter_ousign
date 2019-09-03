package com.openunion.flutter_ousign.utils;

import androidx.annotation.Nullable;

import com.cfca.mobile.hke.sipedit.SipEditText;
import com.cfca.mobile.hke.sipedit.SipEditTextDelegator;

/**
 * Created by liz on 2019/08/30
 */
public class SipEditTextUtil {
    private static final String MATCHREGEX =
            "^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)]|[\\(\\)])+$)([^(0-9a-zA-Z)]|[\\(\\)]|[a-zA-Z]|[0-9]){8,16}$";
    private static final int OUTPUTTYPE = 2;//原文加密
    private static final int MAX_LENGTH = 16;
    private static final int MIN_LENGTH = 8;

    public static void initSipEditText(SipEditText sipEditText, String serverRandom) {
        initSipEditText(sipEditText, serverRandom, null);
    }

    public static void initSipEditText(SipEditText sipEditText, String serverRandom,
                                       @Nullable SipEditTextDelegator delegator) {
        sipEditText.setLastCharacterShown(true);
        sipEditText.setKeyAnimation(true);
        sipEditText.setHasButtonClickSound(false);
        sipEditText.setOutSideDisappear(true);
        if (delegator != null) {
            sipEditText.setSipDelegator(delegator);
        }
        sipEditText.setServerRandom(serverRandom);
    }

    public static boolean checkInputLength(SipEditText sipEditText) {
        return sipEditText.getText().length() >= MIN_LENGTH;
    }

    public static class SipEditTextDelegatorAdaptor implements SipEditTextDelegator {

        @Override
        public void beforeKeyboardShow(SipEditText sipEditText, int i) {
        }

        @Override
        public void afterKeyboardHidden(SipEditText sipEditText, int i) {
        }

        @Override
        public void afterClickDown(SipEditText sipEditText) {
        }

        @Override
        public void onTextChangeListener(int lengthBefore, int lengthAfter) {

        }
    }
}
