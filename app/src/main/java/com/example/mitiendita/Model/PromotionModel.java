package com.example.mitiendita.Model;

public class PromotionModel {

    private String Title;
    private String Image;
    private String RestaurantName;
    private String Price;
    private String DateEnd;
    private String RestaurantID;
    private String Status;

    public PromotionModel() {

    }

    public PromotionModel(String title, String image, String restaurantName, String price, String dateEnd, String restaurantID, String status) {
        Title = title;
        Image = image;
        RestaurantName = restaurantName;
        Price = price;
        DateEnd = dateEnd;
        RestaurantID = restaurantID;
        Status = status;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDateEnd() {
        return DateEnd;
    }

    public void setDateEnd(String dateEnd) {
        DateEnd = dateEnd;
    }

    public String getRestaurantID() {
        return RestaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        RestaurantID = restaurantID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
