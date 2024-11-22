package com.example.fruitify_offcial.roomdb;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {CartProducts.class}, version = 1, exportSchema = false)
public abstract class CartProductDataBase extends RoomDatabase {

    public abstract CartProductDao cartProductsDao();

    private static volatile CartProductDataBase INSTANCE;

    public static CartProductDataBase getDatabaseInstance(final Context context) {
        CartProductDataBase tempInstance = INSTANCE;
        if (tempInstance != null) {
            return tempInstance;
        }

        synchronized (CartProductDataBase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                                context.getApplicationContext(),
                                CartProductDataBase.class,
                                "CartProducts"
                        )
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }

}
