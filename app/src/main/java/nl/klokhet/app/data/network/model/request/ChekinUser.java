package nl.klokhet.app.data.network.model.request;

import java.io.Serializable;

public class ChekinUser implements Serializable {
    GPS gps;
    String qrCode;
    String scheduleId;

    public ChekinUser() {
    }

    public GPS getGps() {
        return gps;
    }

    public void setGps(GPS gps) {
        this.gps = gps;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public String toString() {
        return "ChekinUser{" +
                "gps=" + gps +
                ", qrCode='" + qrCode + '\'' +
                ", scheduleId='" + scheduleId + '\'' +
                '}';
    }
}
