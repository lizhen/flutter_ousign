package com.openunion.flutter_ousign.constant;

/**
 * Created by liz on 2019/08/30
 */
public class OuSignPluginMethods {
    private OuSignPluginMethods() {
    }

    public static final String INITIALIZE = "initialize"; // 初始化签名插件

    public static final String REQUEST_RANDOM = "requestRandom"; // 请求随机数

    public static final String AUTHENTICATE = "authenticate"; // 身份认证

    public static final String DOWNLOAD_CERTIFICATE = "downloadCertificate"; // 证书下载

    public static final String SIGN = "sign"; // 签名

    public static final String SET_PASSWORD = "setPassword"; // 设置密码
    public static final String CHANGE_PASSWORD = "changePassword"; // 修改密码
    public static final String VERIFY_PASSWORD = "verifyPassword"; // 验证密码

    public static final String SHOW_VERIFY_PASSWORD = "showVerifyPassword"; // 弹出验证密码

    public static final String SET_RANDOM = "setRandom"; // 设置客户端随机数
    public static final String GET_ENCRYPT_DATA = "getEncryptData"; // 获取加密结果


    public static final String CANCEL = "cancel"; // 取消
}
