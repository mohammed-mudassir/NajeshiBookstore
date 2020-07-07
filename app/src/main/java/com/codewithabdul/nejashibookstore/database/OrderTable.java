package com.codewithabdul.nejashibookstore.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.codewithabdul.nejashibookstore.models.BookModel;
import com.codewithabdul.nejashibookstore.models.CartModel;
import com.codewithabdul.nejashibookstore.utils.ConstantUtils;

import java.util.ArrayList;

public class OrderTable {


    private static final String CART_TABLE = "tbl_cart";

    private static final String PRIMARY_KEY = "_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String CATEGORY_ID = "category_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String PRODUCT_COMMENT = "comment";
    private static final String AVAILABLE_STOCK = "available_stock";
    private static final String ACTUAL_PRICE = "actual_price";
    private static final String DISCOUNTED_PRICE = "discounted_price";
    private static final String DESCRIPTION = "description";
    private static final String UNIT_ID = "unit_id";
    private static final String UNIT_NAME = "unit_name";
    private static final String STATUS = "status";

    private static final String PRODUCT_IMAGE = "product_image";
    private static final String QUANTITY = "quantity";
    private static final String TOTAL_PRICE = "total_price";
    private static final String TOTAL_QUANTITY = "total_qty";


    public static final String CREATE_CART_TABLE =
            " CREATE TABLE IF NOT EXISTS " + CART_TABLE + "(" + PRIMARY_KEY + " INTEGER PRIMARY KEY, "
                    + PRODUCT_ID + " INTEGER , " + PRODUCT_NAME + " TEXT , " + PRODUCT_IMAGE + " TEXT , " + ACTUAL_PRICE + " TEXT , " + DISCOUNTED_PRICE + " TEXT , " + TOTAL_PRICE + " TEXT , " + QUANTITY + " INTEGER) ";


    private SQLiteDatabase db = null;
    private Context context = null;
    private DataBaseHandler dataBaseHandler = null;

    public OrderTable(Context context) {
        this.context = context;
        this.dataBaseHandler = new DataBaseHandler(context, ConstantUtils.DB_NAME, null, ConstantUtils.DB_VERSION);

    }

    public void initialise() {
        dataBaseHandler.getWritableDatabase();
        dataBaseHandler.close();

    }


