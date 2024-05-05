package com.example.autoquest;

public class Car {
    private String brand;
    private String model;
    private int price;
    private int year;
    private String fuelType;
    private int enginePower;
    private int engineCapacity;
    private String transmissionType;
    private int imageResource;

    public Car(String brand, String model, int price, int year, String fuelType, int enginePower, int engineCapacity, String transmissionType, int imageResource) {
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.year = year;
        this.fuelType = fuelType;
        this.enginePower = enginePower;
        this.engineCapacity = engineCapacity;
        this.transmissionType = transmissionType;
        this.imageResource = imageResource;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}

