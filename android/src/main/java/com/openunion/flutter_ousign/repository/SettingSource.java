package com.openunion.flutter_ousign.repository;

/**
 * Created by wufan on 2018/3/16.
 * 设置相关信息, 本地存储
 */
public interface SettingSource {

    void saveServiceTypeIndex(int hkeServiceTypeIndex);

    int getServiceTypeIndex();

    void saveIsCITITest(boolean isCITITest);

    boolean getIsCITITest();

    void saveHKEEndPoint(String hkeServer);

    String getHKEEndPoint();

    void saveEndPoint(String server);

    String getEndPoint();

    String getSignEndPoint();

    String getOrderEndPoint();

    String getSignEndPoint2();

    String getCITIEncryption();

    String getAuthEndPoint();

    String getOrderHuishangEndPoint();

    String getRegisterEndPoint();

    String getQueryEndPoint();

    String getRevokeEndPoint();

    String getAnxingOpenAccount();

    String getDecryptPoint();

    void setOldAuthenticate(boolean useOldAuthenticate);

    boolean isOldAuthenticate();

    String getExtension();

    void setExtension(String extension);

    void setUseExtension(boolean useExtension);

    boolean isUseExtension();

    void saveOrgAppTypeIndex(int typeIndex);

    int getOrgAppTypeIndex();

}

