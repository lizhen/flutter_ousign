package com.openunion.flutter_ousign.handler;

import com.openunion.flutter_ousign.constant.Constants;

import java.util.logging.Logger;

import cn.com.cfca.sdk.hke.Callback;
import cn.com.cfca.sdk.hke.HKEException;
import cn.com.cfca.sdk.hke.HKEServiceType;
import cn.com.cfca.sdk.hke.HKEWithPasswordApi;
import cn.com.cfca.sdk.hke.data.AuthenticateInfo;
import cn.com.cfca.sdk.hke.data.CFCACertificate;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import com.google.gson.Gson;

/**
 * Created by liz on 2019/08/30
 */
public class HKEApiWrapper {

    private static final Logger logger = Logger.getLogger(HKEApiWrapper.class.getSimpleName());

    private static final Object SUCCESS = new Object();
    private static HKEApiWrapper instance;
    private final HKEWithPasswordApi hkeApi;

    private HKEApiWrapper(HKEWithPasswordApi api) {
        this.hkeApi = api;
    }

    public boolean isSupportSE() {
        return hkeApi.isSupportSE();
    }

    public static void initialize(Registrar registrar, MethodCall call, Result result) {
        logger.info(">>>initialize<<<");

        String orgID = call.argument(Constants.ORG_ID);
        if (orgID == null || orgID.isEmpty()) {
            throw new IllegalStateException("OrgID is a required parameter");
        }

        String appID = call.argument(Constants.APP_ID);
        if (appID == null || appID.isEmpty()) {
            throw new IllegalStateException("AppID is a required parameter");
        }

        boolean isProd = call.argument(Constants.IS_PROD);


        HKEWithPasswordApi.initialize(registrar.context(), orgID, appID, isProd ? HKEServiceType.PRODUCT : HKEServiceType.TEST);

        instance = new HKEApiWrapper(HKEWithPasswordApi.getInstance());

        result.success("HKEApiWrapper Successful");
    }

