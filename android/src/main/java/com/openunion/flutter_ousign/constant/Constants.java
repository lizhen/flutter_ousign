package com.openunion.flutter_ousign.constant;

/**
 * Created by liz on 2019/08/30
 */
public final class Constants {
    private Constants() {
    }

    // 初始化
    public static final String ORG_ID = "orgID"; // 机构ID
    public static final String APP_ID = "appID"; // AppID
    public static final String IS_PROD = "isProd"; // 是否生产环境

    // 获取服务器随机数（IFAA版）
    public static final String USER_NAME = "userName"; // 用户姓名
    public static final String IDENTITY_TYPE_CODE = "identityTypeCode"; // 用户证件类型编码（0:身份证）
    public static final String IDENTITY_NUMBER = "identityNumber"; // 用户身份证号码
    public static final String PHONE_NUMBER = "phoneNumber"; // 用户手机号
    public static final String DEVICE_ID = "deviceID"; // 用户设备标识
    public static final String USER_INFO = "userInfo"; // 用户信息

    // 身份认证
    public static final String SERVER_SIGNATURE = "serverSignature"; // 随机数签名

    // 证书下载
    public static final String ENTENSION = "extension"; // 描述扩展信息

    // 业务签名
    public static final String BUSINESS_TEXT = "businessText"; // 待签名业务报文
    public static final String BUSINESS_TEXT_SIGNATURE = "businessTextSignature"; // 使用机构证书对业务报文的签名

    // 设置密码、验证密码
    public static final String ENCRYPTED_PASSWORD = "encryptedPassword"; // 安全控件加密后的密文
    public static final String ENCRYPTED_CLIENT_RANDOM = "encryptedClientRandom"; // 安全控件加密后的客户端随机数

    // 修改密码
    public static final String ORIGINAL_ENCRYPTED_PASSWORD = "originalEncryptedPassword"; // 旧密码密文
    public static final String ORIGINAL_ENCRYPTED_CLIENT_RANDOM = "originalEncryptedCilentRandom"; // 旧密码客户端随机数
    public static final String NEW_ENCRYPTED_PASSWORD = "newEncryptedPassword"; // 新密码密文
    public static final String NEW_ENCRYPTED_CLIENT_RANDOM = "newEncryptedCilentRandom"; // 新密码客户端随机数
}
