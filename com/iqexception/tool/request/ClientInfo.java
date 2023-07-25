package com.iqexception.tool.request;

import com.ctriposs.baiji.rpc.mobile.common.types.ExtensionFieldType;
import com.ctriposs.baiji.rpc.mobile.common.types.MobileRequestHead;

/**
 * @author wang.wei
 * @since 2019/7/11
 */
public class ClientInfo {
    private String syscode;

    private String lang;

    private String auth;

    private String cid;

    private String ctok;

    private String cver;

    private String sid;

    private String pauth;

    private String sauth;

    private String appid;
    // extensions
    /// <summary>
    /// 平台
    /// </summary>
    private String platform;
    /// <summary>
    /// 请求时间戳
    /// </summary>
    private String reqTime;
    /// <summary>
    /// 客户端信息
    /// </summary>
    private String clientInfo;
    /// <summary>
    /// 版本
    /// </summary>
    private String appVersion;
    /// <summary>
    /// 设备号
    /// </summary>
    private String deviceId;
    /// <summary>
    /// 签名
    /// </summary>
    private String sign;
    /// <summary>
    /// 合作方
    /// </summary>
    private String partner;
    /// <summary>
    /// 渠道
    /// </summary>
    private String channel;
    /// <summary>
    /// deviceToken
    /// </summary>
    private String deviceToken;
    /// <summary>
    /// 纠正版本号
    /// </summary>
    private Integer correctVersion;

    private String uid;

    private String latitude;

    private String longitude;

    /**
     * 微信小程序formId
     */
    private String formId;

    /**
     * 微信openId
     * @param openId
     * @return
     */
    private String openId;

    public static ClientInfo from(MobileRequestHead head) {
        if (head == null) return null;
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setAppid(head.getAppid())
                .setAuth(head.getAuth())
                .setCid(head.getCid())
                .setCtok(head.getCtok())
                .setCver(head.getCver())
                .setLang(head.getLang())
                .setPauth(head.getPauth())
                .setSauth(head.getSauth())
                .setSid(head.getSid())
                .setSyscode(head.getSyscode());
        if (head.getExtension() != null) {
            for (ExtensionFieldType extensionField : head.getExtension()) {
                switch (extensionField.getName()) {
                    case "platform":
                        clientInfo.setPlatform(extensionField.getValue());
                        break;
                    case "reqTime":
                        clientInfo.setReqTime(extensionField.getValue());
                        break;
                    case "clientInfo":
                        clientInfo.setClientInfo(extensionField.getValue());
                        break;
                    case "appVersion":
                        clientInfo.setAppVersion(extensionField.getValue());
                        break;
                    case "deviceId":
                        clientInfo.setDeviceId(extensionField.getValue());
                        break;
                    case "partner":
                        clientInfo.setPartner(extensionField.getValue());
                        break;
                    case "channel":
                        clientInfo.setChannel(extensionField.getValue());
                        break;
                    case "sign":
                        clientInfo.setSign(extensionField.getValue());
                        break;
                    case "deviceToken":
                        clientInfo.setDeviceToken(extensionField.getValue());
                        break;
                    case "correctVersion":
                        clientInfo.setCorrectVersion(Integer.parseInt(extensionField.getValue()));
                        break;
                    case "latitude":
                        clientInfo.setLatitude(extensionField.getValue());
                        break;
                    case "longitude":
                        clientInfo.setLongitude(extensionField.getValue());
                        break;
                    case "appid":
                        clientInfo.setAppid(extensionField.getValue());
                        break;
                    case "uid":
                        clientInfo.setUid(extensionField.getValue());
                        break;
                    case "formId":
                        clientInfo.setFormId(extensionField.getValue());
                        break;
                    case "openId":
                        clientInfo.setOpenId(extensionField.getValue());
                        break;
                    default:
                        break;
                }

            }
        }
        return clientInfo;

    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getLatitude() {
        return latitude;
    }

    public ClientInfo setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getLongitude() {
        return longitude;
    }

    public ClientInfo setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getSyscode() {
        return syscode;
    }

    public ClientInfo setSyscode(String syscode) {
        this.syscode = syscode;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public ClientInfo setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getAuth() {
        return auth;
    }

    public ClientInfo setAuth(String auth) {
        this.auth = auth;
        return this;
    }

    public String getCid() {
        return cid;
    }

    public ClientInfo setCid(String cid) {
        this.cid = cid;
        return this;
    }

    public String getCtok() {
        return ctok;
    }

    public ClientInfo setCtok(String ctok) {
        this.ctok = ctok;
        return this;
    }

    public String getCver() {
        return cver;
    }

    public ClientInfo setCver(String cver) {
        this.cver = cver;
        return this;
    }

    public String getSid() {
        return sid;
    }

    public ClientInfo setSid(String sid) {
        this.sid = sid;
        return this;
    }

    public String getPauth() {
        return pauth;
    }

    public ClientInfo setPauth(String pauth) {
        this.pauth = pauth;
        return this;
    }

    public String getSauth() {
        return sauth;
    }

    public ClientInfo setSauth(String sauth) {
        this.sauth = sauth;
        return this;
    }

    public String getAppid() {
        return appid;
    }

    public ClientInfo setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public ClientInfo setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getReqTime() {
        return reqTime;
    }

    public ClientInfo setReqTime(String reqTime) {
        this.reqTime = reqTime;
        return this;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public ClientInfo setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public ClientInfo setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public ClientInfo setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public ClientInfo setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getPartner() {
        return partner;
    }

    public ClientInfo setPartner(String partner) {
        this.partner = partner;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public ClientInfo setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public ClientInfo setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
        return this;
    }

    public Integer getCorrectVersion() {
        return correctVersion;
    }

    public ClientInfo setCorrectVersion(Integer correctVersion) {
        this.correctVersion = correctVersion;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public ClientInfo setUid(String uid) {
        this.uid = uid;
        return this;
    }
}
