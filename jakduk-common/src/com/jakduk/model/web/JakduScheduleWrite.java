package com.jakduk.model.web;

import com.jakduk.model.db.FootballClub;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by pyohwan on 15. 12. 24.
 */
public class JakduScheduleWrite {

    @Id
    private String id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @NotEmpty
    private String home;

    @NotEmpty
    private String away;

    private Integer homeFirstHalf;

    private Integer awayFirstHalf;

    private Integer homeSecondHalf;

    private Integer awaySecondHalf;

    private Integer homeOvertime;

    private Integer awayOvertime;

    private Integer homePenaltyShootOut;

    private Integer awayPenaltyShootOut;

    private boolean fullTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public Integer getHomeFirstHalf() {
        return homeFirstHalf;
    }

    public void setHomeFirstHalf(Integer homeFirstHalf) {
        this.homeFirstHalf = homeFirstHalf;
    }

    public Integer getAwayFirstHalf() {
        return awayFirstHalf;
    }

    public void setAwayFirstHalf(Integer awayFirstHalf) {
        this.awayFirstHalf = awayFirstHalf;
    }

    public Integer getHomeSecondHalf() {
        return homeSecondHalf;
    }

    public void setHomeSecondHalf(Integer homeSecondHalf) {
        this.homeSecondHalf = homeSecondHalf;
    }

    public Integer getAwaySecondHalf() {
        return awaySecondHalf;
    }

    public void setAwaySecondHalf(Integer awaySecondHalf) {
        this.awaySecondHalf = awaySecondHalf;
    }

    public Integer getHomeOvertime() {
        return homeOvertime;
    }

    public void setHomeOvertime(Integer homeOvertime) {
        this.homeOvertime = homeOvertime;
    }

    public Integer getAwayOvertime() {
        return awayOvertime;
    }

    public void setAwayOvertime(Integer awayOvertime) {
        this.awayOvertime = awayOvertime;
    }

    public Integer getHomePenaltyShootOut() {
        return homePenaltyShootOut;
    }

    public void setHomePenaltyShootOut(Integer homePenaltyShootOut) {
        this.homePenaltyShootOut = homePenaltyShootOut;
    }

    public Integer getAwayPenaltyShootOut() {
        return awayPenaltyShootOut;
    }

    public void setAwayPenaltyShootOut(Integer awayPenaltyShootOut) {
        this.awayPenaltyShootOut = awayPenaltyShootOut;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFullTime(boolean fullTime) {
        this.fullTime = fullTime;
    }

    @Override
    public String toString() {
        return "JakduScheduleWrite{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", home='" + home + '\'' +
                ", away='" + away + '\'' +
                ", homeFirstHalf=" + homeFirstHalf +
                ", awayFirstHalf=" + awayFirstHalf +
                ", homeSecondHalf=" + homeSecondHalf +
                ", awaySecondHalf=" + awaySecondHalf +
                ", homeOvertime=" + homeOvertime +
                ", awayOvertime=" + awayOvertime +
                ", homePenaltyShootOut=" + homePenaltyShootOut +
                ", awayPenaltyShootOut=" + awayPenaltyShootOut +
                ", fullTime=" + fullTime +
                '}';
    }

}