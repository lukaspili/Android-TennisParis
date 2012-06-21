package com.siu.android.tennisparis.model;

import com.siu.android.tennisparis.dao.model.Tennis;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class Reservation implements Serializable {

    private String title;
    private Date day;
    private int hour;
    private Tennis tennis;
    private String court;

    public Reservation() {
    }

    public Reservation(String title, Date day, int hour) {
        this.title = title;
        this.day = day;
        this.hour = hour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Tennis getTennis() {
        return tennis;
    }

    public void setTennis(Tennis tennis) {
        this.tennis = tennis;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }
}
