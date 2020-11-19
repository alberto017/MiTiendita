package com.example.mitiendita.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartModel implements Parcelable {

    private String productID;
    private String image;
    private String productName;
    private String quantity;
    private String price;
    private String discount;
    private String userPhone;

    public CartModel() {

    }

    //Constructor para Favoritos
    public CartModel(String productID, String productName, String quantity, String price, String discount, String image, String userPhone) {
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.image = image;
        this.userPhone = userPhone;
    }

    //Constructor para Food
    public CartModel(String productID, String productName, String quantity, String price, String discount, String image) {
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.image = image;
    }

    //Constructor para Promocion
    public CartModel(String productID, String productName, String quantity, String price, String image) {
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }

    protected CartModel(Parcel in) {
        productID = in.readString();
        productName = in.readString();
        quantity = in.readString();
        price = in.readString();
        discount = in.readString();
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Metodos propios de la implementacion Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productID);
        parcel.writeString(productName);
        parcel.writeString(quantity);
        parcel.writeString(price);
        parcel.writeString(discount);
    }//writeToParcel

}//CartModel
