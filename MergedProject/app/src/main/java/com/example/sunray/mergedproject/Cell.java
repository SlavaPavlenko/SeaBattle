package com.example.sunray.mergedproject;

public class Cell {
    private String imageName;
    private boolean ship;

    public Cell(String imageName, boolean ship) {
        this.ship = ship;
        this.imageName = imageName;
    }

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isShip() {
        return ship;
    }

    public void setShip(boolean value){this.ship = value;}

}