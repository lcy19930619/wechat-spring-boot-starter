package net.jlxxw.wechat.dto.message.event;

/**
 * @author chunyang.leng
 * @date 2021-12-17 7:24 下午
 */
public class ScanCodeInfo {
    /**
     * 扫描结果，即二维码对应的字符串信息
     */
    private String scanResult;

    /**
     * 扫描类型，一般是qrcode
     */
    private String scanType;

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }


}
