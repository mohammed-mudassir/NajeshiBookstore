package com.codewithabdul.nejashibookstore.models;

import java.io.Serializable;

public class AddressModel implements Serializable {
    private String add_id;
    private String title;
    private String description;
    private String contact;
    private String latitude;
    private String longitude;
    private boolean selected;

    public String getAdd_id() {
        return add_id;
    }

    public void setAdd_id(String add_id) {
        this.add_id = add_id;
    }

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
