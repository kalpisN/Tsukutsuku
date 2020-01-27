package app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)

public class TimeTableRow {
    private String stationShortCode;
    private int operatorUICCode;
    // private String countryCode;
    private String type;
    private boolean trainStopping;
    private boolean commercialStop;
    private int commercialTrack;
    private boolean cancelled;
    private Date scheduledTime;


    public TimeTableRow() {

    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    public int getOperatorUICCode() {
        return operatorUICCode;
    }

    public void setOperatorUICCode(int operatorUICCode) {
        this.operatorUICCode = operatorUICCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTrainStopping() {
        return trainStopping;
    }

    public void setTrainStopping(boolean trainStopping) {
        this.trainStopping = trainStopping;
    }

    public boolean isCommercialStop() {
        return commercialStop;
    }

    public void setCommercialStop(boolean commercialStop) {
        this.commercialStop = commercialStop;
    }

    public int getCommercialTrack() {
        return commercialTrack;
    }

    public void setCommercialTrack(int commercialTrack) {
        this.commercialTrack = commercialTrack;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    public String toString() {
        return "TimeTableRow{" +
                "stationShortCode='" + stationShortCode + '\'' +
                ", operatorUICCode=" + operatorUICCode +
                ", type='" + type + '\'' +
                ", trainStopping=" + trainStopping +
                ", commercialStop=" + commercialStop +
                ", commercialTrack=" + commercialTrack +
                ", cancelled=" + cancelled +
                ", scheduledTime=" + scheduledTime +
                '}';
    }
}
