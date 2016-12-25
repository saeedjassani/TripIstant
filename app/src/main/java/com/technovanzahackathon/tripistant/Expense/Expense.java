package com.technovanzahackathon.tripistant.Expense;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Pooja S on 9/30/2016.
 */
public class Expense implements Serializable {
    private Long id;
    private String title, note, date;
    private float amount, budget;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getNote(){
        return this.note;
    }

    public void setNote(String note){
        this.note = note;
    }

    public String getDate(){
        return this.note;
    }

    public void setDate(String date){
        this.date = date;
    }

    public float getAmount(){
        return this.amount;
    }

    public void setAmount(float amount){
        this.amount = amount;
    }

    public float getBudget(){
        return this.budget;
    }

    public void setBudget(float budget){
        this.budget = budget;
    }
}

