package com.example.realeastatepriceprediction;

public class model {

    String address;
    String imageurl;
    String location;
    String name;
    String features;
    String Booking;
    String coordinates;



    public model(String address, String imageurl, String location, String name,String features, String booking,String coordinates) {
        this.address = address;
        this.imageurl = imageurl;
        this.location = location;
        this.name = name;
        this.features = features;
        this.Booking = booking;
        this.coordinates=coordinates;


    }
    public model(){

    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getBooking() {
        return Booking;
    }

    public void setBooking(String booking) {
        Booking = booking;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
