package com.example.mitiendita.Model;

public class FavoritesModel {

    private String FoodId;
    private String FoodName;
    private String FoodPrice;
    private String FoodMenuId;
    private String FoodImage;
    private String FoodDiscount;
    private String FoodDescription;
    private String UserPhone;

    public FavoritesModel() {

    }//Constructor01

    public FavoritesModel(String foodId, String foodName, String foodPrice, String foodMenuId, String foodImage, String foodDiscount, String foodDescription, String UserPhone) {
        FoodId = foodId;
        FoodName = foodName;
        FoodPrice = foodPrice;
        FoodMenuId = foodMenuId;
        FoodImage = foodImage;
        FoodDiscount = foodDiscount;
        FoodDescription = foodDescription;
        UserPhone = UserPhone;
    }//Constructor02

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        FoodPrice = foodPrice;
    }

    public String getFoodMenuId() {
        return FoodMenuId;
    }

    public void setFoodMenuId(String foodMenuId) {
        FoodMenuId = foodMenuId;
    }

    public String getFoodImage() {
        return FoodImage;
    }

    public void setFoodImage(String foodImage) {
        FoodImage = foodImage;
    }

    public String getFoodDiscount() {
        return FoodDiscount;
    }

    public void setFoodDiscount(String foodDiscount) {
        FoodDiscount = foodDiscount;
    }

    public String getFoodDescription() {
        return FoodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        FoodDescription = foodDescription;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}//FavoritesModel
