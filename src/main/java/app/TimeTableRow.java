package app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jdk.jshell.execution.LocalExecutionControl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private LocalTime scheduledTime;


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

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {

       LocalTime time = LocalDateTime.ofInstant(scheduledTime.toInstant(),
               ZoneId.systemDefault()).toLocalTime();
       this.scheduledTime = time;

    }

    @Override
    public String toString() {
        return stationShortCode + ", track " + commercialTrack + " at " + scheduledTime;
    }
}
