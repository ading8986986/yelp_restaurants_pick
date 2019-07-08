package com.android_examples.rbcdemo.model;

public class RestaurantDetailFeed {
    public String name;
    public String rate;
    public int reviewNum;
    public String category;
    public String addr;
    public String price;
    public String phone;
    public String[] imgUrls;

    public RestaurantDetailFeed(String name, String rate, int reviewNum, String category, String addr, String[] imgUrls){
        this.name=name;
        this.rate=rate;
        this.reviewNum =reviewNum;
        this.category=category;
        this.addr = addr;
        this.imgUrls = imgUrls;
    }

    public RestaurantDetailFeed(){

    }
}
