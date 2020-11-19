package com.example.mitiendita.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.mitiendita.Common.Common;
import com.example.mitiendita.Model.CartModel;
import com.example.mitiendita.Model.FavoritesModel;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "GroceriesDB_v01.db";
    private static final int DB_VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }//Database

    public List<CartModel> getCart(String userPhone) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductName", "ProductID", "Quantity", "Price", "Discount","Image"};
        String sqliTable = "OrderDetail";

        sqLiteQueryBuilder.setTables(sqliTable);
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, sqlSelect, null, null, null, null, null);

        final List<CartModel> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                result.add(new CartModel(cursor.getString(cursor.getColumnIndex("ProductID")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),
                        cursor.getString(cursor.getColumnIndex("Price")),
                        cursor.getString(cursor.getColumnIndex("Discount")),
                        cursor.getString(cursor.getColumnIndex("Image"))
                ));
            } while (cursor.moveToNext());
        }
        return result;
    }//getCart


    public void clearCart(String userphone) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s'",userphone);
        sqLiteDatabase.execSQL(query);
    }//addToCart

/*
    public void clearCart() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        sqLiteDatabase.execSQL(query);
    }//addToCart
 */


    public void addToCart(CartModel carritoModel) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail (ProductID,ProductName,Quantity,Price,Discount,Image,UserPhone) VALUES ('%s','%s','%s','%s','%s','%s','%s');",
                carritoModel.getProductID(),
                carritoModel.getProductName(),
                carritoModel.getQuantity(),
                carritoModel.getPrice(),
                carritoModel.getDiscount(),
                carritoModel.getImage(),
                Common.currentUsuarioModel.getPhone());
        sqLiteDatabase.execSQL(query);
    }//addToCart

    public void addToFavorites(FavoritesModel favoritesModel) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites (" +
                        "FoodId,UserPhone,FoodName,FoodMenuId,FoodImage,FoodDiscount,FoodDescription,FoodPrice)" +
                        "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');",
                favoritesModel.getFoodId(),
                favoritesModel.getUserPhone(),
                favoritesModel.getFoodName(),
                favoritesModel.getFoodMenuId(),
                favoritesModel.getFoodImage(),
                favoritesModel.getFoodDiscount(),
                favoritesModel.getFoodDescription(),
                favoritesModel.getFoodPrice());
        db.execSQL(query);
    }//addToFavorites

    public void removeFromFavorites(String foodID,String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FoodId = '%s' and UserPhone = '%s';", foodID,userPhone);
        db.execSQL(query);
    }//addToFavorites

    public boolean isFavorite(String foodID,String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE FoodId = '%s' and UserPhone = '%s';", foodID,userPhone);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }//if
        cursor.close();
        return true;
    }//addToFavorites


    public List<FavoritesModel> getAllFavorites(String userPhone) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"FoodId","UserPhone", "FoodName", "FoodMenuId", "FoodImage", "FoodDiscount","FoodDescription","FoodPrice"};
        String sqliTable = "Favorites";

        sqLiteQueryBuilder.setTables(sqliTable);
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, sqlSelect, "UserPhone=?", new String[]{userPhone}, null, null, null);

        final List<FavoritesModel> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                result.add(new FavoritesModel(
                        cursor.getString(cursor.getColumnIndex("FoodId")),
                        cursor.getString(cursor.getColumnIndex("UserPhone")),
                        cursor.getString(cursor.getColumnIndex("FoodName")),
                        cursor.getString(cursor.getColumnIndex("FoodMenuId")),
                        cursor.getString(cursor.getColumnIndex("FoodImage")),
                        cursor.getString(cursor.getColumnIndex("FoodDiscount")),
                        cursor.getString(cursor.getColumnIndex("FoodDescription")),
                        cursor.getString(cursor.getColumnIndex("FoodPrice"))
                ));
            } while (cursor.moveToNext());
        }
        return result;
    }//getCart

    public void removeFromCart(String productID, String userphone) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s' and ProductID='%s'",userphone,productID);
        sqLiteDatabase.execSQL(query);
    }//removeFromCart

}//Database
