package com.technovanzahackathon.tripistant.TripChecklists;

import java.io.Serializable;

/**
 * Created by Pooja S on 12/24/2016.
 */

public class Trips implements Serializable{

    String tripname, tripsrc, tripdest, tripdate, triptime;
    int tripdatetimestatus;
    Long tripid;

    public Long getTripId(){
        return tripid;
    }

    public void setTripId(Long tripid){
        this.tripid = tripid;
    }

    public String getTripname() {
        return tripname;
    }

    public void setTripname(String tripname) {
        this.tripname = tripname;
    }

    public String getTripsrc() {
        return tripsrc;
    }

    public void setTripsrc(String tripsrc) {
        this.tripsrc = tripsrc;
    }

    public String getTripdest() {
        return tripdest;
    }

    public void setTripdest(String tripdest) {
        this.tripdest = tripdest;
    }

    public String getTripdate() {
        return tripdate;
    }

    public void setTripdate(String tipdate) {
        this.tripdate = tipdate;
    }

    public String getTriptime() {
        return triptime;
    }

    public void setTriptime(String triptime) {
        this.triptime = triptime;
    }

    public int getTripdatetimestatus() {
        return tripdatetimestatus;
    }

    public void setTripdatetimestatus(int tripdatetimestatus) {
        this.tripdatetimestatus = tripdatetimestatus;
    }
}
