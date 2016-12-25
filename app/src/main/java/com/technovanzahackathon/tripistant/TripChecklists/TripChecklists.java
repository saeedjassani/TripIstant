package com.technovanzahackathon.tripistant.TripChecklists;

import java.io.Serializable;

/**
 * Created by Pooja S on 12/24/2016.
 */

public class TripChecklists implements Serializable {

    String title, content, reminder_date, reminder_time, updated_at, tripnamec;
    int color;
    Long id;
    int lock_status;

    public Long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReminder_date() {
        return reminder_date;
    }

    public void setReminder_date(String reminder_date) {
        this.reminder_date = reminder_date;
    }

    public String getReminder_time() {
        return reminder_time;
    }

    public void setReminder_time(String reminder_time) {
        this.reminder_time = reminder_time;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getLock_status() {
        return lock_status;
    }

    public void setLock_status(int lock_status){
        this.lock_status = lock_status;
    }

    public String getTripNameC(){
        return tripnamec;
    }

    public void setTripNameC(String tripnamec){
        this.tripnamec = tripnamec;
    }
}