    private void insert_item_to_cart(BookModel bookModel, int qty, String total_price) {

        db = dataBaseHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_ID, bookModel.getProduct_id());
/*        contentValues.put(UNIT_ID, itemsModel.getUnit_id());
        contentValues.put(UNIT_NAME, itemsModel.getSymbol());*/
        contentValues.put(PRODUCT_NAME, bookModel.getTitle());
//        contentValues.put(DISCOUNTED_PRICE, itemsModel.getDiscounted_price());
        contentValues.put(PRODUCT_IMAGE, bookModel.getImage());
        contentValues.put(QUANTITY, String.valueOf(qty));
        contentValues.put(TOTAL_PRICE, total_price);
        contentValues.put(ACTUAL_PRICE, bookModel.getPrice());
        db.insert(CART_TABLE, null, contentValues);
        db.close();

    }


    private void update_item_cart(BookModel bookModel, int qty, String total_price) {

        db = dataBaseHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(DISCOUNTED_PRICE, itemsModel.getDiscounted_price());
        contentValues.put(QUANTITY, String.valueOf(qty));/*
        contentValues.put(UNIT_ID, itemsModel.getUnit_id());
        contentValues.put(UNIT_NAME, itemsModel.getSymbol());*/
        contentValues.put(PRODUCT_IMAGE, bookModel.getImage());
        contentValues.put(TOTAL_PRICE, total_price);
        contentValues.put(ACTUAL_PRICE, bookModel.getPrice());
        db.update(CART_TABLE, contentValues, PRODUCT_ID + "=" + bookModel.getProduct_id(), null);

        db.close();


    }

    public int checkItemFromCart(String itemId) {
        int count = 0;

        String selectQuery = " SELECT " + PRODUCT_ID + " FROM " + CART_TABLE + " WHERE " + PRODUCT_ID + " = " + itemId;

        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            count = cursor.getCount();
        }
        db.close();
        return count;
    }


    public int getCartItemQty(String itemId) {

        int qty = 0;

        String selectQuery = " SELECT " + QUANTITY + " FROM " + CART_TABLE + " WHERE " + PRODUCT_ID + " = " + itemId;

        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {


                qty = cursor.getInt(cursor.getColumnIndex(QUANTITY));

            }
        }
        db.close();
        return qty;
    }

    public int getTotalCartItemQty() {

        int qty = 0;

        String selectQuery = " SELECT SUM(" + QUANTITY + ") AS " + TOTAL_QUANTITY + "  FROM " + CART_TABLE;

        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {


                qty = cursor.getInt(cursor.getColumnIndex(TOTAL_QUANTITY));

            }
        }
        db.close();
        return qty;
    }

    public double getTotalCartItemPrice() {

        double price = 0;

        String selectQuery = " SELECT SUM(" + TOTAL_PRICE + ") AS " + TOTAL_PRICE + "  FROM " + CART_TABLE;

        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {


                price = cursor.getDouble(cursor.getColumnIndex(TOTAL_PRICE));

            }
        }
        db.close();
        return price;
    }


    public CartModel cartData() {

        ArrayList<BookModel> bookModels = new ArrayList<>();

        bookModels = getCartItems();

        CartModel cartModel = new CartModel();
        cartModel.setBookModels(bookModels);
        cartModel.setSub_total(String.valueOf(getTotalCartItemQty()));
        cartModel.setTotal(String.valueOf(getTotalCartItemPrice()));
        return cartModel;
    }

    public ArrayList<BookModel> getCartItems() {
        ArrayList<BookModel> productModels = new ArrayList<>();
        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + CART_TABLE + " WHERE " + QUANTITY + " !=0", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    BookModel productModel = new BookModel();
                    productModel.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
                    productModel.setTitle(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
                    productModel.setImage(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE)));

                 /*   productModel.setUnit_id(cursor.getString(cursor.getColumnIndex(UNIT_ID)));
                    productModel.setSymbol(cursor.getString(cursor.getColumnIndex(UNIT_NAME)));
*/
                    productModel.setCount(String.valueOf(cursor.getInt(cursor.getColumnIndex(QUANTITY))));
                    productModel.setTotal_price(cursor.getString(cursor.getColumnIndex(TOTAL_PRICE)));
                    productModel.setPrice(cursor.getString(cursor.getColumnIndex(ACTUAL_PRICE)));
//                    productModel.setDiscounted_price(cursor.getString(cursor.getColumnIndex(DISCOUNTED_PRICE)));


                    productModels.add(productModel);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return productModels;

    }

    public void addToCart(BookModel itemsModel, int qty) {
        String total_price_string = "";
        String item_id = itemsModel.getProduct_id();

//        String discounted_price = itemsModel.getDiscounted_price();
//        Double price =null;
//        if (discounted_price !=null) {
//            price = Double.parseDouble(discounted_price);
//        }
        double price = Double.valueOf(itemsModel.getPrice());

//        Double minimised_qty = qty / 3;
        double total_price = price * qty;

        total_price_string = String.valueOf(total_price);


        if (checkItemFromCart(item_id) == 0) {

            insert_item_to_cart(itemsModel, qty, total_price_string);

        } else {

            update_item_cart(itemsModel, qty, total_price_string);

        }

    }

    public double deleteItemFromCart(String item_id) {

        db = dataBaseHandler.getWritableDatabase();

        int status = db.delete(CART_TABLE, PRODUCT_ID + " =? ", new String[]{item_id});


        return status;

    }

    public double clearCart() {

        db = dataBaseHandler.getWritableDatabase();

        int status = db.delete(CART_TABLE, null, null);


        return status;

    }


}