    public static HKEApiWrapper getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Should call initialize before getInstance");
        }

        // logger.setLevel(Level.WARNING);

        return instance;
    }

    /**
     * 获取IFAA版本服务器端随机数
     *
     * @param name               用户名
     * @param identityType       证件类型
     * @param identityCardNumber 证件号
     * @param phoneNumber        手机号
     * @param deviceID           设备ID
     * @param userInfo           用户信息
     * @return 服务器端随机数
     */
    // public Flowable<String> requestHKEServerRandom(final String name, final String identityType, final String identityCardNumber, final String phoneNumber, final String deviceID, final String userInfo) {
    public Flowable<String> requestRandom(final MethodCall call) {
        logger.info(">>>requestHKEServerRandom<<<");

        String userName = call.argument(Constants.USER_NAME);
        if (userName == null || userName.isEmpty()) {
            throw new IllegalStateException("UserName is a required parameter");
        }

        String identityTypeCode = call.argument(Constants.IDENTITY_TYPE_CODE);
        if (identityTypeCode == null || identityTypeCode.isEmpty()) {
            throw new IllegalStateException("IdentityTypeCode is a required parameter");
        }

        String identityNumber = call.argument(Constants.IDENTITY_NUMBER);
        if (identityNumber == null || identityNumber.isEmpty()) {
            throw new IllegalStateException("IdentityNumber is a required parameter");
        }

        String phoneNumber = call.argument(Constants.PHONE_NUMBER);
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalStateException("PhoneNumber is a required parameter");
        }

        String deviceID = call.argument(Constants.DEVICE_ID);
        if (deviceID == null || deviceID.isEmpty()) {
            throw new IllegalStateException("DeviceID is a required parameter");
        }

        String userInfo = call.argument(Constants.USER_INFO);
        if (userInfo == null) {
            throw new IllegalStateException("UserInfo is a required parameter");
        }

        return Flowable.create(
                emitter -> hkeApi.requestHKEServerRandom(userName, identityTypeCode, identityNumber, phoneNumber, deviceID, userInfo, new Callback<String>() {
                    @Override
                    public void onResult(String s) {
                        emitter.onNext(s);
                        emitter.onComplete();
                    }

                    @Override
                    public void onError(HKEException e) {
                        emitter.onError(e);
                    }
                }), BackpressureStrategy.BUFFER
        );
    }

    /**
     * 身份认证
     *
     * @param serverRandomSignature 使用机构证书对身份认证返回的随机数做的签名（用以鉴别客户端身份）
     */
    // public Flowable<AuthenticateInfo> authenticate(final String serverRandomSignature) {
    public Flowable<String> authenticate(final MethodCall call) {
        logger.info(">>>authenticate<<<");

        String serverSignature = call.argument(Constants.SERVER_SIGNATURE);
        if (serverSignature == null || serverSignature.isEmpty()) {
            throw new IllegalStateException("ServerSignature is a required parameter");
        }

        return Flowable.create(emitter -> hkeApi.authenticateWithServerSignature(serverSignature,
                new Callback<AuthenticateInfo>() {
                    @Override
                    public void onResult(AuthenticateInfo s) {
                        if (s.getCertificates().size() > 0) {
                            logger.info("SN:" + s.getCertificates().get(0).getSerialNumber());
                        }
                        if (s.getPinState() == 3) {
                            emitter.onError(new HKEException(-1, "Pin is locked!"));
                        } else if (s.getPinState() == 4) {
                            emitter.onError(new HKEException(-1, "Pin is locked forever"));
                        } else {
                            // emitter.onNext(s);
                            emitter.onNext(new Gson().toJson(s));
                            emitter.onComplete();
                        }
                    }

                    @Override
                    public void onError(HKEException e) {
                        emitter.onError(e);
                    }
                }), BackpressureStrategy.BUFFER);
    }

    /**
     * 下载证书
     *
     * @return 证书对象
     */
    // public Flowable<CFCACertificate> downloadCertificate(String extension) {
    public Flowable<String> downloadCertificate(final MethodCall call) {
        logger.info(">>>downloadCertificate<<<");

        String extension = call.argument(Constants.ENTENSION);
        if (extension == null || extension.isEmpty()) {
            throw new IllegalStateException("Extension is a required parameter");
        }

        return Flowable.create(
                emitter -> hkeApi.downloadCertificate(extension, new Callback<CFCACertificate>() {
                    @Override
                    public void onResult(CFCACertificate certificate) {
                        // emitter.onNext(certificate);
                        emitter.onNext(new Gson().toJson(certificate));
                        emitter.onComplete();
                    }

                    @Override
                    public void onError(HKEException e) {
                        emitter.onError(e);
                    }
                }), BackpressureStrategy.BUFFER);
    }

    /**
     * 签名
     *
     * @param businessText          待签名交易报文
     * @param businessTextSignature 带签名交易报文签名
     * @param encryptedPassword     安全控件加密后的密文
     * @param encryptedCilentRandom 安全控件用于加密的客户端随机数
     */
    // public Flowable<String> sign(final String businessText, final String businessTextSignature, final String encryptedPassword, final String encryptedCilentRandom) {
    public Flowable<String> sign(final MethodCall call) {
        logger.info(">>>sign<<<");

        String businessText = call.argument(Constants.BUSINESS_TEXT);
        if (businessText == null || businessText.isEmpty()) {
            throw new IllegalStateException("BusinessText is a required parameter");
        }

        String businessTextSignature = call.argument(Constants.BUSINESS_TEXT_SIGNATURE);
        if (businessTextSignature == null || businessTextSignature.isEmpty()) {
            throw new IllegalStateException("BusinessTextSignature is a required parameter");
        }

        String encryptedPassword = call.argument(Constants.ENCRYPTED_PASSWORD);
        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            throw new IllegalStateException("EncryptedPassword is a required parameter");
        }

        String encryptedClientRandom = call.argument(Constants.ENCRYPTED_CLIENT_RANDOM);
        if (encryptedClientRandom == null || encryptedClientRandom.isEmpty()) {
            throw new IllegalStateException("EncryptedClientRandom is a required parameter");
        }

        return Flowable.create(
                emitter -> hkeApi.signMessageWithBusinessMessage(businessText, businessTextSignature,
                        encryptedPassword, encryptedClientRandom, new Callback<String>() {
                            @Override
                            public void onResult(String signature) {
                                emitter.onNext(signature);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(HKEException e) {
                                emitter.onError(e);
                            }
                        }), BackpressureStrategy.BUFFER);
    }

    /**
     * 设置PIN码
     *
     * @param encryptedPassword     安全控件加密后的密文
     * @param encryptedClientRandom 安全控件用于加密的客户端随机数
     */
    // public Flowable<Object> setPassword(final String encryptedPassword, final String encryptedClientRandom) {
    public Flowable<Object> setPassword(final MethodCall call) {
        logger.info(">>>setPassword<<<");

        String encryptedPassword = call.argument(Constants.ENCRYPTED_PASSWORD);
        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            throw new IllegalStateException("EncryptedPassword is a required parameter");
        }

        String encryptedClientRandom = call.argument(Constants.ENCRYPTED_CLIENT_RANDOM);
        if (encryptedClientRandom == null || encryptedClientRandom.isEmpty()) {
            throw new IllegalStateException("EncryptedClientRandom is a required parameter");
        }

        return Flowable.create(emitter -> hkeApi.setPassword(encryptedPassword, encryptedClientRandom,
                new Callback<Void>() {
                    @Override
                    public void onResult(Void aVoid) {
                        emitter.onNext(SUCCESS);
                        emitter.onComplete();
                    }

                    @Override
                    public void onError(HKEException e) {
                        emitter.onError(e);
                    }
                }), BackpressureStrategy.BUFFER);
    }

    /**
     * 修改密码
     *
     * @param originalEncryptedPassword     旧密码密文
     * @param originalEncryptedCilentRandom 旧密码客户端随机数
     * @param newEncryptedPassword          新密码密文
     * @param newEncryptedCilentRandom      新密码客户端随机数
     */
    // public Flowable<Object> changePassword(final String originalEncryptedPassword, final String originalEncryptedCilentRandom, final String newEncryptedPassword, final String newEncryptedCilentRandom) {
    public Flowable<Object> changePassword(final MethodCall call) {
        logger.info(">>>changePassword<<<");

        String originalEncryptedPassword = call.argument(Constants.ORIGINAL_ENCRYPTED_PASSWORD);
        if (originalEncryptedPassword == null || originalEncryptedPassword.isEmpty()) {
            throw new IllegalStateException("originalEncryptedPassword is a required parameter");
        }

        String originalEncryptedCilentRandom = call.argument(Constants.ORIGINAL_ENCRYPTED_CLIENT_RANDOM);
        if (originalEncryptedCilentRandom == null || originalEncryptedCilentRandom.isEmpty()) {
            throw new IllegalStateException("originalEncryptedCilentRandom is a required parameter");
        }

        String newEncryptedPassword = call.argument(Constants.NEW_ENCRYPTED_PASSWORD);
        if (newEncryptedPassword == null || newEncryptedPassword.isEmpty()) {
            throw new IllegalStateException("newEncryptedPassword is a required parameter");
        }

        String newEncryptedCilentRandom = call.argument(Constants.NEW_ENCRYPTED_CLIENT_RANDOM);
        if (newEncryptedCilentRandom == null || newEncryptedCilentRandom.isEmpty()) {
            throw new IllegalStateException("newEncryptedCilentRandom is a required parameter");
        }

        return Flowable.create(
                emitter -> hkeApi.changePassword(originalEncryptedPassword, originalEncryptedCilentRandom,
                        newEncryptedPassword, newEncryptedCilentRandom, new Callback<Void>() {
                            @Override
                            public void onResult(Void aVoid) {
                                emitter.onNext(SUCCESS);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(HKEException e) {
                                emitter.onError(e);
                            }
                        }), BackpressureStrategy.BUFFER);
    }

    /**
     * 验证密码
     *
     * @param encryptedPassword     密码密文
     * @param encryptedClientRandom 客户端随机数
     */
    // public Flowable<Object> verifyPassword(final String encryptedPassword, final String encryptedClientRandom) {
    public Flowable<Object> verifyPassword(final MethodCall call) {
        logger.info(">>>verifyPassword<<<");

        String encryptedPassword = call.argument(Constants.ENCRYPTED_PASSWORD);
        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            throw new IllegalStateException("EncryptedPassword is a required parameter");
        }

        String encryptedClientRandom = call.argument(Constants.ENCRYPTED_CLIENT_RANDOM);
        if (encryptedClientRandom == null || encryptedClientRandom.isEmpty()) {
            throw new IllegalStateException("EncryptedClientRandom is a required parameter");
        }

        return Flowable.create(
                emitter -> hkeApi.verifyPassword(encryptedPassword, encryptedClientRandom,
                        new Callback<Void>() {
                            @Override
                            public void onResult(Void aVoid) {
                                emitter.onNext(SUCCESS);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(HKEException e) {
                                emitter.onError(e);
                            }
                        }), BackpressureStrategy.BUFFER);
    }

    /**
     * 取消
     *
     * @param call
     */
    public void cancel(final MethodCall call) {
        logger.info(">>>cancel<<<");

        String methodCode = call.argument("methodCode");
        if (methodCode == null || methodCode.isEmpty()) {
            throw new IllegalStateException("MethodCode is a required parameter");
        }

        switch (methodCode) {
            case "01":
                hkeApi.cancelRequestHKEServerRandom();
                break;
            case "02":
                hkeApi.cancelAuthenticate();
                break;
            case "03":
                hkeApi.cancelDownloadCertificate();
                break;
            case "04":
                hkeApi.cancelSign();
                break;
            case "05":
                hkeApi.cancelSetPassword();
                break;
            case "06":
                hkeApi.cancelChangePassword();
                break;
            case "07":
                hkeApi.cancelVerifyPassword();
                break;
            case "08":
                hkeApi.cancelAll();
                break;
            default:
                throw new IllegalStateException("CancelMethod call does not exist");
                break;
        }
    }
}
