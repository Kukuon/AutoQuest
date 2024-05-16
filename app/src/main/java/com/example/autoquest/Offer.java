package com.example.autoquest;

public class Offer {
    private String offerId;
    private String brand;
    private String model;
    private String generation;
    private String price;
    private String year;
    private String description;
    private String enginePower;
    private String fuelConsumption;
    private String ownerId;
    private String ownerPhoneNumber;

    public Offer() {
    }

    // конструктор для карточки объявления на главном экране
    public Offer(String brand, String model, String generation, String price, String year) {
        this.brand = brand;
        this.model = model;
        this.generation = generation;
        this.price = price;
        this.year = year;
    }

    public Offer(String offerId, String brand, String model, String generation, String price, String year, String description, String enginePower, String fuelConsumption, String ownerId, String ownerPhoneNumber) {
        this.offerId = offerId;
        this.brand = brand;
        this.model = model;
        this.generation = generation;
        this.price = price;
        this.year = year;
        this.description = description;
        this.enginePower = enginePower;
        this.fuelConsumption = fuelConsumption;
        this.ownerId = ownerId;
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
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

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(String enginePower) {
        this.enginePower = enginePower;
    }

    public String getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(String fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }
}