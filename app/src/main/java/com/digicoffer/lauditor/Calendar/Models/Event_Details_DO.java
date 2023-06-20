package com.digicoffer.lauditor.Calendar.Models;

import org.json.JSONArray;

import java.util.ArrayList;

public class Event_Details_DO {
    String title;
    String description;
    JSONArray team_name;
    String time;
    String format;
    String offset;
    String offset_location;
    String matter_name;
    String owner_name;

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getMatter_name() {
        return matter_name;
    }

    public void setMatter_name(String matter_name) {
        this.matter_name = matter_name;
    }

    boolean owner;

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public String getOffset_location() {
        return offset_location;
    }

    public void setOffset_location(String offset_location) {
        this.offset_location = offset_location;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDisplay_time() {
        return display_time;
    }

    public void setDisplay_time(String display_time) {
        this.display_time = display_time;
    }

    String display_time;

    public JSONArray getTeam_name() {
        return team_name;
    }

    public void setTeam_name(JSONArray team_name) {
        this.team_name = team_name;
    }

    String entity_name;

    String date;
    boolean all_day;

    public boolean isAll_day() {
        return all_day;
    }

    public void setAll_day(boolean all_day) {
        this.all_day = all_day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public JSONArray getAttachments() {
        return attachments;
    }

    public void setAttachments(JSONArray attachments) {
        this.attachments = attachments;
    }

    JSONArray attachments;
    ArrayList<String> doc_id;

    public ArrayList<String> getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(ArrayList<String> doc_id) {
        this.doc_id = doc_id;
    }

    public ArrayList<String> getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(ArrayList<String> doc_type) {
        this.doc_type = doc_type;
    }

    ArrayList<String> doc_type;

    JSONArray notifications;

    public JSONArray getNotifications() {
        return notifications;
    }

    public void setNotifications(JSONArray notifications) {
        this.notifications = notifications;
    }

    JSONArray Tm_name;

    public JSONArray getTm_name() {
        return Tm_name;
    }

    public void setTm_name(JSONArray tm_name) {
        Tm_name = tm_name;
    }

    String Converted_Start_time;
    String Converted_End_time;

    public String getConverted_End_time() {
        return Converted_End_time;
    }

    public void setConverted_End_time(String converted_End_time) {
        Converted_End_time = converted_End_time;
    }

    public String getConverted_Start_time() {
        return Converted_Start_time;
    }

    public void setConverted_Start_time(String converted_Start_time) {
        Converted_Start_time = converted_Start_time;
    }



    public String getEntity_name() {
        return entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
    }

    public String getTmid() {
        return tmid;
    }

    public void setTmid(String tmid) {
        this.tmid = tmid;
    }

    String tmid;




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepeat_interval() {
        return repeat_interval;
    }

    public void setRepeat_interval(String repeat_interval) {
        this.repeat_interval = repeat_interval;
    }



    public String getTeam_members() {
        return team_members;
    }

    public void setTeam_members(String team_members) {
        this.team_members = team_members;
    }

    public String getClients() {
        return clients;
    }

    public void setClients(String clients) {
        this.clients = clients;
    }


    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public String getFrom_ts() {
        return from_ts;
    }

    public void setFrom_ts(String from_ts) {
        this.from_ts = from_ts;
    }

    public String getTo_ts() {
        return to_ts;
    }

    public void setTo_ts(String to_ts) {
        this.to_ts = to_ts;
    }

    String repeat_interval;
    String team_members;
    String clients;
    boolean isRecurring;
    String from_ts;
    String to_ts;


}