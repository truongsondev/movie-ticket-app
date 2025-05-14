package com.example.movie_ticket_app.data.model;



public class PaymentMethod {
    private int id;
    private String name;
    private String description;
    private int iconResourceId;
    private boolean isSelected;

    public PaymentMethod(int id, String name, String description, int iconResourceId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconResourceId = iconResourceId;
        this.isSelected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
