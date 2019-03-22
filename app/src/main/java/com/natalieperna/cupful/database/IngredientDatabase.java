package com.natalieperna.cupful.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.natalieperna.cupful.models.Ingredient;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class IngredientDatabase {
    private static final String DATABASE_NAME = "ingredients.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "ingredient";

    public static List<Ingredient> getIngredients(Context context) {
        SQLiteDatabase database = new SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION).getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] select = {"name", "gPerCup"};
        String sort = "name";
        queryBuilder.setTables(TABLE);
        Cursor cursor = queryBuilder.query(database, select, null, null, null, null, sort);

        cursor.moveToFirst();

        List<Ingredient> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(new Ingredient(
                    cursor.getString(0),
                    cursor.getInt(1)
            ));
        }

        cursor.close();
        database.close();

        return list;
    }
}
