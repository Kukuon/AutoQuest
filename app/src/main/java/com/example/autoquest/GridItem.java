package com.example.autoquest;

public class GridItem {
    private String brand;
    private String model;
    private String generation;
    private String price;
    private String year;

    public GridItem() {
    }

    public GridItem(String brand, String model, String generation, String price, String year) {
        this.brand = brand;
        this.model = model;
        this.generation = generation;
        this.price = price;
        this.year = year;
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
}