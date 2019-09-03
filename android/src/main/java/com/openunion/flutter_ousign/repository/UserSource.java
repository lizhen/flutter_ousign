package com.openunion.flutter_ousign.repository;

/**
 * Created by wufan on 2018/3/16.
 * 用户相关信息, 本地存储
 */
public interface UserSource {

  void savePhone(String phone);

  String getPhone();

  void saveName(String name);

  String getName();

  void saveIdNO(String idNO);

  String getIdNO();

  void saveIdType(String idType);

  String getIdType();

  void saveDeviceTag(String deviceTag);

  String getDeviceTag();

  void savePinServerRandom(String pinServerRandom);

  String getPinServerRandom();

  void savePinState(int pinState);

  int getPinState();

  void saveServerFingerprintState(int serverFingerprintState);

  int getServerFingerprintState();

  void clearAllInformations();

  void saveEnterprise(String enterprise);

  String getEnterprise();

  void saveIfaa(String ifaa);

  String getIfaa();
}
